package home_search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class User{
	String name;
	String id;
	String password;
	String email;
	
}


class Restaurant_user{
	String name;
	String address;
	String link;
	String date;
	String time;
}

public class user_details extends HttpServlet {
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 
		 /**given name of user, get all relevant info from sql*/
		 
	 }

}
