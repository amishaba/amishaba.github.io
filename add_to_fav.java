package home_search;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;





class favRestaurant {
     int restaurantId;
     String name;
     String address;
     String yelp_link;
     String image_link;

     favRestaurant(int restaurantId, String name,String a,String y,String i) {
        this.restaurantId = restaurantId;
        this.name = name;
        address=a;
        yelp_link=y;
        image_link=i;
       
    }
     public String getName()
     {
    	 return name;
     }
     public String getYelp()
     {
    	 return yelp_link;
     }
}


@WebServlet("/add_to_fav")
public class add_to_fav extends HttpServlet {
	
	
	
	static String username;
    static String name ;
    String imageUrl ;
    String yelpUrl ;
    String address ;
	String todo;
	static String choice;
	
	public String getYelp()
	{
		return yelpUrl;
	}
	
	
	static List<Integer> favoriteRestaurantIds;
	

    private static final String JDBC_URL = "jdbc:mysql://localhost/assn4";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    
    
    private static final String SELECT_USER_ID_QUERY = "SELECT user_id FROM users WHERE username = ?";
    private static final String INSERT_FAVORITE_QUERY = "INSERT INTO favorites (user_id, restaurant_id) VALUES (?, ?)";
    
    private static final String INSERT_RESTAURANT_QUERY =
            "INSERT INTO restaurants (name, address, yelp_link, image_link) " +
            "VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE name = VALUES(name)";
    
    
    
    
    private static final String SELECT_RESTAURANT_BY_ID = "SELECT * FROM restaurants WHERE restaurant_id = ?";
    private static final String DELETE_FAVORITE_QUERY="DELETE FROM favorites WHERE user_id = ? AND restaurant_id = ?";



	
	




	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		System.out.println("inside doGet");
		
        String choice = request.getParameter("choice");
        username=request.getParameter("user");
        
