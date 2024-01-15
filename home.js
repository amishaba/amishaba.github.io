// Wait for the DOM to be fully loaded
//AIzaSyBfh-FQ_M7imIwMA_qG24lYFalrRM9DXX8



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










function displayRestDetails(restaurant) {
	
	console.log('displaying for' + restaurant);
  const resultsElement = document.querySelector('.results');
  
  
  
  const s_ = document.getElementById("result_");
	s_.innerHTML=restaurant.name;//new

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
  
  
   imageElement.addEventListener("click", () => {
    // Redirect to the Yelp link when the image is clicked
    window.location.href = restaurant.url;
     });
  
  
  /*
  imageElement.style.maxWidth = "250px"; // Apply max width
  imageElement.style.height = "auto"; // Allow image height to adjust
  imageElement.style.borderRadius = "5px"; // Add border radius
  */
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
  
  
  document.body.appendChild(modal);
  
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

    
  
    
    
    
    


