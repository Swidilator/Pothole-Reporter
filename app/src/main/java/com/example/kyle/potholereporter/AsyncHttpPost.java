package com.example.kyle.potholereporter;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class AsyncHttpPost extends AsyncTask {
    AsyncHandler handler;

    public AsyncHttpPost(AsyncHandler handler) {
        this.handler = handler;
    }

    @Override
    protected String doInBackground(Object... params) {
        byte[] result = null;
        String str = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost((String) params[0]);// in this case, params[0]

        try {
            ArrayList<String[]> list;
            if(params.length == 2) {
                list = (ArrayList<String[]>) params[1];

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>(2);
                for (int i = 0; i < list.size(); i++) {
                    nameValuePairs.add(new BasicNameValuePair(list.get(i)[0], list.get(i)[1]));
                }
                post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            }

            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
                result = EntityUtils.toByteArray(response.getEntity());
                str = new String(result, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    @Override
    protected void onPostExecute(Object output) {
// do something with the string returned earlier
        handler.handleResponse((String) output);
    }
}