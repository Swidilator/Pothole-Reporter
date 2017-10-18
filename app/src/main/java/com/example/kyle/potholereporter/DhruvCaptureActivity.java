package com.example.kyle.potholereporter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class DhruvCaptureActivity extends AppCompatActivity {

    public static final int REQUEST_CAPTURE = 1;
    private ImageView resultPhoto;
    private Button click;
    private Button send;
    private String email;
    private String time;
    private TextView displayCoordinates;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        displayCoordinates = (TextView) findViewById(R.id.coordinates_textView);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.metropolitan_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                displayCoordinates.setText("Longitude: "+location.getLongitude()+" Latitude: "+ location.getLatitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET

                },10);
                return;
            }
        }else{
            //configureButton();
        }
        locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
        click = (Button)findViewById(R.id.report_button);

        resultPhoto = (ImageView)findViewById(R.id.report_imageView);

        if(!hasCamera()){

            click.setEnabled(false);

        }
    }

    public int processJSON(String json) {
        System.out.println(json);
        if (json.contains("\"success\":2")) {
            return 2;
        } else if (json.contains("\"success\":1")) {
            return 1;
        } else return 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //configureButton();
                }
        }
    }


    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void uploadPicture(View v){
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);
        String name = email + seconds;
        AsyncUpload up = new AsyncUpload(photo,name);
        up.execute("http://lamp.ms.wits.ac.za/~s1037363/uploadImage.php");
        ArrayList<String[]> list = new ArrayList<>();
        list.add(new String[]{"URL", "http://lamp.ms.wits.ac.za/~s1037363/pictures/" + name});
        list.add(new String[]{""});
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {
            @Override
            public void handleResponse(String response) {
                processJSON(response);
            }
        });
        asyncHttpPost.execute("http://lamp.ms.wits.ac.za/~s1116087/createPothole.php", list);
    }

    public void launchCamera(View v){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,REQUEST_CAPTURE);
        locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            photo  = (Bitmap)extras.get("data");
            resultPhoto.setImageBitmap(photo);
            send = (Button)findViewById(R.id.buttonSend);
            assert send !=null;
            send.setEnabled(true);

        }
    }
}
