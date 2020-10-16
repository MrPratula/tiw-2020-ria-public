package servlet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logOut")
@MultipartConfig

public class LogOut extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public LogOut () {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ServletContext servletContext = getServletContext();

        HttpSession session = request.getSession();
        if (session!=null) {
            session.removeAttribute("user");
            session.invalidate();
        }

        response.sendRedirect(servletContext.getContextPath()+"/index.html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws IOException {
        doGet(request, response);
    }

    public void destroy() {
    }
}
