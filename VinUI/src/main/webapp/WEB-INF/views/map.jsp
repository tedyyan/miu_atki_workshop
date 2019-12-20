 <head>
    <title>Geocoding service</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
    </style>
  </head>
  <body>
    <div id="map"></div>
    <script>
      var geocoder;
      var map;
      var address = "chicago";
      function getCookie(cname) {
    	  var name = cname + "=";
    	  var decodedCookie = decodeURIComponent(document.cookie);
    	  var ca = decodedCookie.split(';');
    	  for(var i = 0; i <ca.length; i++) {
    	    var c = ca[i];
    	    while (c.charAt(0) == ' ') {
    	      c = c.substring(1);
    	    }
    	    if (c.indexOf(name) == 0) {
    	      return c.substring(name.length, c.length);
    	    }
    	  }
    	  return "";
    	}
      
      
      function initMap() {
    	address =  getCookie("cityname");
        var map = new google.maps.Map(document.getElementById('map'), {
          zoom: 8,
          center: {lat: -34.397, lng: 150.644}
        });
        geocoder = new google.maps.Geocoder();
        codeAddress(geocoder, map);
      }

      function codeAddress(geocoder, map) {
        geocoder.geocode({'address': address}, function(results, status) {
          if (status === 'OK') {
            map.setCenter(results[0].geometry.location);
            var marker = new google.maps.Marker({
              map: map,
              position: results[0].geometry.location
            });
          } else {
            alert('Geocode was not successful for the following reason: ' + status);
          }
        });
      }
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBxj8KpyPs9WanPWFgq5ml9X0Jj-x1Xw6A&callback=initMap">
    </script>
  </body>
</html>