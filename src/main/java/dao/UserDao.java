package dao;

import beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private final Connection connection;

    public UserDao (Connection connection) {
        this.connection = connection;
    }

    public User checkCredentials (String email, String password){

        String query = "Select * FROM users WHERE email = ? AND password = ?";

        PreparedStatement preparedStatement;
        ResultSet result;

        // prepare the statement to prevent sql injection
        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

        } catch (SQLException e) {
            System.out.println("Bad prepared statement");
            return null;
        }

        // make a sql query
        try {

            result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst())
                return null;

            else {
                result.next();
                User user = new User();

                user.setId(Integer.parseInt(result.getString("id")));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setEmail(result.getString("email"));

                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error requesting user to data base");
            return null;
        }
    }

    public boolean insertIntoDb (String name, String surname, String password, String mail) {

        PreparedStatement preparedStatement;

        String query = "INSERT into users (name, surname, password, email) VALUES (?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, mail);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error register user into DB");
            return false;
        }
    }
}