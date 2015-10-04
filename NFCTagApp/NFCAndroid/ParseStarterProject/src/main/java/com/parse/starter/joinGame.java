package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class joinGame extends AppCompatActivity {
    double longitude;
    double latitude;

    public ListView gamesList;
    public ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        gamesList = (ListView) findViewById(R.id.gameList);
        final ArrayList<String> list = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameSession");
        //query.whereEqualTo("gameSession", "pee");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    Log.d("fefefe", "hey" + scoreList.toArray().length);
                    for (int i = 0; i < scoreList.toArray().length; i++) {
                        ParseObject object = scoreList.get(i);
                        list.add(object.getObjectId());
                        Log.d("fefefe", "hey" + object);

                    }
                    adapter adapter = new adapter(joinGame.this,
                            R.layout.activity_join_game, list);
                    gamesList.setAdapter(adapter);

                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });



        gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                String gameId = (String) parent.getItemAtPosition(position);

                Log.d("did it word", gameId);
                join(gameId);

            }

        });
    }
    public void join(final String gameID) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameSession");

// Retrieve the object by id
        query.getInBackground(gameID, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    boolean fulled = false;
                    int i = 1;
                    while (fulled == false) {
                        if (gameScore.getString("player" + i) == null) {
                            gameScore.put("player" + i, "poop");
                            fulled = true;
                            ParseObject pLocation = new ParseObject("playerLocations");


                            pLocation.put("pLat", latitude);
                            pLocation.put("pLong", longitude);
                            //pLocation.put("playerID", user.getObjectId());
                            pLocation.put("gameSession", gameScore.getObjectId());
                            pLocation.saveInBackground();
                            Intent intent =  new Intent(joinGame.this,MapsActivity.class); //creats bridge to view
                            intent.putExtra("gamesession",gameID);
                            startActivity(intent);

                            if (gameScore.getString("player3") == null) {
                                gameScore.put("full", true);
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
        getMenuInflater().inflate(R.menu.menu_join_game, menu);
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
