package servlet;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnectionHandler {

    private final ServletContext servletContext;

    public SqlConnectionHandler (ServletContext servletContext){
        this.servletContext = servletContext;
    }

    public Connection connect() {

        Connection connection;

        try{
            String driver = servletContext.getInitParameter("dbDriver");
            String url = servletContext.getInitParameter("dbUrl");
            String user = servletContext.getInitParameter("dbUser");
            String password = servletContext.getInitParameter("dbPassword");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);

            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
