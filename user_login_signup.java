package home_search;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;

import java.sql.Statement;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * for login calls - 
 * first check if login was successful or not
 * if yes - go to new page that will have user fav+reservations+a new html page
 * bad login - display message and reset fields
 * 
 * for sign up calls - 
 * add user details to user tables 
 * also need to go to that same new html page just that  fav+res will be empty */


@WebServlet("/user_login_signup")
public class user_login_signup extends HttpServlet{
	
	private static final String db_url="jdbc:mysql://localhost/assn4";
	private static final String user = "root";
    private static final String pass = "root";
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 System.out.println("inside do get");
		 System.out.println();
		 
		 
	        // Get user input from query parameters
	        
	        String choice = request.getParameter("s_c");
	        System.out.println("choice="+choice);
	        boolean a=false;
	        if (choice.equals("login"))
	        {
	        	String username=request.getParameter("l_u");
	        	String password=request.getParameter("l_p");
	        	System.out.println("username = "+username+" and password = "+password);
	        	//call the sql database here 
	        	a=login_call(username,password);
	        }
	        else if (choice.equals("signup"))
	        {
	        	String username=request.getParameter("s_u");
	        	String password=request.getParameter("s_p");
	        	String email=request.getParameter("s_e");
	        	System.out.println("NEWWWWW username = "+username+" and password = "+password +" and email = "+email);
	        	//call sql database
	        	a=signup_call(username,password,email);
	        }
	        if (a==true)
	        {
	        	response.setContentType("text/plain");
	        	String message = "ok!";
	        	response.getWriter().write(message);
	        }
	        else
	        {
	        	response.setContentType("text/plain");
	        	String message = "error";
	        	response.getWriter().write(message);
	        }
	        
	        
	        /**
	         * if the login or signup are successful, i need to send - 
	         * user details
	         * reservations 
	         * favorites
	         * to the next html file 
	         * but rlly i can just send the UID most likely  */
	        
	        
	        

	      
	    }
	 
	 
	 
	 public static boolean login_call(String username,String password)
	 {
		 try 
		 {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn=DriverManager.getConnection(db_url,user,pass);
			
			Statement stmt=conn.createStatement();
			String sql = "SELECT COUNT(*) FROM " + "users" + " WHERE " + "username" + "=?";
			//ResultSet rs=stmt.executeQuery(sql);
			 // Prepare and execute the SQL query
            
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if the username exists in the table
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count>0)
                        {
                        System.out.println("yes you exist"); 
                        return true;
                        }// If count > 0, the username exists
                    }
                }
            }
		 } 
		 
		 
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 System.out.println("you dont exist!");
		 return false;
	 }
	 
	 
	 
	 public static boolean signup_call(String username,String password,String email)
	 {
		 
		 try {
	            // Load the JDBC driver
	            Class.forName("com.mysql.cj.jdbc.Driver");

	            // Establish a connection
	            try (Connection connection = DriverManager.getConnection(db_url,user, pass)) {

	                // Prepare and execute the SQL query for user insertion
	                String sql = "INSERT INTO " + "users" + " (" + "username" + ", " + "password" + ", " + "email" + ") VALUES (?, ?, ?)";
	                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
	                    preparedStatement.setString(1, username);
	                    preparedStatement.setString(2, password);
	                    preparedStatement.setString(3, email);

	                    // Execute the update
	                    int affectedRows = preparedStatement.executeUpdate();

	                    if (affectedRows > 0) {
	                        // Retrieve the auto-generated userid (if any)
	                        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	                            if (generatedKeys.next()) {
	                                long userId = generatedKeys.getLong(1);
	                                System.out.println("User inserted successfully with userid: " + userId);
	                                return true;
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace(); // Handle exceptions according to your needs
	        }
		 
		 return false;
		 
	 }
	 
	 
	 
	 public static void main(String[]args)
	 {
		 String username="ayushi";
		 String password="poopface";
		 String email="catti";
		 signup_call(username,password,email);

	 }
}
	 