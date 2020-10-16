package servlet;

import beans.Folder;
import beans.User;
import com.google.gson.Gson;

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

@WebServlet("/Home")
@MultipartConfig

public class Home extends HttpServlet {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void init() {
        ServletContext servletContext = getServletContext();
        connection = new SqlConnectionHandler(servletContext).connect();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SqlQueryHandler sqlQueryHandler = new SqlQueryHandler(connection);

        // put user into session
        User user;
        try {
            user = sqlQueryHandler.authenticateUser(request);
        } catch (NullPointerException e){
            String json = new Gson().toJson("Account not existing");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            return;
        }

        // folders are not into web context but are sent directly to browser
        List<Folder> folders = sqlQueryHandler.getFolders(user.getId());

        // make java object into json string
        String jsonUser = new Gson().toJson(user);
        String jsonFolders = new Gson().toJson(folders);

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");

        // this set those items each one per line into responseText
        response.getWriter().println(jsonUser);
        response.getWriter().println(jsonFolders);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)	throws IOException {
        doPost(request, response);
    }

    public void destroy() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ignore){}
    }
}
