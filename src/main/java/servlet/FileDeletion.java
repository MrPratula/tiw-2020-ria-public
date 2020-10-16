package servlet;

import beans.User;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/fileDeletion")
@MultipartConfig

public class FileDeletion extends HttpServlet {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void init() {
        ServletContext servletContext = getServletContext();
        connection = new SqlConnectionHandler(servletContext).connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        SqlQueryHandler sqlQueryHandler = new SqlQueryHandler(connection);

        int fileId = Integer.parseInt(request.getParameter("fileId"));
        User user = (User) request.getSession().getAttribute("user");

        if (sqlQueryHandler.removeFile(fileId, user.getId()))
            response.setStatus(HttpServletResponse.SC_OK);
        else
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    public void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ignore){}
    }
}
