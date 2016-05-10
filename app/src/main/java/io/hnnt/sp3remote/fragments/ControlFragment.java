package io.hnnt.sp3remote.fragments;

/**
 * Created by Tobias Nielsen on 2016-05-05.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.ResponseEvent;

public class ControlFragment extends Fragment{

    public static final String TAG = "ControlFragment.java";

    public Context fcontext;
    private ImageButton upButton, leftButton, rightButton, downButton, stopButton;
    private Button runAutoButton;

    public ControlFragment() {
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
        View v = inflater.inflate(R.layout.fragment_control, container, false);

        upButton      = (ImageButton) v.findViewById(R.id.up_arrow_button);
        leftButton    = (ImageButton) v.findViewById(R.id.left_arrow_button);
        rightButton   = (ImageButton) v.findViewById(R.id.right_arrow_button);
        downButton    = (ImageButton) v.findViewById(R.id.down_arrow_button);
        stopButton    = (ImageButton) v.findViewById(R.id.stop_button);
        runAutoButton = (Button)      v.findViewById(R.id.run_auto_button);

        createButtonListeners();

        return v;
    }
    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        fcontext = getContext();
        super.onResume();
    }

    @Override
    public void onStop(){
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControlEvent(ResponseEvent event){ }

    public void createButtonListeners(){

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "upButton pressed");
                EventBus.getDefault().post(new CommandEvent("run u"));
                Toast.makeText(fcontext,getString(R.string.toast_up_button_text), Toast.LENGTH_SHORT).show();
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "leftButton pressed");
                EventBus.getDefault().post(new CommandEvent("run l"));
                Toast.makeText(fcontext,getString(R.string.toast_left_button_text), Toast.LENGTH_SHORT).show();
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "rightButton pressed");
                EventBus.getDefault().post(new CommandEvent("run r"));
                Toast.makeText(fcontext,getString(R.string.toast_right_button_text), Toast.LENGTH_SHORT).show();
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "downButton pressed");
                EventBus.getDefault().post(new CommandEvent("run d"));
                Toast.makeText(fcontext,getString(R.string.toast_down_button_text), Toast.LENGTH_SHORT).show();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "stopButton pressed");
                EventBus.getDefault().post(new CommandEvent("run stop"));
                Toast.makeText(fcontext,getString(R.string.toast_stop_button_text), Toast.LENGTH_SHORT).show();
            }
        });

        runAutoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "runAutoButton pressed");
                EventBus.getDefault().post(new CommandEvent("run auto"));
                Toast.makeText(fcontext,getString(R.string.toast_run_auto_text), Toast.LENGTH_SHORT).show();
            }
        });
    }
}