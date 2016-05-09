package io.hnnt.sp3remote.fragments;

/**
 * Created by Tobias Nielsen on 2016-04-29.
 */

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import io.hnnt.sp3remote.R;

public class LogFragment extends Fragment{

    private Button sendLogButton;
    private View v;
    private TextView logTextView;

    public LogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        logTextView = (TextView) v.findViewById(R.id.log_textview);
        sendLogButton = (Button) v.findViewById(R.id.send_log_button);
        sendLogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_log, container, false);
        return v;
    }

    private void sendEmail(){

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                getString(R.string.email_intent),getString(R.string.email_recepient_default), null));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject_default));
        //the message
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text_default)+"\n\n"+logTextView.getText().toString());

        try{
            startActivity(Intent.createChooser(emailIntent, getString(R.string.email_send_email)));

        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

}