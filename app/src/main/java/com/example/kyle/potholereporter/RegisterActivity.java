package com.example.kyle.potholereporter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirm;
    private Spinner spinner;
    private CheckBox check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText)findViewById(R.id.fieldEmailRegister);
        email.requestFocus();
        password = (EditText)findViewById(R.id.fieldPasswordRegister);
        check = (CheckBox)findViewById(R.id.checkboxRegister);
        confirm = (EditText)findViewById(R.id.fieldConfirm);

        spinner = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.metropolitan_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    private int processJSON(String json) {
        System.out.println(json);
        if (json.contains("\"success\":2")) {
            return 2;
        } else if (json.contains("\"success\":1")) {
            return 1;
        } else return 0;
    }

    public void changeType(View v){
        CheckBox check = (CheckBox)findViewById(R.id.checkboxRegister);
        TextView text = (TextView)findViewById(R.id.textDistrict);

        if(check.isChecked()){
            text.setEnabled(true);
            spinner.setEnabled(true);
        }
        else{
            text.setEnabled(false);
            spinner.setEnabled(false);
        }
    }



    public void register(View v){
        boolean choice = check.isChecked();
        if (email.getText().length() != 0 && password.getText().length() != 0) {



            ArrayList<String[]> list = new ArrayList<>();
            final String user = email.getText().toString();
            System.out.println(user);
            final String pass = password.getText().toString();
            if(confirm.getText().toString().equals(password.getText().toString())) {
                list.add(new String[]{"email", user});
                list.add(new String[]{"password", pass});
                if (choice) {
                    list.add(new String[]{"type", "M"});
                    list.add(new String[]{"district", spinner.getSelectedItem().toString()});
                } else {
                    list.add(new String[]{"type", "N"});
                }
                AsyncHttpPost asyncHttpPost = new AsyncHttpPost(new AsyncHandler() {

                    @Override
                    public void handleResponse(String response) {
                        int success = processJSON(response);

                        if (success == 2) {
                            Intent intent = new Intent();
                            intent.putExtra("email", user);
                            intent.putExtra("password", pass);
                            setResult(RESULT_OK, intent);
                            finish();

                        } else {
                            email.setError("Email address not available");
                        }


                    }

                });
                asyncHttpPost.execute("http://lamp.ms.wits.ac.za/~s1116087/addUser.php", list);
            }else{
                confirm.setError("Does not match password");
            }
        } else if(email.length() == 0){
            email.setError("Email required");
        } else if(password.length() == 0){
            password.setError("Password required");
        }else if(confirm.length() == 0){
            confirm.setError("Confirm password");
        }

    }
}
