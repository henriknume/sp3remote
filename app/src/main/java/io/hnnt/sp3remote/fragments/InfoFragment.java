package io.hnnt.sp3remote.fragments;

/**
 * Created by nume on 2016-04-29.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;

public class InfoFragment extends Fragment{

    public static final String TAG = "InfoFragment.java";
    Context fcontext;
    Button infoButton;
    Button clearButton;
    TextView infoTextView;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        LinearLayout currentLayout = (LinearLayout) inflater.inflate(R.layout.fragment_info, container, false);
        infoTextView = (TextView) currentLayout.findViewById(R.id.info_textview);
        infoButton = (Button) currentLayout.findViewById(R.id.info_button);
        clearButton = (Button) currentLayout.findViewById(R.id.clear_button);

        createButtonListeners();
        return currentLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        fcontext = getContext();
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoEvent(InfoEvent event){
        Log.d(TAG, "onMessageEvent(), message: " + event.message + "\n");
        infoTextView.append(event.message + "\n");
    }

    private void createButtonListeners(){
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "infobutton pressed");
                EventBus.getDefault().post(new CommandEvent("info"));
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clearbutton pressed");
                infoTextView.setText("cleared\n");
            }
        });
    }
}



/* =================================================================================================

some old trash here


public interface InfoFragmentCallback{
    public void sendInfoCommand();
}

private InfoFragmentCallback callback;
    public void onAttach(Context context){
        callback = (InfoFragmentCallback) context;
        super.onAttach(context);
}



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
