package com.example.kyle.potholereporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NormalActivity extends AppCompatActivity {

    private void processResults(String output){
        LinearLayout ll = (LinearLayout) findViewById(R.id.potholesLayout);
        try {
            JSONArray myArray = new JSONArray(output);
            for (int i = 0; i < myArray.length(); i++) {
                RelativeLayout rl = (RelativeLayout) getLayoutInflater().
                        inflate(R.layout.pothole_user, null);
                final JSONObject jo = (JSONObject) myArray.get(i);
                final String id = jo.getString("P_NUM");
                String text = jo.getString("P_DATE");
                String p_long = jo.getString("P_LONG");
                String p_lat = jo.getString("P_LAT");
                final String URL = jo.getString("P_URL");


                TextView t;
                t = (TextView) rl.findViewById(R.id.textNumber);
                t.setText(id);
                t = (TextView) rl.findViewById(R.id.textDate);
                t.setText(text);
                t = (TextView) rl.findViewById(R.id.textCoord);
                final String coords = p_lat + "," + p_long;
                t.setText(coords);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),WebContentActivityUser.class);
                        intent.putExtra("URL",URL);
                        intent.putExtra("coords",coords);
                        startActivity(intent);
                    }
                });
                ll.addView(rl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String email;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        number = intent.getExtras().getString("number");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(email);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{"number", number});
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {
            @Override
            public void handleResponse(String response) {
                processResults(response);
            }
        });
        asyncHttpPost.execute("http://lamp.ms.wits.ac.za/~s1116087/myPotholes.php", list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(null);
            }
        });

    }

    public void takePicture(View v) {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(getApplicationContext(), DhruvCaptureActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);


    }
}
