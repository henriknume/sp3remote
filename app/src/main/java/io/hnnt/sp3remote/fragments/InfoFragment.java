package io.hnnt.sp3remote.fragments;

/**
 * Created by nume on 2016-04-29.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.hnnt.sp3remote.R;

public class InfoFragment extends Fragment{

    Context fcontext;
    TextView debugTextView;

/*
    public interface InfoFragmentCallback{
        public void sendInfoCommand();
    }
*/
    /*
    private InfoFragmentCallback callback;
    public void onAttach(Context context){
        callback = (InfoFragmentCallback) context;
        super.onAttach(context);
    }
    */

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout mRelativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_info,
                container, false);

        debugTextView = (TextView) mRelativeLayout.findViewById(R.id.debug_textview);

        Button mButton = (Button) mRelativeLayout.findViewById(R.id.info_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //debugTextView.append("info");
                //callback.sendInfoCommand();
            }
        });
        // after you've done all your manipulation, return your layout to be shown
        return mRelativeLayout;
        //return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onPause() {
        //LocalBroadcastManager.getInstance(fcontext).unregisterReceiver(mMessageReceiver);
        super.onPause();

    }

    @Override
    public void onResume() {
        fcontext = getContext();
        // Register mMessageReceiver to receive messages.
        /*
        LocalBroadcastManager.getInstance(fcontext).registerReceiver(mMessageReceiver, new IntentFilter("fetchedInfoData"));
        */
        super.onResume();
    }
/*
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("INFO", "Onefragment, onReceive()");
            // Extract data included in the Intent
            String infoText = "empty";
            if (intent.getAction().equals("fetchedInfoData")) {
                Bundle extras =  intent.getExtras();
                if(extras != null){
                    infoText = extras.getString("MESSAGE");
                }
            }
            debugTextView.setText(">>"+ infoText + "<<");
        }
    };
    */


}