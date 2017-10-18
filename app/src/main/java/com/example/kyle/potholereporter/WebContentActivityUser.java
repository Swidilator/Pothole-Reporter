package com.example.kyle.potholereporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class WebContentActivityUser extends AppCompatActivity {


    String coords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content_user);

        setTitle("View Pothole");

        Intent intent = getIntent();
        String URL = intent.getExtras().getString("URL");
        coords = intent.getExtras().getString("coords");
        WebView web = (WebView)findViewById(R.id.webView);

        assert web != null;
        web.loadUrl(URL);

    }


    public void viewMap(View v){
        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
        intent.putExtra("coords", coords);
        startActivity(intent);
    }

}