package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
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
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class BasketItemsActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<SubProduct> subProducts=new ArrayList<SubProduct>();
    ArrayAdapter<SubProduct> adapter;
    SubProduct subProduct;
    int sub_product_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_items);

        listView= (ListView) findViewById(R.id.listViewItemBasketId);
        adapter=new ArrayAdapter<SubProduct>(this,android.R.layout.simple_list_item_1,subProducts);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        //sub_product_id=getIntent().getIntExtra(KEY_REMOVE_BASKET,0);

        new GetBasketItemTask().execute(Utils.getCurrentUserId(this)+"");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("Generate Bill");
        menu.add("<-Back");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Generate Bill"))
        {
            startActivity(new Intent(this,BillActivity.class));
            finish();
        }
        else  if(item.getTitle().equals("<-Back"))
        {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Remove From Basket");

    }



    @Override
    public boolean onContextItemSelected(MenuItem item)
    {

        final AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle().equals("Remove From Basket"))
        {
            SubProduct subProduct=subProducts.get(info.position);
            sub_product_id=subProduct.getSubproduct_id();
            new RemoveFromBasketTask().execute(""+sub_product_id,Utils.getCurrentUserId(this)+"");
            Log.d("YYYYYYYYYY",""+sub_product_id);
            new GetBasketItemTask().execute(Utils.getCurrentUserId(this)+"");

        }
        return super.onContextItemSelected(item);
    }


    class RemoveFromBasketTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BasketItemsActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Removing Item From Basket");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //http://localhost:9090/foodvana/BasketItemServlet
            String strURL = String.format("%s?fooditemid=%s&userid=%s", Utils.getURL(SERVLET_REMOVE_FROM_BASKET),params[0],params[1]);

            Log.d("XXXXXXXXX",""+strURL);

            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();

                String result = Utils.convertInputStream(stream);

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

        }
    }


    class GetBasketItemTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BasketItemsActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Fetching Basket Items");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            //http://localhost:9090/foodvana/BasketItemServlet
            String strURL = String.format("%s?userid=%s", Utils.getURL(SERVLET_BASKET_ITEM),params[0]);


            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();


                try {
                    String result = Utils.convertInputStream(stream);
                    JSONObject jsonRootObject = new JSONObject(result);
                    subProducts.clear();
                    //Get the instance of JSONArray that contains JSONObjects

                    // JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("BasketItem");

                    //Log.e("Parsing", jsonArray.toString());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        SubProduct subProduct = new SubProduct();
                        subProduct.setTitle(jsonObject.getString("FoodTitle"));
                        subProduct.setPrice(jsonObject.getLong("Price"));
                        subProduct.setSubproduct_id(jsonObject.getInt("FoodItemid"));

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

            adapter.notifyDataSetChanged();


        }
    }
}
