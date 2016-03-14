package com.supreeta.dsgalpalli.foodvana.foodvana.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.activities.ProductListActivity;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;
import com.supreeta.dsgalpalli.foodvana.foodvana.models.Product;

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
/**
 * A simple {@link Fragment} subclass.
 */
public class VegFragment extends Fragment implements AdapterView.OnItemClickListener {
    ArrayList<Product> products = new ArrayList<Product>();
    private ListView listView;
    private ArrayAdapter<Product> adapter;
    String data = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new GetProductListTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_veg, null);
        listView = (ListView) layout.findViewById(R.id.veglistId);
        adapter = new ArrayAdapter<Product>(getActivity(), android.R.layout.simple_list_item_1, products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        //TextView output = (TextView) findViewById(R.id.textView1);

        //new GetProductListTask().execute();
        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ProductListActivity.class);
        intent.putExtra(KEY_PRODUCT, products.get(position));
        // intent.putExtra("", products.get(position).getProId());
        // Product product=new Product();

   /* intent.putExtra("CatId", product.getProId());//veg
    intent.putExtra("ProId", product.getCatId());  //sabji*/
        startActivity(intent);
    }


    class GetProductListTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Please wait");
            dialog.setMessage("Fetching Food Items");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String strURL = String.format("%s?catId=1", Utils.getURL(SERVLET_PRODUCT_LIST));

            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();


                try {
                    String result = Utils.convertInputStream(stream);
                    JSONObject jsonRootObject = new JSONObject(result);

                    //Get the instance of JSONArray that contains JSONObjects

                    //JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("ProductList");

                    Log.e("Parsing", jsonArray.toString());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //int count=1;
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Product product = new Product();
                        product.setProduct(jsonObject.getString("Product"));
                        product.setCatId(jsonObject.getInt("CatId"));
                        product.setProId(jsonObject.getInt("ProId"));
                        products.add(product);


                    }

                    // output.setText(data);
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

