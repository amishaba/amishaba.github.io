/**
 * 
 */
var currUser="";
var res_rest;

let myusername=getUserNameFromCookie();
console.log('got from cookie'+myusername); 
currUser=myusername;
console.log(currUser);



 function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}


// Get the 'info' parameter from the URL
var info = getParameterByName('info');

if (info !== null) {
    // Use the received data in "user.js"
    console.log('Received data in user.js: ' + info);
    currUser=info;
    // You can now use the 'info' variable in your JavaScript code in "user.js".
    //send ajax request to user_details to get their reservations, favs
}



document.addEventListener("DOMContentLoaded", function () {
    // Get the form element
    const searchForm = document.querySelector('.search form');

    // Get the red button element
    const redButton = document.querySelector('.red-btn');

    // Add a submit event listener to the form
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent the form from submitting and page refresh

        // Check if the submitted form was triggered by the red button
        if (e.submitter === redButton) {
            // Get the user's input from the form
            const restaurantName = searchForm.querySelector('[name="name"]').value;
            const latitude = searchForm.querySelector('[name="Latitude"]').value;
            const longitude = searchForm.querySelector('[name="Longitude"]').value;
            
            // Get the selected radio button value
            const selectedRadioButton = searchForm.querySelector('input[name="choice"]:checked').value;
            
            // You can now use these variables to make your API request and display the results as needed
            console.log('Restaurant Name:', restaurantName);
            console.log('Latitude:', latitude);
            console.log('Longitude:', longitude);
            console.log('Selected Radio Button:', selectedRadioButton);
            
            // Call the ajaxFetch function with the retrieved values
            ajaxFetch(restaurantName, latitude, longitude, selectedRadioButton);
        }
    });
});




 
 
 
 function ajaxFetch(restaurantName, latitude, longitude, selectedRadioButton) {
	 console.log("in ajax fetch");
    // Get the base URL of the project root
    var baseUrl = window.location.origin + '/CSCI210_assn4/search_api_call'; // Replace 'your_project_directory' with the actual directory name
    console.log('baseUrl = '+baseUrl);

    // Construct the URL for the servlet
    var servletName = 'search_api_call'; 
    var url = new URL(servletName, baseUrl);

    // Create a URLSearchParams object to set query parameters
    var params = new URLSearchParams();
    params.append('name', restaurantName);
    params.append('latitude', latitude);
    params.append('longitude', longitude);
    params.append('choice', selectedRadioButton);

    // Set the URL object's search member to the query parameters
    url.search = params;

    // Use the fetch API to make a GET request
    fetch(url)
        .then(response => {
            // Check if the response status is 200 (OK)
            if (response.status === 200) {
                // Assuming your servlet returns JSON, parse it
                return response.json();
            } else {
                // Handle the case where the request fails with an error
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            // Handle the JSON response data here
            console.log(data); 
           
            //populateRestaurantBoxes(data)
            //displayJSONData(data);
            displayData(data,restaurantName);
        })
        .catch(error => {
            // Handle any errors that occurred during the fetch
            console.error('Error:', error);
        });
        
    }

/*
// Wait for the DOM to be fully loaded
document.addEventListener("DOMContentLoaded", function () {
    // Get the form element
    const searchForm = document.querySelector('.search form');

    // Add a submit event listener to the form
    searchForm.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent the form from submitting and page refresh

        // Get the user's input from the form
        const restaurantName = searchForm.querySelector('[name="name"]').value;
        const latitude = searchForm.querySelector('[name="Latitude"]').value;
        const longitude = searchForm.querySelector('[name="Longitude"]').value;
        
        // Get the selected radio button value
        const selectedRadioButton = searchForm.querySelector('input[name="choice"]:checked').value;
        
        // You can now use these variables to make your API request and display the results as needed
        console.log('Restaurant Name:', restaurantName);
        console.log('Latitude:', latitude);
        console.log('Longitude:', longitude);
        console.log('Selected Radio Button:', selectedRadioButton);                
        ajaxFetch(restaurantName, latitude, longitude, selectedRadioButton);            
    });
});



 
 function ajaxFetch(restaurantName, latitude, longitude, selectedRadioButton) {
	 console.log("in ajax fetch");
    // Get the base URL of the project root
    var baseUrl = window.location.origin + '/CSCI210_assn4/search_api_call'; // Replace 'your_project_directory' with the actual directory name
    console.log('baseUrl = '+baseUrl);

    // Construct the URL for the servlet
    var servletName = 'search_api_call'; 
    var url = new URL(servletName, baseUrl);

    // Create a URLSearchParams object to set query parameters
    var params = new URLSearchParams();
    params.append('name', restaurantName);
    params.append('latitude', latitude);
    params.append('longitude', longitude);
    params.append('choice', selectedRadioButton);

    // Set the URL object's search member to the query parameters
    url.search = params;

    // Use the fetch API to make a GET request
    fetch(url)
        .then(response => {
            // Check if the response status is 200 (OK)
            if (response.status === 200) {
                // Assuming your servlet returns JSON, parse it
                return response.json();
            } else {
                // Handle the case where the request fails with an error
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            // Handle the JSON response data here
            console.log(data); 
           
            //populateRestaurantBoxes(data)
            //displayJSONData(data);
            displayData(data,restaurantName);
        })
        .catch(error => {
            // Handle any errors that occurred during the fetch
            console.error('Error:', error);
        });
        
    }
    
   */ 
    
       
   function displayData(data,r_) {
	   
	   
	const s_ = document.getElementById("result_");
	s_.innerHTML='Results for "' +r_+ '"';
    
    
  const restaurantList = document.getElementById("resultsBody");
  restaurantList.style.marginTop="-280px";//was 220
  restaurantList.innerHTML="";
  

  data.businesses.forEach(restaurant => {
    const restaurantDiv = document.createElement("div");
    restaurantDiv.className = "restaurant info"; // Add "info" class for outer container div

    const nameElement = document.createElement("h2");
    nameElement.textContent = restaurant.name;

    const imageElement = document.createElement("img");
    imageElement.className = "mypic"; // Add "mypic" class for the image
    imageElement.src = restaurant.image_url;
    imageElement.alt = restaurant.name;
    
    
    imageElement.addEventListener("click",()=>{
		displayRestDetails(restaurant);
	});
    

    const addressElement = document.createElement("p");
    addressElement.textContent = `Address: ${restaurant.location.display_address.join(', ')}`;
    
   
    const yelpElement = document.createElement("p");
	const lli = ` ${restaurant.url}`;
	const urlWithoutQueryString = lli.split('?')[0];

	yelpElement.innerHTML = `<a href="${urlWithoutQueryString}" target="_blank" style="text-decoration: none; color: inherit;">${urlWithoutQueryString}</a>`;


    restaurantDiv.style.borderRadius = "5px"; // Add border radius
    restaurantDiv.style.padding = "5px";//was 10
    restaurantDiv.style.backgroundColor = "white";
    restaurantDiv.style.display = "flex"; // Add display: flex
    restaurantDiv.style.height = "300px"; // Set the height
    restaurantDiv.style.margin = "10px"; // Add margin was 60
  
    imageElement.style.maxWidth = "200px"; // Apply max width was 250
    imageElement.style.height = "200px"; // Allow image height to adjust
    imageElement.style.borderRadius = "10px"; // Add border radius
    imageElement.style.marginRight = "10px"; // Add margin right

    const r_info = document.createElement("div");
    r_info.className = "r_info"; // Add "r_info" class
    r_info.style.flex = "1"; // Set flex property
    r_info.style.marginTop="20px"; //to align - clear cache 

    // Style the paragraphs within the "r_info" div
    const paragraphs = [nameElement, addressElement, yelpElement ];
    paragraphs.forEach(paragraph => {
      paragraph.style.margin = "0";
      paragraph.style.fontSize = "16px";
      paragraph.style.marginBottom = "10px";
      r_info.appendChild(paragraph);
    });
    
    
    
     const dottedLine = document.createElement("div");
    dottedLine.className = "dotted-line";
        
    restaurantDiv.appendChild(imageElement);
    restaurantDiv.appendChild(r_info);
    restaurantList.appendChild(restaurantDiv);
    restaurantList.appendChild(dottedLine);

  });
  
   
  
}
    
    
    
    /*
    
     function displayData(data,r_) {
		 
		 
		 const s_ = document.getElementById("result_");
	s_.innerHTML='Results for "' +r_+ '"';
	
	
  const restaurantList = document.getElementById("resultsBody");
  restaurantList.style.marginTop="-550px";
  restaurantList.innerHTML="";
  

  data.businesses.forEach(restaurant => {
    const restaurantDiv = document.createElement("div");
    restaurantDiv.className = "restaurant info"; // Add "info" class for outer container div

    const nameElement = document.createElement("h2");
    nameElement.textContent = restaurant.name;

    const imageElement = document.createElement("img");
    imageElement.className = "mypic"; // Add "mypic" class for the image
    imageElement.src = restaurant.image_url;
    imageElement.alt = restaurant.name;
    
    
    imageElement.addEventListener("click",()=>{
		displayRestDetails(restaurant);
	});
    

    const addressElement = document.createElement("p");
    addressElement.textContent = `Address: ${restaurant.location.display_address.join(', ')}`;
 
   
   const yelpElement = document.createElement("p");
	const lli = ` ${restaurant.url}`;
	const urlWithoutQueryString = lli.split('?')[0];

	yelpElement.innerHTML = `<a href="${urlWithoutQueryString}" target="_blank" style="text-decoration: none; color: inherit;">${urlWithoutQueryString}</a>`;



    restaurantDiv.style.borderRadius = "5px"; // Add border radius
    restaurantDiv.style.padding = "5px";
    restaurantDiv.style.backgroundColor = "white";
    restaurantDiv.style.display = "flex"; // Add display: flex
    restaurantDiv.style.height = "300px"; // Set the height
    restaurantDiv.style.margin = "10px"; // Add margin
  
    imageElement.style.maxWidth = "200px"; // Apply max width was 250
    imageElement.style.height = "200px"; // Allow image height to adjust
    imageElement.style.borderRadius = "10px"; // Add border radius
    imageElement.style.marginRight = "10px"; // Add margin right

    const r_info = document.createElement("div");
    r_info.className = "r_info"; // Add "r_info" class
    r_info.style.flex = "1"; // Set flex property
    r_info.style.marginTop="20px"; //to align - clear cache 

    // Style the paragraphs within the "r_info" div
    const paragraphs = [nameElement, addressElement, yelpElement ];
    paragraphs.forEach(paragraph => {
      paragraph.style.margin = "0";
      paragraph.style.fontSize = "16px";
      paragraph.style.marginBottom = "10px";
      r_info.appendChild(paragraph);
    });
    
    const dottedLine = document.createElement("div");
    dottedLine.className = "dotted-line";

    restaurantDiv.appendChild(imageElement);
    restaurantDiv.appendChild(r_info);
    restaurantList.appendChild(restaurantDiv);
     restaurantList.appendChild(dottedLine);
  });
}

*/
    





function displayRestDetails(restaurant) {
	
	
		console.log('displaying for' + restaurant);
  const resultsElement = document.querySelector('.results');

  if (resultsElement) {
	  console.log('found');
    resultsElement.style.display = 'none';
  }
  /**ADD CLASSNAMES STYLING WILL COME DIRECTLY */

  const modal = document.createElement("div");
  modal.className = "modal"; // You can style this class for the modal appearance
  
  modal.style.marginLeft="30px";
  modal.style.marginTop="-160px";
  

  const detailsDiv = document.createElement("div");
  detailsDiv.className = "restaurant-details"; // You can style this class for the details container

  // Create elements for restaurant details (e.g., name, image, rating, etc.)
  const nameElement = document.createElement("h2");
  nameElement.textContent = restaurant.name;

  const contentDiv = document.createElement("div");
  contentDiv.style.display = "flex"; // Add display: flex
  contentDiv.style.alignItems = "center"; // Align items in the center

  const imageElement = document.createElement("img");
  imageElement.className = "mypic"; // Add "mypic" class for the image
  imageElement.src = restaurant.image_url;
  imageElement.alt = restaurant.name;
  

    imageElement.style.maxWidth = "200px"; // Apply max width was 250
    imageElement.style.height = "200px"; // Allow image height to adjust
    imageElement.style.borderRadius = "10px"; // Add border radius
    imageElement.style.marginRight = "10px"; // Add margin right

  const detailsTextDiv = document.createElement("div");
  detailsTextDiv.style.flex = "1"; // Take remaining space
  detailsTextDiv.style.marginLeft = "10px"; // Add margin left

  const ratingElement = document.createElement("p");
  //change here
  //ratingElement.textContent = `Rating: ${restaurant.rating}`;
   ratingElement.innerHTML = `Rating: ${getStarRating(restaurant.rating)}`;

 

  const addressElement = document.createElement("p");
  addressElement.textContent = `Address: ${restaurant.location.display_address.join(', ')}`;

  const phoneElement = document.createElement("p");
  phoneElement.textContent = `Phone: ${restaurant.display_phone}`;
  
  const cuisineElement = document.createElement("p");
  cuisineElement.textContent = `Cuisine: ${restaurant.categories.map(category => category.title).join(', ')}`;
  
  const priceElement = document.createElement("p");
  priceElement.textContent = `Price: ${restaurant.price}`;
  // Append details to the detailsTextDiv
  
  detailsTextDiv.appendChild(addressElement);
  detailsTextDiv.appendChild(phoneElement);
  detailsTextDiv.appendChild(cuisineElement);
  detailsTextDiv.appendChild(priceElement);
  detailsTextDiv.appendChild(ratingElement);

  // Append the elements to the contentDiv
  contentDiv.appendChild(imageElement);
  contentDiv.appendChild(detailsTextDiv);

  // Append the elements to the detailsDiv
  detailsDiv.appendChild(nameElement);
  detailsDiv.appendChild(contentDiv);

  // Append the detailsDiv to the modal
  modal.appendChild(detailsDiv);
	
	
  
  const buttonsDiv = document.createElement("div");
  buttonsDiv.className = "buttons";
  buttonsDiv.style.display = "flex";
  buttonsDiv.style.flexDirection = "column";

  // Create buttons
  const favButton = document.createElement("button");
  favButton.className = "fav-btn";
  favButton.innerHTML = '<i class="fa-solid fa-star"></i>'+"Add to Favorites";

  const resButton = document.createElement("button");
  resButton.className = "res-btn";
  resButton.textContent = "Add Reservation";

  // Append buttons to the buttons container
  buttonsDiv.appendChild(favButton);
  buttonsDiv.appendChild(resButton);

  // Append the buttons container to the modal
  modal.appendChild(buttonsDiv);

  // Append the modal to the body to display it
  document.body.appendChild(modal);
  
  let addfav=false;
  favButton.addEventListener("click", function () {
	  console.log('adding to fav');
	  var c;
	  if (addfav==false){//false or true here 
		  console.log('add');
		  c='add';
		  favButton.innerHTML='<i class="fa-solid fa-star"></i>'+"Remove from Favorites";
	  }
	  else{
		  console.log('remove');
		  c='remove';
		  favButton.innerHTML='<i class="fa-solid fa-star"></i>'+"Add To Favorites";
	  }
	  addfav=!addfav;
    addToFavorites(restaurant,c);
  });
  
  
  
  // Add event listener for "Add Reservation" button
  resButton.addEventListener("click", function () {
	   console.log('adding reservation');
    addReservation(restaurant);
  });
}

function addToFavorites(restaurant,c){
	console.log('inside fav');
	//NOW WE NEED USER LOL
	console.log('current user = '+currUser);
	//add ajax request to ADD TO this this users favs
	/**add name,address, yelp link */
	
	
	// Extracting information from the restaurant object
  var baseUrl = window.location.origin + '/CSCI210_assn4/add_to_fav'; 
  var servletName='add_to_fav';
  var url=new URL(servletName,baseUrl);
    // Create a URLSearchParams object to set parameters
    var params = new URLSearchParams();
    params.append('user',currUser);
    params.append('name', restaurant.name);
    params.append('imageUrl', restaurant.image_url);
    params.append('yelpUrl', restaurant.url);
    params.append('address', restaurant.location.display_address.join(', '));
    params.append('choice',c);
    
    
    fetch(url, {
        method: 'POST',
        body: params,
    })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            console.log('Restaurant added successfully:', data);
            // Handle success response if needed
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle error if needed
        });	
}



