package servlet;

import beans.File;
import beans.Folder;
import beans.User;
import dao.FileDao;
import dao.FoldersDao;
import dao.SubFolderDao;
import dao.UserDao;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;

public class SqlQueryHandler {

    private final Connection connection;

    public SqlQueryHandler(Connection connection) {
        this.connection = connection;
    }

    public User authenticateUser(HttpServletRequest request) throws NullPointerException{

        String userEmail = StringEscapeUtils.escapeJava(request.getParameter("logInMail"));
        String userPassword  = StringEscapeUtils.escapeJava(request.getParameter("logInPassword"));

        if (!isAGoodMail(userEmail) || !isAGoodString(userPassword))
            throw new NullPointerException();

        UserDao userDao = new UserDao(connection);
        User user = userDao.checkCredentials(userEmail, userPassword);

        if (user == null)
            throw new NullPointerException();
        else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return user;
        }
    }

    public List<Folder> getFolders(int userId){
        FoldersDao foldersDao = new FoldersDao(connection);
        return foldersDao.getFolders(userId);
    }

    public List<File> getSubFolderFiles(int subFolderId, int userId){

        SubFolderDao subFolderDao = new SubFolderDao(connection);

        if (subFolderDao.isItMine(subFolderId, userId))
            return subFolderDao.getSubFolderFiles(subFolderId);
        else
            return null;
    }

    public File getFile(int id, int ownerId) {

        FileDao fileDao = new FileDao(connection);

        if (fileDao.isItMine(id, ownerId))
            return fileDao.getFile(id);
        else
            return null;
    }

    public boolean moveFile(HttpServletRequest request){

        int fileId;
        int newPosition;

        User user = (User) request.getSession().getAttribute("user");

        fileId = Integer.parseInt(request.getParameter("fileId"));
        newPosition = Integer.parseInt(request.getParameter("destination"));

        FileDao fileDao = new FileDao(connection);
        SubFolderDao subFolderDao = new SubFolderDao(connection);

        if (subFolderDao.isItMine(newPosition, user.getId()) && fileDao.isItMine(fileId, user.getId()))
            fileDao.updatePosition(fileId, newPosition);
        else
            return false;

        return true;
    }

    public boolean isAGoodMail (String mail) {

        if (mail==null)
            return false;

        if (mail.isEmpty())
            return false;

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(mail).matches();
    }

    public boolean isAGoodString(String password) {

        if (password == null)
            return false;

        return !password.isEmpty();
    }


    public boolean removeFile(int fileId, int userId) {

        FileDao fileDao = new FileDao(connection);

        if (fileDao.isItMine(fileId, userId)){
            fileDao.removeFile(fileId);
            return true;
        }
        else return false;

    }

    public void createNewUser (HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name, surname, password, confirmPassword, mail;

        name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        surname = StringEscapeUtils.escapeJava(request.getParameter("surname"));
        password = StringEscapeUtils.escapeJava(request.getParameter("password"));
        confirmPassword = StringEscapeUtils.escapeJava(request.getParameter("confirmPassword"));
        mail = StringEscapeUtils.escapeJava(request.getParameter("email"));


        if (isAGoodString(name) && isAGoodString(surname) && isAGoodString(password) && isAGoodString(confirmPassword) && isAGoodMail(mail)) {

            if(password.equals(confirmPassword)) {

                UserDao userDao = new UserDao(connection);
                if (!userDao.insertIntoDb(name, surname, password, mail)) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().println("Mail address is already in use");
                }

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Password doesn't match!");
            }
        } else {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Email was not a valid mail address!");
        }
    }
}
