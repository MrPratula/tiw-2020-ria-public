package servlet;

import beans.User;
import com.google.gson.Gson;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/fileView")
@MultipartConfig

public class File extends HttpServlet {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void init() {
        ServletContext servletContext = getServletContext();
        connection = new SqlConnectionHandler(servletContext).connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SqlQueryHandler sqlQueryHandler = new SqlQueryHandler(connection);
        int fileId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("fileId")));
        User user = (User) request.getSession().getAttribute("user");

        beans.File file = sqlQueryHandler.getFile(fileId, user.getId());

        if (file == null)
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Error getting file in File.java");

        else {

            String jsonFile = new Gson().toJson(file);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(jsonFile);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws IOException {
        doGet(request, response);
    }

    public void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ignore){}
    }
}
