package com.supreeta.dsgalpalli.foodvana.foodvana.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.supreeta.dsgalpalli.foodvana.foodvana.R;
import com.supreeta.dsgalpalli.foodvana.foodvana.common.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import static com.supreeta.dsgalpalli.foodvana.foodvana.common.Constants.*;
/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment implements View.OnClickListener {
    EditText editDes;
    String des;

    Button sendButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_feedback, null);
        editDes = (EditText) layout.findViewById(R.id.EditFeedBackId);
        sendButton = (Button) layout.findViewById(R.id.sendId);

        //id= Utils.getCurrentUserId(getActivity());

        sendButton.setOnClickListener(this);
        return layout;
    }

    @Override
    public void onClick(View v) {

        new FeedBackTask().execute(Utils.getCurrentUserId(getActivity()) + "", String.format(editDes.getText() + ""));
    }


    class FeedBackTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {


            String strURL = String.format("%s?userid=%s&des=%s", Utils.getURL(SERVLET_FEEDBACK), params[0], params[1]);
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


            Toast.makeText(getActivity(), "Your FeedBack has been sent.", Toast.LENGTH_SHORT).show();
            editDes.setText(" ");


        }
    }

}


