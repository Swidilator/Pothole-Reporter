package com.example.kyle.potholereporter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;

    int REQ_CODE = 232;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boolean checked = false;
        email = (EditText) findViewById(R.id.fieldEmailRegister);
        password = (EditText) findViewById(R.id.fieldPasswordRegister);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public void registerClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent,REQ_CODE);
    }

    public int processJSON(String json) {
        System.out.println(json);
        if (json.contains("\"success\":2")) {
            return 2;
        } else if (json.contains("\"success\":1")) {
            return 1;
        } else return 0;
    }

    private void disableLogin(){

        EditText user = (EditText) findViewById(R.id.fieldEmailRegister);
        assert user != null;
        user.setEnabled(false);
        EditText pass = (EditText) findViewById(R.id.fieldPasswordRegister);
        assert pass != null;
        pass.setEnabled(false);
        Button login = (Button) findViewById(R.id.buttonLogin);
        assert login != null;
        login.setEnabled(false);
        Button register = (Button) findViewById(R.id.buttonSignUp);

        register.setEnabled(false);
    }

    private void enableLogin(){
        EditText user = (EditText) findViewById(R.id.fieldEmailRegister);
        user.setEnabled(true);
        EditText pass = (EditText) findViewById(R.id.fieldPasswordRegister);
        pass.setEnabled(true);
        Button login = (Button) findViewById(R.id.buttonLogin);
        login.setEnabled(true);
        Button register = (Button) findViewById(R.id.buttonSignUp);
        register.setEnabled(true);
    }

    public void loginClick(View v) {
        ArrayList<String[]> list = new ArrayList<>();
        String user = email.getText().toString();
        String pass = password.getText().toString();
        if (!user.isEmpty() && !pass.isEmpty()) {
            list.add(new String[]{"email", user});
            list.add(new String[]{"password", pass});

            AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {

                @Override
                public void handleResponse(String response) {

                    int result = processJSON(response);

                    if (result == 2) {
                        String userNumber = response.substring(response.lastIndexOf(":") + 1).replace("\"","").replace("}","").trim();

                        if (response.contains("\"message\":\"M\"")) {
                            String district = response.split(":")[4].replace("\"","").replace("}","").replace(" ",".").trim().replace("."," ");
                            disableLogin();
                            Intent intent = new Intent(LoginActivity.this, MunicipalActivity.class);
                            intent.putExtra("district", district);
                            startActivity(intent);
                        } else if (response.contains("\"message\":\"N\"")) {
                            disableLogin();
                            Intent intent = new Intent(LoginActivity.this, NormalActivity.class);
                            intent.putExtra("email", email.getText().toString());
                            intent.putExtra("number", userNumber);
                            startActivity(intent);

                        }
                    } else {
                        email.setError("Email or pass incorrect");
                    }


                }

            });
            asyncHttpPost.execute("http://lamp.ms.wits.ac.za/~s1116087/login.php", list);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQ_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                email.setText(data.getExtras().getString("email"));
                password.setText(data.getExtras().getString("password"));
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        enableLogin();

    }

}
