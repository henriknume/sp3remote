package io.hnnt.sp3remote.fragments;

/**
 * Created by nume on 2016-04-29.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.InfoEvent;
import io.hnnt.sp3remote.model.Sp3Model;

public class InfoFragment extends Fragment{

    public static final String TAG = "InfoFragment.java";
    public static final String TAG_EVENTS = "commandhandler";
    Context fcontext;
    Button infoButton;

    TextView softWareReleaseTextView;
    TextView currentPositionTextView;
    TextView calcSunPositionTextView;
    TextView tiltZeroPositionTextView;
    TextView sensorZeroPositionTextView;
    TextView nrOfSunnyDaysTextView;
    TextView lastSunTimeTextView;
    TextView nrOfResetsTextView;
    TextView morningPosDiffTextView;
    TextView sunTodayTextView;

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
        //infoTextView = (TextView) currentLayout.findViewById(R.id.info_textview);
        infoButton = (Button) currentLayout.findViewById(R.id.info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "infobutton pressed");
                EventBus.getDefault().post(new CommandEvent("info", CommandEvent.RESPONSE_TYPE_INFOEVENT, CommandEvent.TARGET_INFO_FRAGMENT));
            }
        });

        softWareReleaseTextView = (TextView) currentLayout.findViewById(R.id.info_field_0_textview);
        currentPositionTextView = (TextView) currentLayout.findViewById(R.id.info_field_1_textview);
        calcSunPositionTextView = (TextView) currentLayout.findViewById(R.id.info_field_2_textview);
        tiltZeroPositionTextView = (TextView) currentLayout.findViewById(R.id.info_field_3_textview);
        sensorZeroPositionTextView = (TextView) currentLayout.findViewById(R.id.info_field_4_textview);
        nrOfSunnyDaysTextView = (TextView) currentLayout.findViewById(R.id.info_field_5_textview);
        lastSunTimeTextView = (TextView) currentLayout.findViewById(R.id.info_field_6_textview);
        nrOfResetsTextView = (TextView) currentLayout.findViewById(R.id.info_field_7_textview);
        morningPosDiffTextView = (TextView) currentLayout.findViewById(R.id.info_field_8_textview);
        sunTodayTextView = (TextView) currentLayout.findViewById(R.id.info_field_9_textview);
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
        Log.d(TAG, "onPause()");
        super.onPause();

    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        fcontext = getContext();
        getInfoSp3Model();
        super.onResume();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInfoEvent(InfoEvent event){
        Sp3Model.setInfoSp3Model(event);

        softWareReleaseTextView.setText(event.getSoftwareRelease());
        currentPositionTextView.setText(event.getCurrentPosition());
        calcSunPositionTextView.setText(event.getCalcSunPosition());
        tiltZeroPositionTextView.setText(event.getTiltZeroPosition());
        sensorZeroPositionTextView.setText(event.getSensorZeroPosition());
        nrOfSunnyDaysTextView.setText(event.getNrOfSunnyDays());
        lastSunTimeTextView.setText(event.getLastSunTime());
        nrOfResetsTextView.setText(event.getNrOfResets());
        morningPosDiffTextView.setText(event.getMorningPosDiff());
        boolean sun = false;
        if(event.getSunToday().equals("1")){
            sun = true;
        }
        sunTodayTextView.setText("" + sun);
    }

    public void getInfoSp3Model(){
        if(Sp3Model.getSoftwareRelease() != null){
            softWareReleaseTextView.setText(Sp3Model.getSoftwareRelease());
            currentPositionTextView.setText(Sp3Model.getCurrentPosition());
            calcSunPositionTextView.setText(Sp3Model.getCalcSunPosition());
            tiltZeroPositionTextView.setText(Sp3Model.getTiltZeroPosition());
            sensorZeroPositionTextView.setText(Sp3Model.getSensorZeroPosition());
            nrOfSunnyDaysTextView.setText(Sp3Model.getNrOfSunnyDays());
            lastSunTimeTextView.setText(Sp3Model.getLastSunTime());
            nrOfResetsTextView.setText(Sp3Model.getNrOfResets());
            morningPosDiffTextView.setText(Sp3Model.getMorningPosDiff());
            sunTodayTextView.setText(Sp3Model.getSunToday());
        }
    }
}