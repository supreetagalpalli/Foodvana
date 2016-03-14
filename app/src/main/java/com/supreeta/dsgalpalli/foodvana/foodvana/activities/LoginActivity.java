package com.supreeta.dsgalpalli.foodvana.foodvana.activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {

    EditText editUserName, editPassword;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        checkBox = (CheckBox) findViewById(R.id.checkboxRememberMe);
    }


    public void loginUser(View v) {
        if (editUserName.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Please enter your user name", Toast.LENGTH_SHORT).show();
        } else if (editPassword.getText().toString().length() == 0) {
            Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {

            String userName = editUserName.getText().toString();
            String password = editPassword.getText().toString();

            LoginTask task = new LoginTask();
            task.execute(userName, password);

        }
    }

    public void registerUser(View v) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Logging in");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String strURL = String.format("%s?user_name=%s&password=%s",
                    Utils.getURL(SERVLET_LOGIN), params[0], params[1]);

            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();
                String result = Utils.convertInputStream(stream);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.cancel();
            // String result = Utils.convertInputStream(inputStream);

            try {
                JSONObject object = new JSONObject(result);
                if (object.getString("status").equals("success")) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (checkBox.isChecked()) {

                        editor.putBoolean(KEY_LOGIN_STATUS, true);


                    } else {
                        editor.putBoolean(KEY_LOGIN_STATUS, false);
                    }

                    editor.putInt(KEY_LOGIN_USERID, object.getInt("userId"));
                    editor.putString(KEY_LOGIN_FULLNAME, object.getString("FullName"));
                    editor.putString(KEY_LOGIN_USERNAME, object.getString("Username"));

                    editor.commit();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                } else if (object.getString("status").equals("failure")) {
                    Toast.makeText(LoginActivity.this, "Invalid user name or password.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
