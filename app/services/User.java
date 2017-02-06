package services;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.ResultSet; 
import java.sql.SQLException;


public class User {
    String email;
    String password;
    String salt;

    public User(){ }

    public User(String email, String password) {
        this.email = email;
        this. salt = BCrypt.gensalt();
        this.password = BCrypt.hashpw(password, salt);
    }
    
    public String getEmail(){
        return this.email;
    }

    public String getSalt(){
        return this.salt;
    }

    public String getPassword(){
        return this.password;
    }

    private static Boolean comparePasswords(String hashedPassword, String plainPassword, String salt) {
        System.out.println(plainPassword);
        return hashedPassword.equals(BCrypt.hashpw(plainPassword, salt));
    }

    public static Boolean existsInDatabase(String email, String password) throws SQLException {
        ResultSet rs = DatabaseService.executeQuery("SELECT password, salt FROM Users WHERE username LIKE ?" , email);
        if (rs != null && rs.next()) {
            String fetchedPass = rs.getString("password");
            String salt = rs.getString("salt");
            return comparePasswords(fetchedPass, password, salt);
        }
        return false;
    }

    public static Boolean insertToDatabase(String email, String password) throws SQLException {
        User user = new User(email, password);

        DatabaseService.executeUpdate("INSERT INTO Users (username, password, salt) " + 
        "VALUES (?,?,?)", user.getEmail(), user.getPassword(), user.getSalt());

        return true;
    }

}