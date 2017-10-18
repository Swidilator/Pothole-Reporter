package com.example.kyle.potholereporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MunicipalActivity extends AppCompatActivity {

    ArrayList<String[]> list;
    LinearLayout ll;

    private void processResults(String output){
        ll = (LinearLayout) findViewById(R.id.municipalLayout);

        try {
            JSONArray myArray = new JSONArray(output);
            for (int i = 0; i < myArray.length(); i++) {
                RelativeLayout rl = (RelativeLayout) getLayoutInflater().
                        inflate(R.layout.pothole_user, null);
                final JSONObject jo = (JSONObject) myArray.get(i);
                final String p_num = jo.getString("P_NUM");
                String p_date = jo.getString("P_DATE");
                String p_long = jo.getString("P_LONG");
                String p_lat = jo.getString("P_LAT");
                final String URL = jo.getString("P_URL");
                final String coords = p_lat + "," + p_long;

                TextView t;
                t = (TextView) rl.findViewById(R.id.textNumber);
                t.setText(p_num);
                t = (TextView) rl.findViewById(R.id.textDate);
                t.setText(p_date);
                t = (TextView) rl.findViewById(R.id.textCoord);
                t.setText(coords);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),WebContentActivityMunicipal.class);
                        intent.putExtra("URL",URL);
                        intent.putExtra("coords", coords);
                        startActivity(intent);
                    }
                });
                ll.addView(rl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String district;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipal);

        Intent intent = getIntent();
        district = intent.getExtras().getString("district").trim();

        setTitle(district);

        list = new ArrayList<>();
        list.add(new String[]{"district", district});
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {
            @Override
            public void handleResponse(String response) {
                processResults(response);
            }
        });

        String post = "http://lamp.ms.wits.ac.za/~s1116087/findPotholes.php";
        asyncHttpPost.execute(post,list);
    }

    protected void onResume(){
        super.onResume();

        ll.removeAllViewsInLayout();
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {
            @Override
            public void handleResponse(String response) {
                processResults(response);
            }
        });

        String post = "http://lamp.ms.wits.ac.za/~s1116087/findPotholes.php";
        asyncHttpPost.execute(post,list);
    }
}
