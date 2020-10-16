package dao;

import beans.Folder;
import beans.SubFolder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoldersDao {

    private final Connection connection;

    public FoldersDao(Connection connection) {
        this.connection = connection;
    }

    public List<Folder> getFolders (int userId){

        List<Folder> folders = new ArrayList<>();
        String query = "Select * FROM folders JOIN sub_folders WHERE folders.owner = ? AND sub_folders.owner = folders.id";

        PreparedStatement preparedStatement;
        ResultSet result;

        // prepare the statement to prevent sql injection
        try {

            preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, Integer.toString(userId));

        } catch (SQLException e) {
            System.out.println("Bad prepared statement requesting sub folders");
            return null;
        }

        // make a sql query
        try {

            result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst())
                return null;

            else {

                // result set stuff
                int folderId, folderOwner, subFolderId, subFolderOwner;
                String folderName, subFolderName, creationDate;

                // first cycle to create the folders
                while (result.next()){

                    folderId = Integer.parseInt(result.getString(1));
                    folderName = result.getString(2);
                    creationDate = result.getString(3);
                    folderOwner = Integer.parseInt(result.getString(4));

                    boolean canIAddFolder = true;

                    for (Folder f: folders){
                        if (f.getId() == folderId){
                            canIAddFolder = false;
                            break;
                        }
                    }

                    if (canIAddFolder) {
                        Folder newFolder = new Folder();
                        newFolder.setId(folderId);
                        newFolder.setName(folderName);
                        newFolder.setOwner(folderOwner);
                        newFolder.setCreationDate(creationDate);
                        folders.add(newFolder);
                    }
                }

                result.beforeFirst();

                // second cycle to create the sub folders
                while (result.next()){

                    subFolderId = Integer.parseInt(result.getString(5));
                    subFolderName = result.getString(6);
                    subFolderOwner = Integer.parseInt(result.getString(7));

                    // get the correct folder in which create the sub folders
                    int folderIndex = 0;
                    for (Folder f: folders) {
                        if (f.getId() == subFolderOwner) {
                            break;
                        } else folderIndex++;
                    }

                    List<SubFolder> subFolders = folders.get(folderIndex).getSubFolders();

                    boolean canIAddSubFolder = true;

                    // check if the sub folder exist or not
                    for (SubFolder sf: subFolders){
                        if (sf.getId() == subFolderId){
                            canIAddSubFolder = false;
                            break;
                        }
                    }

                    // if not it is created
                    if (canIAddSubFolder) {
                        SubFolder newSubFolder = new SubFolder();
                        newSubFolder.setId(subFolderId);
                        newSubFolder.setName(subFolderName);
                        newSubFolder.setOwner(subFolderOwner);
                        subFolders.add(newSubFolder);
                    }
                }
            }

        }catch (SQLException e) {
            System.out.println("Error requesting sub folders");
            return null;
        }
        return folders;
    }
}