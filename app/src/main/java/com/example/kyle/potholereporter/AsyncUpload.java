package com.example.kyle.potholereporter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
public class AsyncUpload extends AsyncTask {

    private Bitmap image;
    private String name;
    //private Context c;

    public AsyncUpload(Bitmap image, String name){
        this.image = image;
        this.name = name;
        //this.c = c;
    }


    @Override
    protected Object doInBackground(Object... params) {
        System.out.println(name);
        //byte representation of outgoing data in this case image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.out.println("Step 1");
        //compress image png format 30% quality into byteArrayOutputStream
        image.compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        System.out.println("Step 2");
        //encoded string of the image base 64
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.NO_WRAP);
        System.out.println("Step 3");
        //allows us to hold a key and a value
        ArrayList<NameValuePair> uploadData = new ArrayList<NameValuePair>();
        System.out.println("Step 4");
        uploadData.add(new BasicNameValuePair("name",name));
        System.out.println("Step 5");
        uploadData.add(new BasicNameValuePair("image",encodedImage));
        System.out.println("Step 6");

        HttpClient client = new DefaultHttpClient();
        System.out.println("Step 7");
        HttpPost post = new HttpPost((String)params[0]);
        System.out.println("Step 8");

        try {
            //data to send
            post.setEntity(new UrlEncodedFormEntity(uploadData));
            System.out.println("Step 9");
            HttpResponse response = client.execute(post);
            System.out.println("Step 10");
            StatusLine statusLine = response.getStatusLine();
            System.out.println(statusLine);

        }catch (Exception e){
            //Toast.makeText(c,"failed",Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        //Toast.makeText(c,"Query placed",Toast.LENGTH_LONG).show();
    }
}