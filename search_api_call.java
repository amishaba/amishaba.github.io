package home_search;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.util.List;


class Restaurant{
	String name;
	double latitude;
	double longitue;
	String address;
	String cuisine;
	String price;
	String rating;
	String phone;
	String yelp_link;
	String img;
	
	
	Restaurant(String n,double la,double li,String a,String c,String p,String r,String po,String l)
	{
		name=n;address=a;cuisine=c;price=p;
		rating=r;phone=po;yelp_link=l;
		latitude=la;
		longitue=li;
	}
	
	Restaurant(String n,double la,double li,String a,String c,String p,String r,String po,String l, String url)
	{
		name=n;address=a;cuisine=c;price=p;
		rating=r;phone=po;yelp_link=l;
		latitude=la;
		longitue=li;
		img=url;
	}
}

/*
 * 34.0211
 * -118.2871*/

@WebServlet("/search_api_call")
public class search_api_call extends HttpServlet{
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Get user input from query parameters
	        String restaurantName = request.getParameter("name");
	        String latitude = request.getParameter("latitude");
	        String longitude = request.getParameter("longitude");
	        String choice = request.getParameter("choice");

	        // Perform the Yelp API request and response processing here 
	        // You can use Java libraries like HttpClient to make the API request to Yelp

	        // Process the Yelp API response and create a JSON response
	        String jsonResponse = processYelpAPI(restaurantName, latitude, longitude, choice);
	        System.out.println(""); System.out.println(""); System.out.println("the json-");
	        System.out.println(jsonResponse);
	        response.setContentType("application/json");
	        response.getWriter().write(jsonResponse);
	        
	        
	        
	       // List<Restaurant> restaurants = extractRestaurantsFromJson(jsonResponse);

	        // Set the data as an attribute in the request object

