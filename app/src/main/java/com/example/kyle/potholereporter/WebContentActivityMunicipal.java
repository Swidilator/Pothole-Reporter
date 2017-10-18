package com.example.kyle.potholereporter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import java.util.ArrayList;

public class WebContentActivityMunicipal extends AppCompatActivity {

    private String coords;
    private String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content_municipal);

        setTitle("View Pothole");

        Intent intent = getIntent();
        URL = intent.getExtras().getString("URL");
        coords = intent.getExtras().getString("coords");
        WebView web = (WebView)findViewById(R.id.webView1);
        assert web != null;
        web.loadUrl(URL);
    }

    public void processJSON(String json) {
        System.out.println(json);
        if (json.contains("\"success\":2")) {
            finish();
        }
    }

    public void viewMap1(View v){
        Intent intent = new Intent(getApplicationContext(),MapActivity.class);
        intent.putExtra("coords", coords);
        startActivity(intent);
    }

    public void resolve(View v){

        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{"URL", URL});
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {
            @Override
            public void handleResponse(String response) {
                processJSON(response);
            }
        });

        String post = "http://lamp.ms.wits.ac.za/~s1116087/resolvePothole.php";
        asyncHttpPost.execute(post,list);
    }

}