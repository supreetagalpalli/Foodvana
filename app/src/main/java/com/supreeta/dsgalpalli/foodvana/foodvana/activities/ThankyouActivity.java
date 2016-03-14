package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.User;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ThankyouActivity extends AppCompatActivity {
    User user;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);

        new GetServiceTask().execute(Utils.getCurrentUserId(this) + "");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("Logout");
        menu.add("Home");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Logout"))
        {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        else if (item.getTitle().equals("Home"))
        {
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class GetServiceTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //http://localhost:9090/foodvana/BasketItemServlet
            String strURL = String.format("%s?userId=%s", Utils.getURL(SERVLET_SERVICE), params[0]);


            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();


                try {
                    String result = Utils.convertInputStream(stream);
                    JSONObject jsonRootObject = new JSONObject(result);

                    //Get the instance of JSONArray that contains JSONObjects

                    // JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("User");

                    //Log.e("Parsing", jsonArray.toString());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        user = new User();
                        user.setEmail(jsonObject.getString("Email"));
                        user.setPhoneno(jsonObject.getString("Phone"));


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  return stream;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    BillActivity activity=new BillActivity();

    public void Sendsms(View v)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(user.getPhoneno(), null, "Your Token number is " + activity.GenerateTokenNumber(Utils.getCurrentUserId(this)), null, null);

        /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("\"Your Token number is 30\"", "default content");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);*/
    }
}
