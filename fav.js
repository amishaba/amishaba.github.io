
let myusername=getUserNameFromCookie();
console.log('got from cookie'+myusername); 


document.addEventListener("DOMContentLoaded", function() {
    // Your JavaScript code here
    const ans=document.getElementById("result_");
ans.textContent=myusername+ "'s Favorites";
});





//NEW START

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
            displayData_(data);
        })
        .catch(error => {
            // Handle any errors that occurred during the fetch
            console.error('Error:', error);
        });
        
    }

   function displayData_(data) {
  const restaurantList = document.getElementById("resultsBody");
  restaurantList.innerHTML='';
  restaurantList.style.marginTop="-290px";//might need to change
  
  
  const restaurantLt = document.getElementById("restaurantList");
  restaurantLt.innerHTML='';

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

    const ratingElement = document.createElement("p");
   // ratingElement.textContent = `Rating: ${restaurant.rating}`;
    ratingElement.innerHTML = `Rating: ${getStarRating(restaurant.rating)}`;


    const reviewCountElement = document.createElement("p");
    reviewCountElement.textContent = `Review Count: ${restaurant.review_count}`;

    const addressElement = document.createElement("p");
    addressElement.textContent = `Address: ${restaurant.location.display_address.join(', ')}`;

    const phoneElement = document.createElement("p");
    phoneElement.textContent = `Phone: ${restaurant.display_phone}`;

    restaurantDiv.style.borderRadius = "5px"; // Add border radius
    restaurantDiv.style.padding = "10px";
    restaurantDiv.style.display = "flex"; // Add display: flex
    restaurantDiv.style.height = "300px"; // Set the height
    restaurantDiv.style.margin = "60px"; // Add margin
    
    imageElement.style.maxWidth = "200px"; // Apply max width
    imageElement.style.height = "200px"; // Allow image height to adjust
    imageElement.style.borderRadius = "10px"; // Add border radius
    imageElement.style.marginRight = "10px"; // Add margin right

    const r_info = document.createElement("div");
    r_info.className = "r_info"; // Add "r_info" class
    r_info.style.flex = "1"; // Set flex property
    r_info.style.marginTop="20px"; //to align - clear cache 

    // Style the paragraphs within the "r_info" div
    const paragraphs = [nameElement, ratingElement, reviewCountElement, addressElement, phoneElement];
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






function displayRestDetails(restaurant) {
  const resultsElement = document.querySelector('.results');

  if (resultsElement) {
    // Check if the element exists before manipulating it
    resultsElement.style.display = 'none';
  }
  
  /**ADD CLASSNAMES STYLING WILL COME DIRECTLY */

  const modal = document.createElement("div");
  modal.className = "modal"; // You can style this class for the modal appearance

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
  imageElement.style.maxWidth = "250px"; // Apply max width
  imageElement.style.height = "auto"; // Allow image height to adjust
  imageElement.style.borderRadius = "5px"; // Add border radius

  const detailsTextDiv = document.createElement("div");
  detailsTextDiv.style.flex = "1"; // Take remaining space
  detailsTextDiv.style.marginLeft = "10px"; // Add margin left

  const ratingElement = document.createElement("p");
  //ratingElement.textContent = `Rating: ${restaurant.rating}`;
  ratingElement.innerHTML = `Rating: ${getStarRating(restaurant.rating)}`;

  const reviewCountElement = document.createElement("p");
  reviewCountElement.textContent = `Review Count: ${restaurant.review_count}`;

  const addressElement = document.createElement("p");
  addressElement.textContent = `Address: ${restaurant.location.display_address.join(', ')}`;

  const phoneElement = document.createElement("p");
  phoneElement.textContent = `Phone: ${restaurant.display_phone}`;

  // Append details to the detailsTextDiv
  detailsTextDiv.appendChild(ratingElement);
  detailsTextDiv.appendChild(reviewCountElement);
  detailsTextDiv.appendChild(addressElement);
  detailsTextDiv.appendChild(phoneElement);

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
  favButton.textContent = "Add to Favorites";

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
  
  
  
  favButton.addEventListener("click", function () {
	  console.log('adding to fav');
    addToFavorites(restaurant);
  });

  // Add event listener for "Add Reservation" button
  resButton.addEventListener("click", function () {
	   console.log('adding reservation');
    addReservation(restaurant);
  });
}

function addToFavorites(restaurant){
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
    var params = new SearchParams();
    params.append('user',currUser);
    params.append('name', restaurant.name);
    params.append('imageUrl', restaurant.image_url);
    params.append('yelpUrl', restaurant.url);
    params.append('address', restaurant.location.display_address.join(', '));
    
    
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

    
    
//NEW END






document.addEventListener('DOMContentLoaded', function() {
    const dropdownButton = document.getElementById('dropdown-btn');
    const dropdownContainer = document.querySelector('.dropdown');
    const dropdownOptions = document.querySelectorAll('.dropdown-content a');
    const dropdownContent  = document.getElementById('dropdown-content');
      dropdownContent.style.display = "none";

    dropdownButton.addEventListener('click', () => {
        dropdownContainer.classList.toggle('active');
        dropdownContent.style.display = (dropdownContent.style.display === "block") ? "none" : "block";
    });
    
    
    /**link it to the same JAVA SERVLET AS A GET REQUEST NOT A POST ONE YYYAASSSSS */

    // Event listener for each dropdown option
    dropdownOptions.forEach(option => {
        option.addEventListener('click', (e) => {
            e.preventDefault();
            const selectedOption = option.textContent;
            console.log(`Selected option: ${selectedOption}`);
            dropdownContainer.classList.remove('active');
            const dropdownContent  = document.getElementById('dropdown-content');
            dropdownContent.style.display = "none";
            
            fetch_faves(selectedOption);
            /** go and get the corresponding info from the ajax request  */
        });
    });

    document.addEventListener('click', (e) => {
        if (!dropdownContainer.contains(e.target)) {
            dropdownContainer.classList.remove('active');
        }
    });
});



 
 
 function fetch_faves(choice) {
	 console.log("in fetch");
    // Get the base URL of the project root
    var baseUrl = window.location.origin + '/CSCI210_assn4/add_to_fav'; // Replace 'your_project_directory' with the actual directory name
    console.log('baseUrl = '+baseUrl);
    var sendme=myusername;
    console.log(sendme + 'this this this');

    // Construct the URL for the servlet
    var servletName = 'add_to_fav'; 
    var url = new URL(servletName, baseUrl);

    // Create a URLSearchParams object to set query parameters
    var params = new URLSearchParams();
    params.append('choice', choice);
    params.append('user',sendme);
    params.append('whoisthis',myusername);
    params.append('random',myusername);
    console.log('sending from FAV JS FETCH FAVES+ '+ myusername);
    

    // Set the URL object's search member to the query parameters
    url.search = params;

    // Use the fetch API to make a GET request
    
        
        
        fetch(url)
        .then(response => {
            // Check if the response status is 200 (OK)
            if (response.ok) {
                // Parse JSON from the response
                return response.json();
            } else {
                // Handle the case where the request fails with an error
                throw new Error('Request failed with status: ' + response.status);
            }
        })
        .then(data => {
            // Handle the parsed JSON response data here
            console.log('Response body:', data);
            displayData(data);

            // Example: update UI with data
            // updateUI(data);
        })
        .catch(error => {
            // Handle any errors that occurred during the fetch
            console.error('Error:', error.message);
        });
        
    }
    
    




   function displayData(data) {
	   console.log('in display data new fav');
  const restaurantList = document.getElementById("restaurantList");
  restaurantList.style.marginTop="-380px";//was-280
  restaurantList.innerHTML="";
  

  data.forEach(restaurant => {
    const restaurantDiv = document.createElement("div");
    restaurantDiv.className = "restaurant info"; // Add "info" class for outer container div

    const nameElement = document.createElement("h2");
    nameElement.textContent = restaurant.name;

    const imageElement = document.createElement("img");
    imageElement.className = "mypic"; // Add "mypic" class for the image
    imageElement.src = restaurant.image_link;
    imageElement.alt = restaurant.name;
    
    
    imageElement.addEventListener("click",()=>{
		displayRestDetails(restaurant);
	});
    

    const addressElement = document.createElement("p");
    addressElement.textContent = `Address: ${restaurant.address}`;
    
    
    
     
     
     const yelpElement = document.createElement("p");
	const lli = ` ${restaurant.yelp_link}`;
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

















function getStarRating(rating) {
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

    return stars;
}



