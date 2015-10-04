package com.parse.starter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter  extends ArrayAdapter<String> {
    public ArrayList values;

    public adapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public adapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        values = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.activity_adapter, null);
        }




        TextView timer = (TextView) v.findViewById(R.id.TimeStamp);
        //Button goToTime = (Button) v.findViewById(R.id.goToTime);
        //timer.setText(timer.get(VideoSearchActivity.videoPlayer));

        if (timer != null) {
            timer.setText(values.get(position).toString());

        }

            /*if (goToTime != null) {
                goToTime.setText(p.getText());
            }*/
        return v;
    }
}
