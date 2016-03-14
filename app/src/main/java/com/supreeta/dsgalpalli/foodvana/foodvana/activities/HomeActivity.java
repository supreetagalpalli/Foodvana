package com.supreeta.dsgalpalli.foodvana.foodvana.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.AboutUsFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.FAQFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.FeedbackFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.HomeFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.NonVegFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.TermsConditionsFragment;
import com.supreeta.dsgalpalli.foodvana.foodvana.fragments.VegFragment;

import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    DrawerLayout drawerLayout;
    private ListView listView;
    ActionBarDrawerToggle drawerToggle;

    // fragments
    HomeFragment homeFragment;

    String sidebarMenuItems[] = new String[] {
            "Veg", "NonVeg", "Feedback","About us","FAQs","Terms and Conditions", "Logout"
    };
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeFragment=new HomeFragment();

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sidebarMenuItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        // load home at startup
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, homeFragment)
                .commit();
        //getSupportActionBar().setTitle(sidebarMenuItems[0]);
        getSupportActionBar().setTitle("Welcome To Foodvana");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("confirmation");
        builder.setMessage("Are you sure you want to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HomeActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 6) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_LOGIN_STATUS, false);

            editor.putInt(KEY_LOGIN_USERID, -1);
            editor.putString(KEY_LOGIN_FULLNAME, "");
            editor.putString(KEY_LOGIN_USERNAME, "");

            editor.commit();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Fragment fragment = null;
        switch (position) {
            case 0: fragment = new VegFragment(); break;
            case 1: fragment = new NonVegFragment(); break;
            case 2: fragment = new FeedbackFragment(); break;
            case 3: fragment = new AboutUsFragment(); break;
            case 4: fragment = new FAQFragment(); break;
            case 5: fragment = new TermsConditionsFragment(); break;

        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_layout, fragment)
                .commit();
        getSupportActionBar().setTitle(sidebarMenuItems[position]);
        drawerLayout.closeDrawer(listView);
    }
}
