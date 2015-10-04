package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
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

public class RegisterGame extends AppCompatActivity  implements CreateNdefMessageCallback {
    ParseUser user;
   Button join;
    double longitude;
    NfcAdapter mNfcAdapter;
    double latitude;
    ParseGeoPoint point;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_game);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        // Register callback
        mNfcAdapter.setNdefPushMessageCallback(this, this);
    join = (Button) findViewById(R.id.joinGame);
        //ParseUser.logOut();
        user = ParseUser.getCurrentUser();
        if (user == null) {
            ParseUser.logInInBackground("Mouse", "poop", new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {

                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        point = new ParseGeoPoint(latitude, longitude);

                    } else {
                        // Signup failed. Look at the ParseException to see what happened.
                    }
                }
            });
        }




    }
    @Override
    public void onNewIntent(Intent intent) {
        Intent poop =  new Intent(RegisterGame.this,MapsActivity.class);
        // onResume gets called after this to handle the intent
        setIntent(poop);
    }
    void processIntent(Intent intent) {
        ///tagged = (TextView) findViewById(R.id.tag);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
       // tagged.setText(new String(msg.getRecords()[0].getPayload()));
    }
    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("YES YOU ARE IT!");
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/vnd.com.parse.starter", text.getBytes()),

                        NdefRecord.createApplicationRecord("com.parse.starter")
                });

        return msg;
    }
  public void join(View view) {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("GameSession");

// Retrieve the object by id
      query.getInBackground("hmYW3cYPrO", new GetCallback<ParseObject>() {
          public void done(ParseObject gameScore, ParseException e) {
              if (e == null) {

                  Intent intent = new Intent(RegisterGame.this, joinGame.class); //creats bridge to view
                  startActivity(intent);

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
    @Override
    public void onResume() {
        super.onResume();
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

}
