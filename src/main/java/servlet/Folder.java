package servlet;

import beans.File;
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
import java.util.List;

@WebServlet("/folderView")
@MultipartConfig

public class Folder extends HttpServlet {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void init() {
        ServletContext servletContext = getServletContext();
        connection = new SqlConnectionHandler(servletContext).connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SqlQueryHandler sqlQueryHandler = new SqlQueryHandler(connection);

        int subFolderId = Integer.parseInt(StringEscapeUtils.escapeJava(request.getParameter("subFolderId")));
        User user = (User)request.getSession().getAttribute("user");

        List<File> files = sqlQueryHandler.getSubFolderFiles(subFolderId, user.getId());

        if (files == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        else {

            String jsonSubFolder = new Gson().toJson(files);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(jsonSubFolder);
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
