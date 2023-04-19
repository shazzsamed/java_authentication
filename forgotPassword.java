package UserLoginAndRegister;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

public class forgotPassword {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Security Question 1: What is your Favourite movie ?");
        String answer1 = scanner.nextLine();
        
        System.out.print("Security Question 2: In what city did your parents meet?");
        String answer2 = scanner.nextLine();
        
        System.out.print("Security Question 3: What is your Favourite Car/Bike ?");
        String answer3 = scanner.nextLine();
        // query the unpw table to check if the username exists
        String query = "SELECT * FROM unpw WHERE username=?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginauth?autoReconnect=true&useSSL=false", "root", "password");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String correctAnswer1 = rs.getString("secq1");
                String correctAnswer2 = rs.getString("secq2");
                String correctAnswer3 = rs.getString("secq3");
                if (answer1.equals(correctAnswer1) && answer2.equals(correctAnswer2) && answer3.equals(correctAnswer3)) {
                    System.out.println("Enter New Password: ");
                	String Password = scanner.nextLine();
                    String newPassword = hashPassword(Password);
                    String updateQuery = "UPDATE unpw SET password=? WHERE username=?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setString(1, newPassword);
                        updateStmt.setString(2, username);
                        int rowsAffected = updateStmt.executeUpdate();
                        if (rowsAffected == 1) {
                            System.out.println("Password updated successfully!");
                        } else {
                            System.out.println("Error updating password.");
                        }
                    }
                } else {
                    System.out.println("Security answers do not match.");
                }
            } else {
                System.out.println("Username not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
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
