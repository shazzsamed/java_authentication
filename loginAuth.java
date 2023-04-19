package UserLoginAndRegister;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class loginAuth {

    // database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/loginauth?autoReconnect=true&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    // method to encrypt the password using SHA-256 algorithm
    private static String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
  
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        scanner.close();
        
        try {
            // open a connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // create a statement to execute SQL queries
            Statement stmt = conn.createStatement();

            // query the database for the username and encrypted password
            String query = "SELECT username,password FROM unpw WHERE username='" + username + "'";
            ResultSet rs = stmt.executeQuery(query);

            // if username is found in the database
            if (rs.next()) {
                // compare the encrypted password with the one entered by the user
                String encryptedPassword = rs.getString("password");
                if (encryptPassword(password).equals(encryptedPassword)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Login failed! Check username and password ");
                }
            } else {
                System.out.println("Login failed! Check username and password");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