        System.out.println("choice is ="+choice);
        System.out.println("and username is ="+username);
        String json=get_faves(choice);
        System.out.println("ur faves are - "+json);
        response.setContentType("application/json");
        response.getWriter().write(json);
        /** 
         * find the use id in users
         * find the id in faves
         * get the restaurant id from faves
         * get corressponfing rest from restaurants
         * display the restaurant 
         * now we need code to get and display the faves 
         * call get user id
         * 
         * find that id in faves
         * get restaurant accoriding to id 
         * call get or insert restaurant id
         * get that info */	
		// String restaurantName = request.getParameter("name");	
	}
	
	public String getName()
	{
		return name;
	}
	
	
	
	public static String get_faves(String c_s)
	{
		String json_="";
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Step 1: Get user ID
            int userId = getUserId(connection, username);
            favoriteRestaurantIds=getFavoriteRestaurantIds(userId);
            for (int a:favoriteRestaurantIds)
            {
            	System.out.println(a);
            }
            
            List<favRestaurant> favoriteRestaurants = getFavoriteRestaurants(favoriteRestaurantIds);
            
            for (favRestaurant a:favoriteRestaurants)
            {
            	System.out.println(a.address);
            }
            //sort by choice now 
            choice=c_s;        
            
            if (choice.equals("A to Z"))
            {
            	Collections.sort(favoriteRestaurants, Comparator.comparing(favRestaurant::getName));
            }
            else if (choice.equals("Z to A"))
            {
            	Collections.sort(favoriteRestaurants, Comparator.comparing(favRestaurant::getName, Comparator.reverseOrder()));
            	
            }
            else if (choice.equals("Least Recent"))
            {
            	 Collections.reverse(favoriteRestaurants);
            }
            else if (choice.equals("Most Recent"))
            {
            	
            }
            else
            {
            	Collections.sort(favoriteRestaurants, Comparator.comparing(favRestaurant::getYelp).reversed());

            }
            
             json_=convertToJson(favoriteRestaurants);
            System.out.println(json_);
        } catch (SQLException e) {
            // Log the exception or send an appropriate error response
            e.printStackTrace();
        }
		
		return json_;
		
	}
	
	

	
	
	
	
	
	public static List<Integer> getFavoriteRestaurantIds(int userId) {
        List<Integer> favoriteRestaurantIds = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT restaurant_id FROM favorites WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int restaurantId = resultSet.getInt("restaurant_id");
                        favoriteRestaurantIds.add(restaurantId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteRestaurantIds;
    }
	
	
	
	private static List<favRestaurant> getFavoriteRestaurants(List<Integer> restaurantIds) throws SQLException {
		List<favRestaurant> addme =new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Iterate through the list of restaurant IDs
            for (int restaurantId : restaurantIds) {
                // Prepare the SQL statement
                try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_RESTAURANT_BY_ID)) {
                    preparedStatement.setInt(1, restaurantId);

                    // Execute the query
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        // Check if a result is returned
                        if (resultSet.next()) {
                            // Retrieve and display restaurant details
                            int id = resultSet.getInt("restaurant_id");
                            String name = resultSet.getString("name");
                            String address = resultSet.getString("address");
                            String yelpLink = resultSet.getString("yelp_link");
                            String imageLink = resultSet.getString("image_link");

                            System.out.println("Restaurant ID: " + id);
                            System.out.println("Name: " + name);
                            System.out.println("Address: " + address);
                            System.out.println("Yelp Link: " + yelpLink);
                            System.out.println("Image Link: " + imageLink);
                            System.out.println("----------------------");
                            String k=address;
                            address=imageLink;
                            imageLink=k;
                            favRestaurant a=new favRestaurant(id,name,address,yelpLink,imageLink);
                            addme.add(a);
                            
                            
                        } else {
                            System.out.println("No restaurant found with ID: " + restaurantId);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		  return addme;
	}
	
	
	
	public static String convertToJson(List<favRestaurant> restaurants) {
        // Create Gson object
        Gson gson = new Gson();

        // Convert ArrayList to JSON
        String json = gson.toJson(restaurants);

        return json;
    }
	
	
	


	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract restaurant information from parameters
		System.out.println("in post favs");
		 username=request.getParameter("user");
		 System.out.println("who is = "+request.getParameter("user"));
         name = request.getParameter("name");
         imageUrl = request.getParameter("imageUrl");
         yelpUrl = request.getParameter("yelpUrl");
         address = request.getParameter("address");
         todo=request.getParameter("todo");
         System.out.println("you are = "+username);
        System.out.println(name);
        System.out.println(imageUrl);
        System.out.println(yelpUrl);
        System.out.println(address);
        System.out.println(username);   
        choice=request.getParameter("choice");
        System.out.println("you want to "+ request.getParameter("choice"));
        
        if (choice.equals("add")) {
        	addToFavorites(username,name,imageUrl,yelpUrl, address);
        }
        else
        {
        	removeFromFavorites(username,name);
        	System.out.println("removed");
        }
        


        // Send a success response
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Restaurant added successfully\"}");
    }
	
	
	
	
	public static void removeFromFavorites(String username, String name) {
	    try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
	        // Step 1: Get user ID
	        int userId = getUserId(connection, username);

	        // Step 2: Get restaurant ID
	        int restaurantId = getExistingRestaurantId(connection, name);

	        // Step 3: Remove from favorites
	        removeFromFavorites(connection, userId, restaurantId);

	        System.out.println("Removed from favorites successfully.");

	    } catch (SQLException e) {
	        // Log the exception or send an appropriate error response
	        e.printStackTrace();
	    }
	}

	private static void removeFromFavorites(Connection connection, int userId, int restaurantId) throws SQLException {
	    try (PreparedStatement statement = connection.prepareStatement(DELETE_FAVORITE_QUERY)) {
	        statement.setInt(1, userId);
	        statement.setInt(2, restaurantId);
	        statement.executeUpdate();
	    }
	}

	
	
	
	
	
	
	
	
	
	public static void addToFavorites(String username, String name, String imageUrl, String yelpUrl, String address) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Step 1: Get user ID
        	
            int userId = getUserId(connection, username);
            System.out.println("ur id is ="+userId);

            // Step 2: Get or insert restaurant and get restaurant ID
            int restaurantId = getOrInsertRestaurantId(connection, name, imageUrl, yelpUrl, address);
            
            System.out.println("rest id is ="+restaurantId);

            // Step 3: Add to favorites
            addToFavorites(connection, userId, restaurantId);

            System.out.println("Added to favorites successfully.");

        } catch (SQLException e) {
            // Log the exception or send an appropriate error response
            e.printStackTrace();
        }
    }

    public static int getUserId(Connection connection, String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_USER_ID_QUERY)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            }
        }
        throw new SQLException("User not found.");
    }

    private static void addToFavorites(Connection connection, int userId, int restaurantId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_FAVORITE_QUERY)) {
            statement.setInt(1, userId);
            statement.setInt(2, restaurantId);
            statement.executeUpdate();
        }
    }
    
    

    
    
    
    private static int getOrInsertRestaurantId(Connection connection, String name, String imageUrl, String yelpUrl, String address)
            throws SQLException {
        // Check if the restaurant already exists
        int existingRestaurantId = getExistingRestaurantId(connection, name);
        
        if (existingRestaurantId != -1) {
            // Restaurant already exists, return the existing ID
            return existingRestaurantId;
        } else {
            // Restaurant doesn't exist, insert a new one
            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_RESTAURANT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, name);
                insertStatement.setString(2, imageUrl);

                if (yelpUrl != null) {
                    insertStatement.setString(3, yelpUrl);
                } else {
                    insertStatement.setNull(3, java.sql.Types.VARCHAR);
                }

                insertStatement.setString(4, address);
                insertStatement.executeUpdate();

                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        throw new SQLException("Failed to get or insert restaurant.");
    }

    private static int getExistingRestaurantId(Connection connection, String name) throws SQLException {
        String selectQuery = "SELECT restaurant_id FROM restaurants WHERE name = ?";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, name);

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("restaurant_id");
                }
            }
        }
        // Return -1 if the restaurant with the given name doesn't exist
        return -1;
    }


 

  
    
    public static void main(String[]args)
    {
    	
    	username="catsn";
    	choice="remove";
    	name="iki";
    	removeFromFavorites(username,name);
    }
    
    
    
    
	
	
	
	
	
	
	
	
	

}
