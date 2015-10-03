package com.parse.starter;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;

public class RegisterGame extends AppCompatActivity {
    ParseUser user;
   Button join;
    double longitude;
    double latitude;
    ParseGeoPoint point;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_game);
    join = (Button) findViewById(R.id.joinGame);
        ParseUser.logInInBackground("player1", "player1pass", new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                }
            }
        });
     user = ParseUser.getCurrentUser();
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        point = new ParseGeoPoint(latitude, longitude);



    }

  public void join(View view) {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("GameSession");

// Retrieve the object by id
      query.getInBackground("hmYW3cYPrO", new GetCallback<ParseObject>() {
          public void done(ParseObject gameScore, ParseException e) {
              if (e == null) {
                  // Now let's update it with some new data. In this case, only cheatMode and score
                  // will get sent to the Parse Cloud. playerName hasn't changed.
                  boolean fulled = false;
                  int i = 1;
                  while (fulled == false){
                      if (gameScore.getString("player" + i) == null) {
                          gameScore.put("player" + i,"poop" );
                          fulled = true;
                          ParseObject pLocation = new ParseObject("playerLocations");
                          pLocation.put("pLocation", point);
                          pLocation.put("pLat", latitude);
                          pLocation.put("pLong", longitude);
                          pLocation.put("playerID", user.getObjectId());
                          pLocation.put("gameSession",gameScore.getObjectId());
                          pLocation.saveInBackground();
                          if (gameScore.getString("player3") == null) {
                              gameScore.put("full",true);
                          }
                      } else {
                          i++;
                      }
                  }





                  gameScore.saveInBackground();
              }
          }
      });
  }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
