package com.supreeta.dsgalpalli.foodvana.foodvana.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener{

    Button callButton;
    public AboutUsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_about_us, null);
        callButton = (Button) layout.findViewById(R.id.callId);
        callButton.setOnClickListener(this);
        return layout;
    }


    @Override
    public void onClick(View v) {
        Intent phoneIntent=new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:91-844-638-6524"));
        startActivity(phoneIntent);
    }
}
