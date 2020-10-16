package dao;

import beans.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDao {

    private final Connection connection;

    public FileDao(Connection connection) {
        this.connection = connection;
    }

    public File getFile(int fileId) {

        File file = new File();

        String query = "Select * FROM files WHERE files.id = ?";

        PreparedStatement preparedStatement;
        ResultSet result;

        // prepare the statement to prevent sql injection
        try {

            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(fileId));

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting file in FileDao.java");
            return null;
        }

        // make sql query
        try {

            result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst())
                return null;

            else {
                result.next();

                // get the unique file with that id
                file.setName(result.getString("name"));
                file.setId(fileId);
                file.setExtension(result.getString("extension"));
                file.setOwner(Integer.parseInt(result.getString("owner")));
                file.setCreationDate(result.getString("creation_date"));
                file.setSummary(result.getString("summary"));
            }

        } catch (SQLException e) {
            System.out.println("Error requesting file in FileDao.java");
            return null;
        }

        return file;
    }

    public void updatePosition(int id, int newPosition) {

        String query = "UPDATE files SET owner = ? WHERE id = ?";

        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(newPosition));
            preparedStatement.setString(2, Integer.toString(id));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting file in FileDao.java updatePosition()");
            e.printStackTrace();
        }
    }

    public boolean isItMine (int fileId, int userId){

        String query = "SELECT * FROM files WHERE files.user_owner = ?";

        ResultSet result;
        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(userId));

            result = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting file in FileDao.java updatePosition()");
            e.printStackTrace();
            return false;
        }

        List<Integer> allFilesId = new ArrayList();

        try {
            while (result.next()){
                allFilesId.add(Integer.parseInt(result.getString("id")));
            }
        } catch (SQLException e) {
            return false;
        }

        for (int id: allFilesId){
            if (id == fileId)
                return true;
        }

        return false;
    }

    public void removeFile (int fileId){

        String query = "DELETE FROM files WHERE files.id = ?";

        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(fileId));

           preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting file in FileDao.java updatePosition()");
            e.printStackTrace();
        }
    }
}