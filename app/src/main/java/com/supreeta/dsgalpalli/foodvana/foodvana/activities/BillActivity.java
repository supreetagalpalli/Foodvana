package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.User;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;
import com.supreeta.dsgalpalli.foodvana.foodvana.models.SubProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BillActivity extends AppCompatActivity {

    ArrayList<SubProduct> subProducts = new ArrayList<SubProduct>();
    TextView textBill;
    float sum=0;
    User user;



    SubProduct subProduct = new SubProduct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        textBill=(TextView)findViewById(R.id.textViewBillId);
        new GenerateBillTask().execute(Utils.getCurrentUserId(this) + "");

        new GetServiceTask().execute(Utils.getCurrentUserId(this) + "");


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

                    JSONArray jsonArray = jsonRootObject.optJSONArray("User");

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


    class GenerateBillTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BillActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Generating Bill Amount");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //http://localhost:9090/foodvana/BillServlet
            String strURL = String.format("%s?userid=%s", Utils.getURL(SERVLET_BILL),params[0]);


            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();


                try {
                    String result = Utils.convertInputStream(stream);
                    JSONObject jsonRootObject = new JSONObject(result);

                    //Get the instance of JSONArray that contains JSONObjects

                    //JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("bill");

                    //Log.e("Parsing", jsonArray.toString());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        subProduct.setPrice(jsonObject.getLong("price"));
                        sum=sum+subProduct.getPrice();

                        subProducts.add(subProduct);


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


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            dialog.cancel();



            textBill.setText(sum+"");

        }
    }
    public int GenerateTokenNumber(int userId)
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + userId;
        return result;
    }

    public void place(View v)
    {
        String s="Your Token number is :"+GenerateTokenNumber(Utils.getCurrentUserId(this))+"    " +
                "Bill Amount is : "+sum+" Rs " +
                " Thank you For Working with Us.." +
                " Visit Again!! ";



        startActivity(new Intent(this, ThankyouActivity.class));
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, user.getEmail());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bill Payment");
        intent.putExtra(Intent.EXTRA_TEXT,s);
        startActivity(intent);
        finish();
    }

    public void cancel(View v)
    {
        subProducts.clear();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

}