	        // Forward the data to the JSP page for rendering
	        
	        
	        /*
	        request.setAttribute("restaurants", restaurants); // Set the ArrayList as an attribute
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/results.jsp");
	        dispatcher.forward(request, response);*/

	    }
	 
	
	 

	 
	 public static String processYelpAPI(String name, String lat,String longi,String choice)
		{
		 
			//what all info to pull? edit restaurant object accordingly 
			System.out.println("inside reading rest api");
			 String apiKey = "PJrNPRdtoxXXhyQueXOepnVARKVZ3S6hUzTTDgerWCXLN5HDB2eYK9m6wYn0q4n6VaskqxBuJerdMQFse9f0AS1aNnnWIX4Np5n9QPD3HR7PwlwS_IOx_IJKv2UjZXYx";
				 String searchTerm=name;
				 String encodedSearchTerm="";
				
		         try {
					 encodedSearchTerm = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				// Build the URI with dynamic parameters
		         
		         
		         String url = "https://api.yelp.com/v3/businesses/search" +
		                 "?latitude=" + lat +
		                 "&longitude=" + longi +
		                 "&term=" + encodedSearchTerm +
		                 "&sort_by=" + choice +"&limit=20";
		         
		         
		            URI uri = URI.create(url);
		           
		            HttpRequest request = HttpRequest.newBuilder()
		            		.uri(URI.create(url))
		                    //.uri(URI.create("https://api.yelp.com/v3/businesses/search?location=Los%20Angeles&term=" + encodedSearchTerm + "&sort_by=best_match&limit=10"))
		                    .header("accept", "application/json")
		                    .header("Authorization", "Bearer R01aIgDx960hLTn2aiwSnfkqi_6XdCGNSY5xAZSeTJyfZypXugeEAqrxFviKzdMoJirBPvJvozDSYbUPBjSGztclnWw074JwoftOKNe-72PkTGKdkQuzNC3pqGo5ZXYx")
		                    .method("GET", HttpRequest.BodyPublishers.noBody())
		                    .build();
		                HttpResponse<String> response=null;
						try {
							response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		           
		            
		            String jsonResponse = response.body(); // Paste the JSON response here

		            // Parse the JSON response
		            JsonParser jsonParser = new JsonParser();
		            JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

		            // Extract business data
		            JsonArray businesses = jsonObject.getAsJsonArray("businesses");
		            if (businesses==null)
		            {
		            	System.out.println("something went wrong");
		            	System.exit(0);
		            }
		            
		            
		            
		            List<Restaurant >res=extractRestaurantsFromJson(jsonResponse);
		            System.out.println(""); System.out.println(""); System.out.println("the json-");
			        System.out.println(jsonResponse);
			        
			        
			        
			        //String jr="{\"businesses\": [{\"id\": \"MlmcOkwaNnxl3Zuk6HsPCQ\", \"alias\": \"slurpin-ramen-bar-los-angeles-los-angeles\", \"name\": \"Slurpin' Ramen Bar - Los Angeles\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/axO_FH4VwDYcPQOuabFi6g/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/slurpin-ramen-bar-los-angeles-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 5636, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.0573614429986, \"longitude\": -118.306769744705}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"3500 W 8th St\", \"address2\": null, \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90005\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"3500 W 8th St\", \"Los Angeles, CA 90005\"]}, \"phone\": \"+12133888607\", \"display_phone\": \"(213) 388-8607\", \"distance\": 1425.7025690501273}, {\"id\": \"0kXAJeO1rL4zxa8sa7zL3g\", \"alias\": \"iki-ramen-los-angeles-5\", \"name\": \"Iki Ramen\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/g7cwgu-fRZyJTdlfpsWhDg/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/iki-ramen-los-angeles-5?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 736, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"izakaya\", \"title\": \"Izakaya\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.05844, \"longitude\": -118.3087}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"740 S Western Ave\", \"address2\": \"Ste 116\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90005\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"740 S Western Ave\", \"Ste 116\", \"Los Angeles, CA 90005\"]}, \"phone\": \"+14243357749\", \"display_phone\": \"(424) 335-7749\", \"distance\": 1232.4161525177526}, {\"id\": \"E4W2T89vm4hmBwk39EoUuw\", \"alias\": \"tatsu-ramen-los-angeles-4\", \"name\": \"Tatsu Ramen\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/ocrE0sRjOB-ZuMDsJ7YhKw/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/tatsu-ramen-los-angeles-4?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 4119, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.0836332933004, \"longitude\": -118.34466214165636}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"7111 Melrose Ave\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90046\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"7111 Melrose Ave\", \"Los Angeles, CA 90046\"]}, \"phone\": \"+13238799332\", \"display_phone\": \"(323) 879-9332\", \"distance\": 3256.751873264574}, {\"id\": \"Rwv9F0y6r0MxaojuSRWgCQ\", \"alias\": \"ramen-nagi-los-angeles\", \"name\": \"Ramen Nagi\", \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/I5jWQoHj6TGKSUK_vebqBQ/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/ramen-nagi-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 533, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.058763, \"longitude\": -118.419009}, \"transactions\": [], \"price\": \"$$\", \"location\": {\"address1\": \"10250 Santa Monica Blvd\", \"address2\": \"Ste 2850\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90067\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"10250 Santa Monica Blvd\", \"Ste 2850\", \"Los Angeles, CA 90067\"]}, \"phone\": \"\", \"display_phone\": \"\", \"distance\": 8984.877767628835}, {\"id\": \"RJi83-A92s-KqFutK9sVdQ\", \"alias\": \"tonchin-la-los-angeles\", \"name\": \"Tonchin LA\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/GQ-0SVE3gHtE7EaVGrdDLQ/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/tonchin-la-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 230, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"cocktailbars\", \"title\": \"Cocktail Bars\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.08367939117117, \"longitude\": -118.32395787428786}, \"transactions\": [\"restaurant_reservation\"], \"price\": \"$$\", \"location\": {\"address1\": \"5665 Melrose Ave\", \"address2\": \"\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90038\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"5665 Melrose Ave\", \"Los Angeles, CA 90038\"]}, \"phone\": \"+13233806072\", \"display_phone\": \"(323) 380-6072\", \"distance\": 2467.720046050809}, {\"id\": \"iSZpZgVnASwEmlq0DORY2A\", \"alias\": \"daikokuya-little-tokyo-los-angeles\", \"name\": \"Daikokuya Little Tokyo\", \"image_url\": \"https://s3-media3.fl.yelpcdn.com/bphoto/I48pvsYsOvZdG6Mr3364Kw/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/daikokuya-little-tokyo-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 9489, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.04997674306073, \"longitude\": -118.24009370981828}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"327 E 1st St\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90012\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"327 E 1st St\", \"Los Angeles, CA 90012\"]}, \"phone\": \"+12136261680\", \"display_phone\": \"(213) 626-1680\", \"distance\": 7598.99810760939}, {\"id\": \"FoEMpu0X_bGCb7yRreMxMQ\", \"alias\": \"tsujita-la-artisan-noodle-los-angeles-2\", \"name\": \"Tsujita LA Artisan Noodle\", \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/sOeDcCcbiHBYhFp4jpK-6A/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/tsujita-la-artisan-noodle-los-angeles-2?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 4830, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.0395785158514, \"longitude\": -118.442669604805}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"2057 Sawtelle Blvd\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90025\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"2057 Sawtelle Blvd\", \"Los Angeles, CA 90025\"]}, \"phone\": \"+13102317373\", \"display_phone\": \"(310) 231-7373\", \"distance\": 11439.221490434153}, {\"id\": \"Cln5nzmk0idvBbcXorWboQ\", \"alias\": \"iki-ramen-los-angeles-8\", \"name\": \"Iki Ramen\", \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/sVLjVM5rhKiehltUzuslnw/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/iki-ramen-los-angeles-8?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 117, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}], \"rating\": 5.0, \"coordinates\": {\"latitude\": 34.098484, \"longitude\": -118.3331}, \"transactions\": [\"delivery\", \"pickup\"], \"location\": {\"address1\": \"6565 Sunset Blvd\", \"address2\": null, \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90028\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"6565 Sunset Blvd\", \"Los Angeles, CA 90028\"]}, \"phone\": \"+12135129552\", \"display_phone\": \"(213) 512-9552\", \"distance\": 4239.115506285202}, {\"id\": \"wpqPm7og9vgb3b1DnuyGGw\", \"alias\": \"susuru-ramen-bar-los-angeles\", \"name\": \"Susuru Ramen Bar\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/La_-huyj8KOaqQ7CGl9HKA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/susuru-ramen-bar-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 158, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 5.0, \"coordinates\": {\"latitude\": 34.102, \"longitude\": -118.30291}, \"transactions\": [], \"price\": \"$$\", \"location\": {\"address1\": \"5179 Hollywood Blvd\", \"address2\": \"\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90027\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"5179 Hollywood Blvd\", \"Los Angeles, CA 90027\"]}, \"phone\": \"+13235223652\", \"display_phone\": \"(323) 522-3652\", \"distance\": 4785.859766364802}, {\"id\": \"fxeuGYnoRWwm5aGDg1FRJA\", \"alias\": \"marugame-monzo-los-angeles-5\", \"name\": \"Marugame Monzo\", \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/De7_sqX_0bvCqvuFv2WadQ/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/marugame-monzo-los-angeles-5?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 4610, \"categories\": [{\"alias\": \"japanese\", \"title\": \"Japanese\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}, {\"alias\": \"asianfusion\", \"title\": \"Asian Fusion\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.04994760027338, \"longitude\": -118.24004464723886}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"329 E 1st St\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90012\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"329 E 1st St\", \"Los Angeles, CA 90012\"]}, \"phone\": \"+12133469762\", \"display_phone\": \"(213) 346-9762\", \"distance\": 7604.0045304468695}, {\"id\": \"CpAgDCkj0PASeu7vmSigTw\", \"alias\": \"hironori-craft-ramen-pasadena-3\", \"name\": \"HiroNori Craft Ramen\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/mc064oMmirjbW2vwHzA8AA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/hironori-craft-ramen-pasadena-3?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 818, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"vegan\", \"title\": \"Vegan\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.14289, \"longitude\": -118.1328}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"163 S Lake Ave\", \"address2\": null, \"address3\": \"\", \"city\": \"Pasadena\", \"zip_code\": \"91101\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"163 S Lake Ave\", \"Pasadena, CA 91101\"]}, \"phone\": \"+16264608594\", \"display_phone\": \"(626) 460-8594\", \"distance\": 19594.717917680944}, {\"id\": \"NNayFhITG-6Lfbc4pCLr0A\", \"alias\": \"ramen-tatsunoya-los-angeles-2\", \"name\": \"Ramen Tatsunoya\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/X6M_MR_46jjkJmrJGoWEjA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/ramen-tatsunoya-los-angeles-2?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 340, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.088251, \"longitude\": -118.2764613}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"3440 W Sunset Blvd\", \"address2\": \"Ste A\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90026\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"3440 W Sunset Blvd\", \"Ste A\", \"Los Angeles, CA 90026\"]}, \"phone\": \"+13235226639\", \"display_phone\": \"(323) 522-6639\", \"distance\": 5089.744927243856}, {\"id\": \"Fz0dr16smyDx_fsNX806Lg\", \"alias\": \"ramen-nagi-arcadia\", \"name\": \"Ramen Nagi\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/Fpsld9CYfD1ZDzBgunnndw/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/ramen-nagi-arcadia?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 607, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.13486012905233, \"longitude\": -118.0515997311205}, \"transactions\": [], \"price\": \"$$\", \"location\": {\"address1\": \"400 S Baldwin Ave\", \"address2\": \"Ste D2\", \"address3\": null, \"city\": \"Arcadia\", \"zip_code\": \"91007\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"400 S Baldwin Ave\", \"Ste D2\", \"Arcadia, CA 91007\"]}, \"phone\": \"\", \"display_phone\": \"\", \"distance\": 26142.814806340193}, {\"id\": \"oUfPbdzIC1U3TyfeRR-SSg\", \"alias\": \"saikai-ramen-bar-los-angeles\", \"name\": \"Saikai Ramen Bar\", \"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/T9gAOoOmGXA5dsIbxBen9g/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/saikai-ramen-bar-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 321, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"izakaya\", \"title\": \"Izakaya\"}, {\"alias\": \"tapasmallplates\", \"title\": \"Tapas/Small Plates\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.07493, \"longitude\": -118.3095}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"209 N Western Ave\", \"address2\": \"Unit B\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90004\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"209 N Western Ave\", \"Unit B\", \"Los Angeles, CA 90004\"]}, \"phone\": \"+13233786518\", \"display_phone\": \"(323) 378-6518\", \"distance\": 1845.2827299599771}, {\"id\": \"zbO-Y5Xi2UbtcRaYysGhmQ\", \"alias\": \"la-brea-ramen-and-sushi-los-angeles-5\", \"name\": \"La Brea Ramen and Sushi\", \"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/kOQ4xwWIwzs1t_ZyBeOzWA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/la-brea-ramen-and-sushi-los-angeles-5?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 591, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.06205, \"longitude\": -118.34382}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"5224 Wilshire Blvd\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90036\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"5224 Wilshire Blvd\", \"Los Angeles, CA 90036\"]}, \"phone\": \"+12135370033\", \"display_phone\": \"(213) 537-0033\", \"distance\": 2066.393067928278}, {\"id\": \"jaLRtJ7YGYcCszfiBFxoHQ\", \"alias\": \"silverlake-ramen-los-angeles\", \"name\": \"Silverlake Ramen\", \"image_url\": \"https://s3-media1.fl.yelpcdn.com/bphoto/FY11CM6fAgSbwdg4vhFBRA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/silverlake-ramen-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 3746, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.0835268538041, \"longitude\": -118.273676509195}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"2927 W Sunset Blvd\", \"address2\": \"\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90026\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"2927 W Sunset Blvd\", \"Los Angeles, CA 90026\"]}, \"phone\": \"+13236608100\", \"display_phone\": \"(323) 660-8100\", \"distance\": 5025.532320338132}, {\"id\": \"CWM5nseG5en-lCW5ac-Vfw\", \"alias\": \"afuri-ramen-dumpling-los-angeles-5\", \"name\": \"Afuri Ramen Dumpling\", \"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/8kN6ldQeeJcP3E7uSZ38cQ/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/afuri-ramen-dumpling-los-angeles-5?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 493, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.03517, \"longitude\": -118.23218}, \"transactions\": [\"delivery\", \"restaurant_reservation\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"688 Mateo St\", \"address2\": \"\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90021\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"688 Mateo St\", \"Los Angeles, CA 90021\"]}, \"phone\": \"+12132217206\", \"display_phone\": \"(213) 221-7206\", \"distance\": 8726.282301263325}, {\"id\": \"lYbzuO5xHStf_elUKAudvQ\", \"alias\": \"ramen-melrose-los-angeles\", \"name\": \"Ramen Melrose\", \"image_url\": \"https://s3-media3.fl.yelpcdn.com/bphoto/Lt-JJkpOy_3H1Cl9fF3b3g/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/ramen-melrose-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 327, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}, {\"alias\": \"noodles\", \"title\": \"Noodles\"}, {\"alias\": \"sushi\", \"title\": \"Sushi Bars\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 34.08316, \"longitude\": -118.32627}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"5784 Melrose Ave\", \"address2\": \"\", \"address3\": null, \"city\": \"Los Angeles\", \"zip_code\": \"90038\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"5784 Melrose Ave\", \"Los Angeles, CA 90038\"]}, \"phone\": \"+13236457766\", \"display_phone\": \"(323) 645-7766\", \"distance\": 2446.178405573359}, {\"id\": \"llKP5-ZAG8I-RVbKrSatQQ\", \"alias\": \"chibiscus-asian-cafe-los-angeles-2\", \"name\": \"Chibiscus Asian Cafe\", \"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/hPUulDk9hH8L50tHtGOxBg/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/chibiscus-asian-cafe-los-angeles-2?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 1186, \"categories\": [{\"alias\": \"asianfusion\", \"title\": \"Asian Fusion\"}, {\"alias\": \"ramen\", \"title\": \"Ramen\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.0981492, \"longitude\": -118.3501071}, \"transactions\": [\"delivery\", \"pickup\"], \"price\": \"$$\", \"location\": {\"address1\": \"7361 W Sunset Blvd\", \"address2\": null, \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90046\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"7361 W Sunset Blvd\", \"Los Angeles, CA 90046\"]}, \"phone\": \"+13239779877\", \"display_phone\": \"(323) 977-9877\", \"distance\": 4850.326257883658}, {\"id\": \"DE2RWDkisuTLKvBQhDBmnA\", \"alias\": \"831-tokyo-ramen-los-angeles\", \"name\": \"831 Tokyo Ramen\", \"image_url\": \"https://s3-media4.fl.yelpcdn.com/bphoto/yXZ_C8zBsS-dlWuUVnYiQA/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/831-tokyo-ramen-los-angeles?adjust_creative=ygOuoTIoFpq7PF62JZESLg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=ygOuoTIoFpq7PF62JZESLg\", \"review_count\": 23, \"categories\": [{\"alias\": \"ramen\", \"title\": \"Ramen\"}], \"rating\": 4.5, \"coordinates\": {\"latitude\": 34.094005, \"longitude\": -118.327421}, \"transactions\": [], \"location\": {\"address1\": \"1253 Vine St\", \"address2\": \"Ste 5\", \"address3\": \"\", \"city\": \"Los Angeles\", \"zip_code\": \"90038\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"1253 Vine St\", \"Ste 5\", \"Los Angeles, CA 90038\"]}, \"phone\": \"+13234980051\", \"display_phone\": \"(323) 498-0051\", \"distance\": 3637.3865404372577}], \"total\": 3000, \"region\": {\"center\": {\"longitude\": -118.32138061523438, \"latitude\": 34.0615895441259}}}\r\n";
		            return jsonResponse;
		            
		}
	 
	 
	 
	 
	 public static List<Restaurant> extractRestaurantsFromJson(String jsonResponse) {
	        List<Restaurant> restaurants = new ArrayList<>();

	        // Parse the JSON response
	        JsonParser jsonParser = new JsonParser();
	        JsonObject jsonObject = jsonParser.parse(jsonResponse).getAsJsonObject();

	        // Extract business data
	        JsonArray businesses = jsonObject.getAsJsonArray("businesses");

	        if (businesses == null) {
	            System.out.println("Something went wrong");
	            System.exit(0);
	        }

	        for (int i = 0; i < businesses.size(); i++) {
	        	
	        	
	        	try {
	        		
	        	
	            JsonObject business = businesses.get(i).getAsJsonObject();

	            String name = business.get("name").getAsString();
	            double latitude = business.getAsJsonObject("coordinates").get("latitude").getAsDouble();
	            double longitude = business.getAsJsonObject("coordinates").get("longitude").getAsDouble();
	            String address = business.getAsJsonObject("location").get("address1").getAsString();
	            String cuisine =""; 
	            String price = business.get("price").getAsString();;    // Extract the price if needed
	            String rating = business.get("rating").getAsString();
	            String phone = business.get("display_phone").getAsString();
	            String yelpLink = business.get("url").getAsString();
	            String u_=business.get("image_url").getAsString();

	            Restaurant restaurant = new Restaurant(name, latitude, longitude, address, cuisine, price, rating, phone, yelpLink,u_);
	            restaurants.add(restaurant);
	            
	            
	            JsonArray cat=business.getAsJsonArray("categories");
	            JsonObject firstCategory = cat.get(0).getAsJsonObject();
	            cuisine = firstCategory.get("title").getAsString();
	        	
	        	
	        	
	            
	            System.out.println(name);
	            System.out.println(address);
	            System.out.println(latitude);
	            System.out.println(longitude);
	            System.out.println(cuisine);
	            System.out.println(price);
	            System.out.println(rating);
	            System.out.println(phone);
	            System.out.println(yelpLink);
	            System.out.println(u_);
	            System.out.println();System.out.println();System.out.println();
	        	}
	            
	            
	            
	            catch(NullPointerException e)
	        	{
	        		System.out.println("null/error");
	        		continue;
	        	}
	        }

	        return restaurants;
	    }
	 
	 
	 public static void main(String[]args)
	 {
		 String x= processYelpAPI("pizza","34.02116","-118.287132","best_match");
	 }
		
		
	 
	 
	 

}