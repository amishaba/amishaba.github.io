/**
 * 
 */
 // Wait for the DOM to be fully loaded
 var currentUser="";
 
 
 
 
 
 
 document.addEventListener("DOMContentLoaded", function () {
    // Get the login and signup forms
    const loginForm = document.getElementById("login-form");
    const signupForm = document.querySelector(".signup form");

    // Add event listener for the login form
    loginForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent the default form submission

        // Get the values from the login form
        const username = document.getElementById("login-username").value;
        const password = document.getElementById("login-password").value;

        // Log the user's choice, input, and what to do next
        console.log("User chose: Log In");
        console.log("Input values - Username:", username, "Password:", password);
        console.log("Next step: Implement login functionality");
        ajaxFetch(username,password,"","","","","login");
        
        signupForm.reset();
    });

    // Add event listener for the signup form
    signupForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Prevent the default form submission

        // Get the values from the signup form
        const email = document.getElementById("email").value;
        const signupUsername = document.getElementById("username").value;
        const signupPassword = document.getElementById("password").value;
        const confirm_password = document.getElementById("confirm_password").value;
        if (signupPassword!=confirm_password)
        {
			//TODO
			
		}

        // Log the user's choice, input, and what to do next
        console.log("User chose: Sign Up");
        console.log("Input values - Email:", email, "Username:", signupUsername, "Password:", signupPassword, "Confirm Password:", confirm_password);
        console.log("Next step: Implement signin functionality");
        ajaxFetch("","",email,signupUsername,signupPassword,signupPassword,"signup");
        signupForm.reset();
    });
});





 
 
 function ajaxFetch(lusername,lpword,semail,suser,spword,sconfirm,choice) 
 {
	 
	 console.log("in ajax fetch");
	 
    // Get the base URL of the project root
    var baseUrl = window.location.origin + '/CSCI210_assn4/user_login_signup'; // Replace 'your_project_directory' with the actual directory name
    console.log('baseUrl = '+baseUrl);

    // Construct the URL for the servlet
    var servletName = 'user_login_signup'; 
    var url = new URL(servletName, baseUrl);

    // Create a URLSearchParams object to set query parameters
    var params = new URLSearchParams();
    params.append('l_u', lusername);
    params.append('l_p', lpword);
    params.append('s_e', semail);
    params.append('s_u', suser);
    params.append('s_p', spword);
    params.append('s_c', choice);
    params.append('s_con', sconfirm);

    // Set the URL object's search member to the query parameters
    url.search = params;

    // Use the fetch API to make a GET request
    fetch(url, {
    method: 'GET',
	})
	.then(response => {
 	   if (response.ok) {
 	       return response.text(); // Read the response as text
 	   } else {
  	      throw new Error('Failed to fetch data');
  	  }
		})
	.then(data => {
    // Use the data received from the servlet
    console.log(data); // It will log "Success"
    if (data==="ok!")
    {
		/*i think now change the nav page  */
		
		
		if (choice==='login'){
			currentUser=lusername;
			window.location.href = 'user.html?info='+lusername; 
		}
		else{
			currentUser=suser;
			window.location.href = 'user.html?info='+suser;
		}
		setUserCookie(currentUser);
		
		
		 
	}
    
     /**if the login/signgup is success, send it to the next html
	  * which is user.html/css/js. here, quickly fetch all the relevant details and store in objects 
	  */
		})
		.catch(error => {
    console.error(error);
	});
        
    }
    
    function setUserCookie(uname)
    {
		document.cookie = "username=" + uname + "; path=/";
	}
	
	
function getUserNameFromCookie() {
    const name = "username=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const cookieArray = decodedCookie.split(';');

    for(let i = 0; i < cookieArray.length; i++) {
        let cookie = cookieArray[i].trim();
        if (cookie.indexOf(name) === 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }

    return "";
}
    
    
    




/*
 
 
 
 
 
 document.addEventListener("DOMContentLoaded", function() {
    // Get a reference to the form element
    var loginForm = document.getElementById("login-form");

    // Add a submit event listener to the form
    loginForm.addEventListener("submit", function(event) {
        // Prevent the default form submission
        event.preventDefault();

        // Get the input values
        var username = document.getElementById("login-username").value;
        var password = document.getElementById("login-password").value;

        // Basic validation - check if both fields are not empty
        if (username.trim() === "" || password.trim() === "") {
            alert("Please provide both username and password.");
        } else {
            // Perform any further actions, such as sending the data to a server or processing it
            // For this example, we'll log the values to the console
            console.log("Username: " + username);
            console.log("Password: " + password);
            // You can add code here to submit the form data to a server for authentication
            // or any other actions you want to take after the form is validated.
        }
    });
});

*/

