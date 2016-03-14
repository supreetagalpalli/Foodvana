package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class RegisterActivity extends AppCompatActivity {

    EditText editFullName, editUserName, editPassword, editConfirmPassword,editAddress,editEmailAddress,editPhoneNumber;
    private RadioButton radiobuttonMale;
    private RadioButton radiobuttonFemale;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editFullName = (EditText) findViewById(R.id.editFullName);
        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editEmailAddress = (EditText) findViewById(R.id.editEmailAddress);

        radiobuttonMale = (RadioButton) findViewById(R.id.radiobuttonMale);
        radiobuttonFemale = (RadioButton) findViewById(R.id.radiobuttonFemale);
    }

    public void registerUser(View v) {
        if (editFullName.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        } else if (editPassword.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else if (editConfirmPassword.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please confirm password", Toast.LENGTH_SHORT).show();
        } else if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
        } else if (editAddress.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
        } else if (editPhoneNumber.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (editEmailAddress.getText().toString().length() == 0) {
            Toast.makeText(RegisterActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
        } else if (radiobuttonMale.isChecked()!=true && radiobuttonFemale.isChecked()!=true) {
            Toast.makeText(RegisterActivity.this, "Please select gender", Toast.LENGTH_SHORT).show();
        }else
        {

            String fullName = editFullName.getText().toString();
            String userName = editUserName.getText().toString();
            String password = editPassword.getText().toString();
            String address = editAddress.getText().toString();
            String phoneNumber = editPhoneNumber.getText().toString();
            String emailAddress = editEmailAddress.getText().toString();
            String gender=null;
            if (radiobuttonMale.isChecked() == true) {
                gender="Male";
            } else if (radiobuttonFemale.isChecked() == true){
                gender="Female";
            }

            RegisterTask task = new RegisterTask();
            task.execute(fullName, userName, password,address,phoneNumber,emailAddress,gender);
        }

    }

    public void cancel(View v) {
        finish();
    }

    class RegisterTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Registering a new user");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            // <>/<>?full_name=user1&user_name=user1&password=pass1
            String strUrl = String.format("%s?full_name=%s&user_name=%s&password=%s&address=%s&phone_number=%s&email_address=%s&gender=%s",
                    Utils.getURL(SERVLET_REGISTER), params[0], params[1], params[2],params[3],params[4],params[5],params[6]);
            Log.e("abc", strUrl);
            try {
                URL url = new URL(strUrl);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                String result = Utils.convertInputStream(stream);
                if (result.equals("0")) {
                    finish();
                } else if (result.equals("-1")) {
                    Toast.makeText(RegisterActivity.this, "User already exits. Please use another user name.", Toast.LENGTH_SHORT).show();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            super.onPostExecute(v);
            dialog.cancel();

        }
    }

}


