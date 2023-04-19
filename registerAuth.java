package UserLoginAndRegister;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

//This is a program to register a new user. All the details(username,password,email address) are stored in a table called unpw in database loginauth
//Pre-requisites: To connect to a MySQL database using Java, you need to have the MySQL Connector/J JDBC driver in your classpath.
// This JDBC driver allows your Java application to connect to a MySQL database and execute SQL statements.
public class registerAuth {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/loginauth?autoReconnect=true&useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        if (isUsernameTaken(username)) {
            System.out.println("Username already taken.");
            return;
        }

        System.out.print("Security Question 1: What is your Favourite movie ?");
        String secq1 = scanner.nextLine();
        
        System.out.print("Security Question 2: In what city did your parents meet?");
        String secq2 = scanner.nextLine();
        
        System.out.print("Security Question 3: What is your Favourite Car/Bike ?");
        String secq3 = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (!isValidPassword(password)) {
            System.out.println("Invalid password.");
            return;
        }

        String hashedPassword = hashPassword(password);
        
        try {
        	Class.forName("com.mysql.jdbc.Driver");
        	Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        	Statement stmt = conn.createStatement();
        	
            String sql = String.format("INSERT INTO unpw (username, password, secq1, secq2, secq3) VALUES ('%s', '%s', '%s','%s','%s')",
                                       username, hashedPassword, secq1, secq2, secq3);
            int rowsAffected = stmt.executeUpdate(sql);
            if (rowsAffected == 1) {
                System.out.println("Registration successful.");
                return;
            } else {
                System.out.println("Registration failed.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed.");
            e.printStackTrace();
        }
    }

    private static boolean isUsernameTaken(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = String.format("SELECT * FROM unpw WHERE username='%s'", username);
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }



    private static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedPassword) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
