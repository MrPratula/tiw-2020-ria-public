package servlet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/MoveFile")
@MultipartConfig

public class MoveFile extends HttpServlet {

    private Connection connection;
    private static final long serialVersionUID = 1L;

    public void init() {
        ServletContext servletContext = getServletContext();
        connection = new SqlConnectionHandler(servletContext).connect();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        SqlQueryHandler sqlQueryHandler = new SqlQueryHandler(connection);


        if (!sqlQueryHandler.moveFile(request)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws IOException {
        doGet(request, response);
    }
}
