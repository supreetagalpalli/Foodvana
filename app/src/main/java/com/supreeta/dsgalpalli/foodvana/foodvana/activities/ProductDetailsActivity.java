package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;
import com.supreeta.dsgalpalli.foodvana.foodvana.models.SubProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class ProductDetailsActivity extends AppCompatActivity {

    TextView textName, textPrice, textDescription;
    ImageView imageView;
    SubProduct subProduct;
    int sub_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        subProduct=new SubProduct();
        imageView = (ImageView) findViewById(R.id.imageViewId);
        textName = (TextView) findViewById(R.id.textNameId);
        textPrice = (TextView) findViewById(R.id.textPriceId);
        textDescription = (TextView) findViewById(R.id.textDecId);

        sub_id = getIntent().getIntExtra(KEY_PRODUCT_DETAIL, 0);
        //subProduct.setSubproduct_id(sub_id); //fooditemid

        new GetProductDetails().execute("" + sub_id); // FoodItemServlet
        //new FetchImageTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("Back");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Back"))
        {
            startActivity(new Intent(this,ProductListActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    class GetProductDetails extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductDetailsActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Fetching Product Details");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //http://localhost:9090/foodvana/ProductDetailsServlet?fooditemid=1
            String strURL = String.format("%s?fooditemid=%s", Utils.getURL(SERVLET_PRODUCT_DETAILS), params[0]);
            Log.d("YYYYYYYYYYYY", strURL);

            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();
                String result = Utils.convertInputStream(stream);

                JSONObject jsonObject = new JSONObject(result);
                subProduct.setTitle(jsonObject.getString("Title"));
                subProduct.setPrice(jsonObject.getLong("Price"));
                subProduct.setDescription(jsonObject.getString("Dec"));
                subProduct.setImage(jsonObject.getString("Image"));


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //  return stream;

            return null;
        }


        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            dialog.cancel();

            textName.setText(subProduct.getTitle());
            textPrice.setText(String.format("" + subProduct.getPrice()));
            textDescription.setText(subProduct.getDescription());
            new FetchImageTask().execute();

        }
    }// End of ProductDetailsTask

//FetchImageTask

    class FetchImageTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {
            // String strURL = String.format("%s", Utils.getURL(SERVLET_PRODUCT_LIST),);
            //"http://172.20.10.2:8080/SunbeamServer/images/"
            try {
                URL url = new URL(FETCH_IMAGE_URL + subProduct.getImage());
                Log.d("URLLLLL", "" + url);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);

        }
    }


}

