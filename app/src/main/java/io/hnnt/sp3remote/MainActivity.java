package io.hnnt.sp3remote;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{

    private Context mainContext = this;

    private TextView logTextView;
    private Button infoButton;
    private Button showDateButton;
    private Button loggaButton;
    private Button showLonButton;
    private Button showLatButton;
    private Button autoButton;
    private Button setDateButton;
    private Button upButton;
    private Button leftButton;
    private Button rightButton;
    private Button downButton;
    private Button stopButton;
    private Button clearButton;
    private Button setPosButton;

    private GpsLocation gpsLocation;
    private Location location;
    private LocationListener locationListener;
    private boolean setPosButtonBoolean = true;
    private String provider;
    private Double latitude;
    private Double longitude;

    private UsbService usbService;
    private MyHandler mHandler;



    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            logTextView.append("onServiceConnected()");
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            logTextView.append("onServiceDisconnected()");
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location){

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
        gpsLocation = new GpsLocation();
        createButtonListeners();
    }

    /*
     * Notifications from UsbService will be received here.
     */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    mActivity.get().logTextView.append(data);
                    break;
            }
        }
    }

    /*
     * These methods are used to check that the app has the proper permissions
     * and if not it will request them from the user.
     * Note that this only applies to SDK23 and above.
     */
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 9001);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        if ((requestCode == 9001) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        }else{
            showRequestDialog();
        }
    }

    public void showRequestDialog(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.alertdialog_title))
                .setMessage(getString((R.string.alertdialog_message)))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok_button_text),new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 9001);
                        gpsLocation.getLocationManager(provider, mainContext, location, locationListener);
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
    *   Buttonlisteners
    */

    private void createButtonListeners() {

        logTextView =    (TextView) findViewById(R.id.log_textview);
        infoButton =       (Button) findViewById(R.id.info_button);
        showDateButton =   (Button) findViewById(R.id.show_date_button);
        loggaButton =      (Button) findViewById(R.id.logga_button);
        showLonButton =    (Button) findViewById(R.id.show_lon_button);
        showLatButton =    (Button) findViewById(R.id.show_lat_button);
        autoButton =       (Button) findViewById(R.id.auto_button);
        setDateButton =    (Button) findViewById(R.id.set_date_button);
        upButton =         (Button) findViewById(R.id.up_button);
        leftButton =       (Button) findViewById(R.id.left_button);
        rightButton =      (Button) findViewById(R.id.right_button);
        downButton =       (Button) findViewById(R.id.down_button);
        stopButton =       (Button) findViewById(R.id.stop_button);
        clearButton =      (Button) findViewById(R.id.clear_button);
        setPosButton =     (Button) findViewById(R.id.set_pos_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<infoButton>\n");
                sendCommand("info");
            }
        });

        showDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showDateButton>\n");
                sendCommand("date");
            }
        });

        loggaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<loggaButton>\n");
                sendCommand("logga");
            }
        });

        showLonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showLonButton>\n");
                sendCommand("lon");
            }
        });

        showLatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showLatButton>\n");
                sendCommand("lat");
            }
        });

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<autoButton>\n");
                sendCommand("run auto");
            }
        });

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<setDateButton>\n");
                // format: [date YYYY-MM-DD HH:MM:SS\r]
                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                String time = dateFormatGmt.format(new Date())+"";
                sendCommand("date " + time);
            }
        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<upButton>\n");
                sendCommand("run u");
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<leftButton>\n");
                sendCommand("run l");
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<rightButton>\n");
                sendCommand("run r");
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<downButton>\n");
                sendCommand("run d");
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<stopButton>\n");
                sendCommand("run stop");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.setText("");
            }
        });

        setPosButton.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logTextView.append("<setPosButton>\n");
                checkPermission();
                gpsLocation.getLocationManager(provider, mainContext, location, locationListener);

                if(!setPosButtonBoolean){
                    showRequestDialog();
                }else{
                    latitude = gpsLocation.getLatitude();
                    longitude = gpsLocation.getLongitude();
                    sendCommand("lat " +latitude);
                    logTextView.append("lat: " + latitude + "\n");
                    logTextView.append("lon: " + longitude + "\n");
                    sendCommand("lon " + longitude);
                }
            }
        }));
    }

    private void sendCommand(String cmd){
        String data = cmd + "\r";  //don't forget the '\r'
        if (usbService != null) {
            usbService.write(data.getBytes());
        }else{
            logTextView.append("[usbservice is null, cant send]\n");
        }
    }
}
