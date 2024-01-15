package home_search;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


class resRestaurant {
    String name;
    String address;
    String yelp_link;
    String image_link;
    String date;
    String time;
    resRestaurant(String a,String b,String c,String d,String e,String f)
    {
    	name=a;
    	address=b;
    	yelp_link=c;
    	image_link=d;
    	date=e;
    	time=f;
    }

  
}


class rest_details{
	String name;
	String address;
	String yelp_link;
	String image_link;
	rest_details(String a,String b,String c,String d)
	{
		name=a;
		address=b;
		yelp_link=c;
		image_link=d;
	}
}


@WebServlet("/add_res")
public class add_res extends HttpServlet{
	
	

	
	
	
	
	String date;
	String time;
	String username;
	String rname;
	String radd;
	String rimg;String ryelp;
	 List<resRestaurant> reservations=new ArrayList<>();
	 static List<Integer> favoriteRestaurantIds;
	
	
	 private static final String JDBC_URL = "jdbc:mysql://localhost/assn4";
	 private static final String USERNAME = "root";
	 private static final String PASSWORD = "root";
	 
	 private static final String INSERT_RESTAURANT_QUERY =
	            "INSERT INTO restaurants (name, address, yelp_link, image_link) " +
	            "VALUES (?, ?, ?, ?) " +
	            "ON DUPLICATE KEY UPDATE name = VALUES(name)";
	 private static final String SELECT_RESTAURANT_BY_ID = "SELECT * FROM restaurants WHERE restaurant_id = ?";
	 
	 
	 
	 
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
		{
			System.out.println("inside doGet reservations");
			
	        String choice = request.getParameter("choice");	   
	         username = request.getParameter("un");	 
	        System.out.println("choice is ="+choice);
	        System.out.println("and username is ="+username);
	        /**
	         * get the user ID using getUserId(same)
	         * get the ID's of the reservations using getFavoriteRestaurantIds (change it to access reservations instead
	         * pass the ID arraylist to getFavoriteRestaurants (Same)*/
	        String json=get_reservations(choice);
	        System.out.println(json);
	        response.setContentType("application/json");
	        response.getWriter().write(json);
	       
			// String restaurantName = request.getParameter("name");	
		}
	 
	 
	 private String get_reservations(String c)
	 {
		 String json_="";
			try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
	            // Step 1: Get user ID
	            int userId = add_to_fav.getUserId(connection, username);
	            
	            
	            List<resRestaurant> res_=getReservationDetails(userId);
	            
	            if (c.equals("recent"))
	            {
	            	System.out.println("sort by recent");
	            	sortByDateTime(res_,true);
	            }
	            else
	            {
	            	sortByDateTime(res_,false);
	            }
	            
	            
	            
	            
	           // favoriteRestaurantIds=getReservationRestaurantIds(userId);
	          
	            /*
	            
	            //sort by choice now 
	            
	             json_=convertToJson(favoriteRestaurants);*/
	            json_=convertToJson(res_);
	            System.out.println(json_);
	        } catch (SQLException e) {
	            // Log the exception or send an appropriate error response
	            e.printStackTrace();
	        }
			
			return json_;
		 
	 }
	 
	 
	 
	    public static List<resRestaurant> getReservationDetails(int userId) {
	        List<resRestaurant> reservationDetailsList = new ArrayList<>();

	        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
	          /*  String sql = "SELECT r.name AS restaurant_name, re.date, re.time " +
	                     "FROM reservations re " +
	                     "JOIN restaurants r ON re.restaurant_id = r.restaurant_id " +
	                     "WHERE re.user_id = ?";*/
	        	  String sql = "SELECT r.name AS restaurant_name, r.address, r.yelp_link, r.image_link, re.date, re.time " +
	                      "FROM reservations re " +
	                      "JOIN restaurants r ON re.restaurant_id = r.restaurant_id " +
	                      "WHERE re.user_id = ?";
	        	   
	            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	                preparedStatement.setInt(1, userId);

	                try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                    while (resultSet.next()) {
	                    	 String restaurantName = resultSet.getString("restaurant_name");
	                         String address = resultSet.getString("address");
	                         String yelpLink = resultSet.getString("yelp_link");
	                         String imageLink = resultSet.getString("image_link");
	                         String date = resultSet.getString("date");
	                         String time = resultSet.getString("time");
	                         String k=address;
	                         address=imageLink;
	                         imageLink=k;
	                         
	                         resRestaurant me=new resRestaurant(restaurantName,address,yelpLink,imageLink,date,time);
	                         reservationDetailsList.add(me);

	                       
	                        System.out.println(restaurantName+" "+date+" "+time);
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return reservationDetailsList;
	    }
	    
	    
	    
	    
	    public static void sortByDateTime(List<resRestaurant> restaurants, boolean earliestToLatest) {
	        Collections.sort(restaurants, new Comparator<resRestaurant>() {
	            @Override
	            public int compare(resRestaurant restaurant1, resRestaurant restaurant2) {
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	                try {
	                    // Parse the date and time strings to compare
	                    long dateTime1 = dateFormat.parse(restaurant1.date + " " + restaurant1.time).getTime();
	                    long dateTime2 = dateFormat.parse(restaurant2.date + " " + restaurant2.time).getTime();

	                    // Compare based on the chosen order
	                    return earliestToLatest ? Long.compare(dateTime1, dateTime2) : Long.compare(dateTime2, dateTime1);
	                } catch (ParseException e) {
	                    e.printStackTrace(); // Handle parsing exception as needed
	                    return 0;
	                }
	            }
	        });
	    }
	 
	 
	 
	 
	 
	 
	
	 
	 
	 public static String convertToJson(List<resRestaurant> restaurants) {
	        // Create Gson object
	        Gson gson = new Gson();

	        // Convert ArrayList to JSON
	        String json = gson.toJson(restaurants);
	        return json;
	    }
	 
	 
	
	//to add a reservations from user.js
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		date=request.getParameter("date");
		time=request.getParameter("time");
		username=request.getParameter("user");
		rname=request.getParameter("restn");
		radd=request.getParameter("resta");
		rimg=request.getParameter("resti");
		ryelp=request.getParameter("resty");
		System.out.println("name is post reservations is ="+username);
		
		add_a_res();
		System.out.println("added!");
	}
	
	public void add_a_res()
	{
		try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
			int uid =add_to_fav.getUserId(connection,username);
			int rid=getOrInsertRestaurantId(connection,rname,rimg,ryelp,radd);
			//add this to reservations done
			addReservation(uid,rid,date,time);
			System.out.println("reservation added !");
			resRestaurant me=new resRestaurant("","","",rname,radd,ryelp);
			reservations.add(me);
		}catch (SQLException e) {
            // Log the exception or send an appropriate error response
            e.printStackTrace();
        }

	}
	
	
	
	public void addReservation(int userId, int restaurantId, String date, String time) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // Insert a new reservation into the reservations table
            String sql = "INSERT INTO reservations (user_id, restaurant_id, date, time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, restaurantId);
                preparedStatement.setString(3, date);
                preparedStatement.setString(4, time);

                // Execute the update
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            // Log the exception or send an appropriate error response
            e.printStackTrace();
        }
    }
	
	//change this to add or find from the fav servlet 
	
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
		add_res me=new add_res();
		me.username="haj";
		/*
		me.date="2011/11/01";
		me.time="11:11";
		me.rname  ="iki";
		me.add_a_res();*/
		me.get_reservations("latest");
				
	}
}
