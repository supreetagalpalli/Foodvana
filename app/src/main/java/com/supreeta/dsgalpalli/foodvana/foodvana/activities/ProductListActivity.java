package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;
import com.supreeta.dsgalpalli.foodvana.foodvana.models.Product;
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
public class ProductListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{




    ListView listView;
    ArrayList<SubProduct> subProducts=new ArrayList<SubProduct>();
    SubProductAdapter adapter;
    SubProduct subProduct;

    int sub_product_id;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = (ListView) findViewById(R.id.productListViewId);
        adapter=new SubProductAdapter(this);
        //adapter=new ArrayAdapter<SubProduct>(this,android.R.layout.simple_list_item_1,subProducts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

        product=getIntent().getParcelableExtra(KEY_PRODUCT);
        new GetProductDetailsTask().execute("" + product.getCatId(), "" + product.getProId());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("My Basket");
        menu.add("<-Back");
        menu.add("Log Out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("My Basket"))
        {
            sub_product_id=subProduct.getSubproduct_id();
            Intent intent=new Intent(this,BasketItemsActivity.class);
            //intent.putExtra(KEY_REMOVE_BASKET,sub_product_id);
            startActivity(intent);

            finish();
        }
        else if(item.getTitle().equals("Log Out"))
        {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else if (item.getTitle().equals("<-Back"))
        {
            startActivity(new Intent(this,HomeActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Add To Basket");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Add To Basket"));
        {

            sub_product_id=subProduct.getSubproduct_id();

            new AddToBasketTask().execute(""+sub_product_id,Utils.getCurrentUserId(this)+"");
        }
        return super.onContextItemSelected(item);
    }

    class AddToBasketTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductListActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Adding Item To Basket");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String strURL = String.format("%s?fooditemid=%s&userid=%s",Utils.getURL(SERVLET_ADD_TO_BASKET), params[0],params[1]);
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

            Toast.makeText(ProductListActivity.this,"Item Added To Basket Successfully",Toast.LENGTH_SHORT).show();


        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        subProduct=subProducts.get(position);
        //String image_name=subProduct.getImage();
        Intent intent=new Intent(this,ProductDetailsActivity.class);
        intent.putExtra(KEY_PRODUCT_DETAIL, subProducts.get(position).getSubproduct_id());
        /*FetchImageTask task = new FetchImageTask(imageView);
        task.execute(image_name);*/

        startActivity(intent);
        finish();
    }


    //SubProductAdapter
    public class SubProductAdapter extends ArrayAdapter<SubProduct>
    {



        public SubProductAdapter(Context context)
        {
            super(context, android.R.layout.simple_list_item_1);

        }


        @Override
        public int getCount()
        {
            return subProducts.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LinearLayout layout = null;
            if (convertView == null) {
                LayoutInflater inflater = ProductListActivity.this.getLayoutInflater();
                layout = (LinearLayout) inflater.inflate(R.layout.list_item_product_details, null);
            } else {
                layout = (LinearLayout) convertView;
            }
            final ImageView imageView=(ImageView)layout.findViewById(R.id.imageId);

            final SubProduct subProduct = subProducts.get(position);

            class FetchImageTask extends AsyncTask<String, Void, Bitmap> {


                @Override
                protected Bitmap doInBackground(String... strings) {
                    String imageName = strings[0];
                    //"http://172.20.10.2:8080/SunbeamServer/images/"
                    try {
                        URL url = new URL(FETCH_IMAGE_URL+ subProduct.getImage());
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
            FetchImageTask task = new FetchImageTask();



            TextView textTitle = (TextView) layout.findViewById(R.id.TextViewTitleId);
            TextView textPrice = (TextView) layout.findViewById(R.id.TextViewPriceId);
            task.execute(subProduct.getImage());

            textTitle.setText("Title: " + subProduct.getTitle());
            textPrice.setText("Price: " + subProduct.getPrice());

            return layout;


        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }

    //End of FetchImage task

    class GetProductDetailsTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ProductListActivity.this);
            dialog.setTitle("Please wait");
            dialog.setMessage("Fetching Product List");
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            String strURL = String.format("%s?CatId=%s&ProId=%s",Utils.getURL(SERVLET_FOOD_ITEM),params[0],params[1]);
            Log.d("YYYYYYYYYYYY", strURL);

            try {
                URL url = new URL(strURL);
                URLConnection connection = url.openConnection();
                InputStream stream = connection.getInputStream();


                try {
                    String result = Utils.convertInputStream(stream);
                    JSONObject jsonRootObject = new JSONObject(result);

                    //Get the instance of JSONArray that contains JSONObjects

                    // JSONArray jsonArray = new JSONArray(result);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("fooditem");

                    //Log.e("Parsing", jsonArray.toString());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        subProduct = new SubProduct();
                        subProduct.setTitle(jsonObject.getString("foodtitle"));
                        subProduct.setPrice(jsonObject.getLong("price"));
                        subProduct.setSubproduct_id(jsonObject.getInt("fooditemid"));
                        subProduct.setImage(jsonObject.getString("image"));

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
