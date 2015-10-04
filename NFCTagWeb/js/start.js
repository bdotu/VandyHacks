Parse.initialize("0Zxv6joQQVuxT4p6C0GTkc6tN5tF2s8PYZWx3t3v",  "TxZu2pYZHjfkUb7Dm7rhaZy4UJNY90a8RXMl2zCI");

var locationArray = [
  [36.1465626, -86.8033906],
  [36.146555, -86.8034291]
];

var currentDuration = 0;
var theTimer;
var startTimerFunction;
window.onload = function getLocation(){
  var playerLocations = Parse.Object.extend("playerLocations");
  // var location = new playerLocation();
  var initArray = []
  var query = new Parse.Query(playerLocations);
  query.equalTo("gameSession", "hmYW3cYPrO");
  query.find({
    success: function(results) {
      // console.log("Success");
      // console.log(results);
      for (var i = 0; i < results.length; i++) {
        var object = results[i];
        // console.log(object.get("pLat"));
        // console.log(object.get("pLong"));
        initArray.push(object.get("pLat"));
        initArray.push(object.get("pLong"));
      }
      locationArray.push(initArray);

      // Map Items
      var iconURLPrefix = 'http://maps.google.com/mapfiles/ms/icons/';

      var icons = [
        iconURLPrefix + 'red-dot.png',
        iconURLPrefix + 'green-dot.png',
        iconURLPrefix + 'blue-dot.png'
      ]
      var iconsLength = icons.length;

      var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: new google.maps.LatLng(-37.92, 151.25),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false,
        streetViewControl: false,
        panControl: false,
        zoomControlOptions: {
          position: google.maps.ControlPosition.LEFT_BOTTOM
        }
      });

      var infowindow = new google.maps.InfoWindow({
        maxWidth: 160
      });

      var markers = new Array();

      var iconCounter = 0;

      // Add the markers and infowindows to the map
      for (var i = 0; i <= 2; i++) {
        var marker = new google.maps.Marker({
          position: new google.maps.LatLng(locationArray[i][0], locationArray[i][1]),
          map: map,
          icon: icons[iconCounter]
        });

        markers.push(marker);

        google.maps.event.addListener(marker, 'click', (function(marker, i) {
          return function() {
            infowindow.setContent(locations[i][0]);
            infowindow.open(map, marker);
          }
        })(marker, i));

        iconCounter++;
        // We only have a limited number of possible icon colors, so we may have to restart the counter
        if(iconCounter >= iconsLength) {
          iconCounter = 0;
        }
      }

      function autoCenter() {
        //  Create a new viewpoint bound
        var bounds = new google.maps.LatLngBounds();
        //  Go through each...
        for (var i = 0; i < markers.length; i++) {
          bounds.extend(markers[i].position);
        }
        //  Fit these bounds to the map
        map.fitBounds(bounds);
      }
      autoCenter();

      function getNamesandRoles(){
        var users = [];
        var playerIt = [];
        var whosIt = Parse.Object.extend("whosIt");
        var User = Parse.Object.extend("User");
        var name = new User();
        var playId = new whosIt();
        var query = new Parse.Query(whosIt);
        var queryName = new Parse.Query(User);
        var plName = document.getElementById("player1");
        query.equalTo("gameSession", "hmYW3cYPrO");
        query.find({
          success: function(results){
            for (var i = 0; i < results.length; i++){
              var object = results[i];
              queryName.get(object.get("playerId"), {
                success: function(name){
                  users.push(name.get("username").toString());
                // console.log(users);
                  player1.innerHTML = users[0];
                  player2.innerHTML = users[1];
                  player3.innerHTML = users[2];
                },
                error: function(object, error){
                  console.log("Error Here!");
                }
                // console.log(users);
              })
              playerIt.push(object.get("isplayerIt"));
              // console.log(object.get("playerId") + " is " + object.get("isplayerIt"));
            }
            // console.log(playerIt);
          }
        })
      }
      getNamesandRoles();

    },

    error: function(object, error){
      console.log("Not working bro");
    }
  })


  function startTimer(duration, display) {
    currentDuration = duration;
    // console.log("current duration: " + currentDuration);
    var timer = currentDuration, minutes, seconds;
    theTimer = setInterval(function () {
      minutes = parseInt(timer / 60, 10);
      seconds = parseInt(timer % 60, 10);

      minutes = minutes < 10 ? "0" + minutes : minutes;
      seconds = seconds < 10 ? "0" + seconds : seconds;

      display.textContent = minutes + ":" + seconds;

      currentDuration = currentDuration - 1;
      // console.log("tick - currentDuration: " + currentDuration);
      if (--timer < 0) {
        // timer = duration;
        display.textContent = "Time Up!"
      }
    }, 1000);
  }
  startTimerFunction = startTimer;
  var fiveMinutes = 60 * 5;
  display = document.querySelector('#timer');
  startTimer(fiveMinutes, display);


}

function increaseTimer() {
  currentDuration = currentDuration + 60;
  clearInterval(theTimer);
  startTimerFunction(currentDuration, document.querySelector('#timer'));
}

function decreaseTimer(){
  currentDuration = currentDuration - 60;
  clearInterval(theTimer);
  startTimerFunction(currentDuration, document.querySelector('#timer'));
}