function addReservation(restaurant){
	res_rest=restaurant;
	
	console.log('inside res');
	 // Wait for the DOM to be fully loaded   
    var resForm = document.querySelector(".res-form");
    // Check if the element is found before attempting to change the style
    if (resForm) {
        // Log a message before changing the style
        console.log('changing property');
        // Change the style of the element
        resForm.style.display = 'flex';
            document.body.appendChild(resForm);
    } else {
        // Log an error if the element is not found
        console.error('Element with class "res-form" not found.');
        }
        
        /*
        const dateInput = document.getElementById('date');
        const timeInput = document.getElementById('time');
        console.log(dateInput);
        console.log(timeInput);

    // Get the values entered by the user
    const dateValue = dateInput.value;
    const timeValue = timeInput.value;

    // You can now use dateValue and timeValue as needed
    console.log('Date:', dateValue);
    console.log('Time:', timeValue);
    submitReservation(dateValue,timeValue,currUser,restaurant); */
        	
}



function submitReservation(date,time,curu,rest) {
	rest=res_rest;
	curu=currUser;
	const dateInput = document.getElementById('date');
        const timeInput = document.getElementById('time');
        console.log(dateInput);
        console.log(timeInput);

    // Get the values entered by the user
    const dateValue = dateInput.value;
    const timeValue = timeInput.value;
    //TODO::::::
    console.log(dateValue);
    console.log(timeValue);
    console.log(rest);
    console.log(currUser);
    date=dateValue;time=timeValue;
        
	var baseUrl = window.location.origin + '/CSCI210_assn4/add_res'; // Replace 'your_project_directory' with the actual directory name
    console.log('baseUrl = '+baseUrl);
    // Construct the URL for the servlet
    var servletName = 'add_res'; 
    var url = new URL(servletName, baseUrl);
    // Create a URLSearchParams object to set query parameters
    var params = new URLSearchParams();
    params.append('date',date );
    params.append('time',time);
    params.append('user',currUser);
    params.append('restn',rest.name);
    params.append('resta',rest.location.display_address.join(', '));
    params.append('resti',rest.image_url);
    params.append('resty',rest.url);
    url.search = params;
    
    fetch(url, {
        body: params,
        method: 'POST',
    })
        .then(response => {
            if (response.status === 200) {
                return response;
            } else {
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            console.log('Restaurant added successfully:', data);
            // Handle success response if needed
        })
        .catch(error => {
            console.error('Error:', error);
            // Handle error if needed
        });	
}


function getStarRating(rating) {
	console.log('in get star rating '+rating);
    const starCount = 5;
    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 !== 0;

    let stars = '';

    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fa-solid fa-star"></i>';
    }

    if (halfStar) {
        stars += '<i class="fa-solid fa-star-half"></i>';
    }

    const emptyStars = starCount - fullStars - (halfStar ? 1 : 0);

    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="far fa-star"></i>';
    }
    console.log(stars);

    return stars;
}










  
    
    
    
    





/** 
 * FUNCTIONALITY - 
 * search for restaurants (copy paste from home)
 * go to user res - make a java for this. has a sort by button also 
 * go to user fav - make a java for this. has a sort by button (:|)
 * logout - ??
 * 
 * 
 * ways i can do this - 
 * new html files but link them all to this file only so its easier
 * just refresh the old html files (p sure this is recomended)
*/