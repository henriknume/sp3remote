package io.hnnt.sp3remote.fragments;

/**
 * Created by nume on 2016-04-29..
 * ..
 */

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import io.hnnt.sp3remote.GpsLocation;
import io.hnnt.sp3remote.R;
import io.hnnt.sp3remote.events.CommandEvent;
import io.hnnt.sp3remote.events.SettingsEvent;
import io.hnnt.sp3remote.model.Sp3Model;

public class SettingsFragment extends Fragment {

    private static final double POS_CONTROL_VALUE = 9001.;
    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 9001;
    private static final int THREAD_SLEEP_TIMER_LONG = 3000;
    private static final int POST_EVENT_SLEEP_TIME = 1500;

    public static final String TAG = "SettingsFragment.java";

    public Context fcontext;

    protected Boolean setPosButtonBoolean = true;
    protected GpsLocation gpsLocation;
    protected LocationListener locationListener;
    protected Location location;
    protected String provider;
    private double lat = POS_CONTROL_VALUE;
    private double lon = POS_CONTROL_VALUE;

    private ProgressBar syncProgressBar;
    private Button syncButton;
    private Button retrieveSettingsButton;

    private TextView sp3TimeTextView;
    //private TextView sp3DateTextView;
    private TextView sp3LatTextView;
    private TextView sp3LonTextView;
    private TextView phoneLatTextView;
    private TextView phoneLonTextView;
    private Boolean  buttonAvailable = true;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Create");
        fcontext = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        retrieveSettingsButton = (Button)   v.findViewById(R.id.ret_sp3settings_button);
        syncButton             = (Button)   v.findViewById(R.id.sync_button);

        sp3TimeTextView        = (TextView) v.findViewById(R.id.sp3_time_field_textview);
        sp3LatTextView         = (TextView) v.findViewById(R.id.sp3_pos_lat_field_textview);
        sp3LonTextView         = (TextView) v.findViewById(R.id.sp3_pos_lon_field_textview);
        phoneLatTextView       = (TextView) v.findViewById(R.id.phone_pos_lat_field_textview);
        phoneLonTextView       = (TextView) v.findViewById(R.id.phone_pos_lon_field_textview);
        syncProgressBar        = (ProgressBar) v.findViewById(R.id.sync_progressbar);

        createButtonListeners();

        return v;
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
        Log.d(TAG, "onPause");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        upDateView();
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /*
        Log.d(TAG, "zzz onSettingsEvent() type:" + event.responseType + "  data:" + event.responseData);
        Log.d(TAG, "zzz" +event.fullMessage);
        Log.d(TAG,"zzz----");
  */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingsEvent(SettingsEvent event) {

        switch (event.responseType) {
            case SettingsEvent.DATE_VALUE_SET:
                //syncProgressBar.setProgress(33);
                //Toast.makeText(fcontext, "Time: " + event.responseData, Toast.LENGTH_SHORT).show();
                break;
            case SettingsEvent.DATE_VALUE_GET:
                //Toast.makeText(fcontext, "B." + event.responseData, Toast.LENGTH_SHORT).show();
                sp3TimeTextView.setText(event.responseData);
                Sp3Model.setSp3TimeAndDate(event.responseData);
                break;
            case SettingsEvent.LAT_VALUE_SET:
                //Toast.makeText(fcontext, "Lat:" + event.responseData, Toast.LENGTH_SHORT).show();
                break;
            case SettingsEvent.LON_VALUE_SET:
                //Toast.makeText(fcontext, "Lon:" + event.responseData, Toast.LENGTH_SHORT).show();
                break;
            case SettingsEvent.LAT_VALUE_GET:
                /* TODO replace this with a proper sync finished event*/
                //Toast.makeText(fcontext, "E." + event.responseData, Toast.LENGTH_SHORT).show();
                sp3LatTextView.setText(event.responseData);
                Sp3Model.setSp3Lat(event.responseData);
                //Toast.makeText(fcontext, "Sync finished", Toast.LENGTH_SHORT).show();
                break;
            case SettingsEvent.LON_VALUE_GET:
                //Toast.makeText(fcontext, "F." + event.responseData, Toast.LENGTH_SHORT).show();
                sp3LonTextView.setText(event.responseData);
                Sp3Model.setSp3Lon(event.responseData);
                break;
            default:
                //Toast.makeText(fcontext, "PARSE ERROR", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void createButtonListeners() {

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "syncButton pressed");

                Toast.makeText(fcontext, getString(R.string.toast_syncing_device_start), Toast.LENGTH_SHORT).show();
                if (setPosButtonBoolean && buttonAvailable) {
                    buttonAvailable = false;
                    new AsyncLocation().execute("");
                    //Toast.makeText(fcontext, getString(R.string.toast_syncing_device_finish), Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "AsyncLocation is currently running");
                }
            }
        });

        retrieveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "retrieveSettings button pressed");
                if (buttonAvailable) {
                    buttonAvailable = false;
                    new AsyncRetrieveSp3Settings().execute("");
                }else {
                    Log.d(TAG, "AsyncRetrieveSp3Settings is currently running");
                }
            }
        });
    }



    protected void checkPermission() {
        if (ContextCompat.checkSelfPermission(fcontext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if ((requestCode == REQUEST_CODE_ACCESS_FINE_LOCATION)
                && (grantResults.length > 0)
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            setPosButtonBoolean = true;
        } else {
            showRequestDialog();
        }
    }

/*
        Dialog explaining why ACCESS.FINE.LOCATION is needed by our app
*/
    public void showRequestDialog() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle(getString(R.string.alertdialog_title))
                .setMessage(getString((R.string.alertdialog_message)))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_button_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
                        gpsLocation.getLocationManager(provider, fcontext, location, locationListener);
                        setPosButtonBoolean = true;
                    }
                })
                .setNegativeButton(getString(R.string.deny_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPosButtonBoolean = false;
                    }
                })
                .show();
    }
