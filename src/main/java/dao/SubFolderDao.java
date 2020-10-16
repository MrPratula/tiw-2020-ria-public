package dao;

import beans.File;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import java.sql.*;

public class SubFolderDao {

    private final Connection connection;

    public SubFolderDao(Connection connection) {
        this.connection = connection;
    }

    public List<File> getSubFolderFiles(int subfolderId) {

        List<File> files = new ArrayList<>();
        String query = "Select * FROM files WHERE files.owner = ?";

        PreparedStatement preparedStatement;
        ResultSet result;

        // prepare the statement to prevent sql injection
        try {

            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(subfolderId));

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting files");
            return null;
        }

        // make sql query
        try{

            result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst())
                return null;

            else {

                int id, owner;
                String name, extension, creationDate, summary;

                // get all files
                while(result.next()) {
                    id = Integer.parseInt(result.getString("id"));
                    name = result.getString("name");
                    extension = result.getString("extension");
                    creationDate = result.getString("creation_date");
                    summary = result.getString("summary");
                    owner = Integer.parseInt(result.getString("owner"));

                    File file = new File();
                    file.setId(id);
                    file.setName(name);
                    file.setExtension(extension);
                    file.setCreationDate(creationDate);
                    file.setSummary(summary);
                    file.setOwner(owner);

                    files.add(file);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error requesting files");
            return null;
        }
        return files;
    }

    public String getSubFolderName(int id){

        String query = "Select name FROM sub_folders WHERE id = ?";

        PreparedStatement preparedStatement;
        ResultSet result;

        String name = null;

        try {

            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(id));

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting files");
            return null;
        }

        try{

            result = preparedStatement.executeQuery();
            result.next();

            name = result.getString("name");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }

    public boolean isItMine (int subFolderId, int userId) {

        String query = "SELECT * FROM sub_folders WHERE sub_folders.user_owner = ?";

        ResultSet result;
        PreparedStatement preparedStatement;

        try{
            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(userId));

            result = preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting file in SubFolderDao.java updatePosition()");
            e.printStackTrace();
            return false;
        }

        List<Integer> allSubFoldersId = new ArrayList();

        try {
            while (result.next()){
                allSubFoldersId.add(Integer.parseInt(result.getString("id")));
            }
        } catch (SQLException e) {
            return false;
        }

        for (int id: allSubFoldersId){
            if (id == subFolderId)
                return true;
        }

        return false;
    }
}