/*
        Getting date and time in UTC format from the device.
 */
    /*
    public void String[] getDateAndTime() {
        String[] dateTime = new String[2];

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date = new Date();

        for (int i = 0; i < 2; i++) {
            dateTime = myFormat.format(date).split("\\s");
        }
        dateTextView.setText(dateTime[0]);
        timeTextView.setText(dateTime[1]);
    }
    */


    /*
        Getting date and time in UTC format from the device.
    */
    private String getFormattedPhoneDateAndTime() {
        String result;
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        Date date = new Date();
        result = myFormat.format(date);
        return result;
    }

    private void upDateView(){
        sp3TimeTextView.setText(Sp3Model.getSp3TimeAndDate());
        sp3LatTextView.setText(Sp3Model.getSp3Lat());
        sp3LonTextView.setText(Sp3Model.getSp3Lon());

        if ((lat != POS_CONTROL_VALUE) && (lon != POS_CONTROL_VALUE)) {
            Log.d(TAG, "textview is now current pos");
            phoneLatTextView.setText("" + lat);
            phoneLonTextView.setText("" + lon);
        }else{
            Log.d(TAG, "textview is now empty");
            phoneLatTextView.setText("");
            phoneLonTextView.setText("");
        }
    }

    public class AsyncRetrieveSp3Settings extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            if(Looper.myLooper() == null){
                Looper.prepare();
            }

            EventBus.getDefault().post(new CommandEvent("date",
                    CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                    CommandEvent.TARGET_SETTINGS_FRAGMENT));

            try {Thread.sleep(POST_EVENT_SLEEP_TIME);} catch (InterruptedException e) {e.printStackTrace();}

            EventBus.getDefault().post(new CommandEvent("lat",
                    CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                    CommandEvent.TARGET_SETTINGS_FRAGMENT));

            try {Thread.sleep(POST_EVENT_SLEEP_TIME);} catch (InterruptedException e) {e.printStackTrace();}

            EventBus.getDefault().post(new CommandEvent("lon",
                    CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                    CommandEvent.TARGET_SETTINGS_FRAGMENT));

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            buttonAvailable = true;
            Log.d(TAG, "retrieveSettings_onPostExec");
        }
    }


    public class AsyncLocation extends AsyncTask{

        @Override
        protected void onPreExecute(){
            gpsLocation = new GpsLocation();
            locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
        }

        @Override
        protected Object doInBackground(Object[] params) {
            if(Looper.myLooper() == null){
                Looper.prepare();
            }
            checkPermission();
            gpsLocation.getLocationManager(provider,fcontext,location,locationListener);
            location = gpsLocation.getLastKnownLocation();
            try {
                Thread.sleep(THREAD_SLEEP_TIMER_LONG);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            if(gpsLocation != null) {
                gpsLocation.stopListener(fcontext);
                gpsLocation = null;
                locationListener = null;
                Log.d(TAG, "gpsLocation dead");
            }
            return null;
        }

/*
        After position has been fetched, check and set fields to the current pos.
        If called from sync also send commandEvents.
*/
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if ((lat == POS_CONTROL_VALUE) || (lon == POS_CONTROL_VALUE)) {
                Toast.makeText(fcontext, getString(R.string.toast_position_not_valid), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Position NOT updated");
            } else {
                Log.d(TAG, "Position updated");
                phoneLatTextView.setText("" + lat);
                phoneLonTextView.setText("" + lon);
            }
            new AsyncSyncSp3().execute();
        }
    }
    public class AsyncSyncSp3 extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            try {Thread.sleep(POST_EVENT_SLEEP_TIME);} catch (InterruptedException e) {e.printStackTrace();}
            if(lat != POS_CONTROL_VALUE){
                EventBus.getDefault().post(new CommandEvent("lat " + lat,
                        CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                        CommandEvent.TARGET_SETTINGS_FRAGMENT));
            }
            try {Thread.sleep(POST_EVENT_SLEEP_TIME);} catch (InterruptedException e) {e.printStackTrace();}
            if(lon != POS_CONTROL_VALUE){
                EventBus.getDefault().post(new CommandEvent("lon " + lon,
                        CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                        CommandEvent.TARGET_SETTINGS_FRAGMENT));
            }
            try {Thread.sleep(POST_EVENT_SLEEP_TIME);} catch (InterruptedException e) {e.printStackTrace();}
            EventBus.getDefault()
                    .post(new CommandEvent("date " + getFormattedPhoneDateAndTime(),
                            CommandEvent.RESPONSE_TYPE_SETTINGSEVENT,
                            CommandEvent.TARGET_SETTINGS_FRAGMENT));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG, "Sync complete");
            buttonAvailable = true;
        }


    }
}