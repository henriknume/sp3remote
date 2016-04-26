package io.hnnt.sp3remote;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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

    private UsbService usbService;
    private MyHandler mHandler;

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            logTextView.setText("onServiceConnected()");
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            logTextView.setText("onServiceDisconnected()");
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler = new MyHandler(this);
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

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<infoButton>");
                String data = "info\r";
                if (usbService != null) { // if UsbService was correctly binded, Send data
                    logTextView.append("<usbService != null>");
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService == null>");
                }
            }
        });

        showDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showDateButton>");
                String data = "date\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        loggaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<loggaButton>");
                String data = "logga\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        showLonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showLonButton>");
                String data = "lon\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        showLatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<showLatButton>");
                String data = "lat\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<autoButton>");
                String data = "run auto\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<setDateButton>");
                // format: [date YYYY-MM-DD HH:MM:SS\r]
                SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
                String time = dateFormatGmt.format(new Date())+"";
                String data = "date " + time + "\r";

                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }


        });

        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<upButton>");
                String data = "run u\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<leftButton>");
                String data = "run l\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<rightButton>");
                String data = "run r\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<downButton>");
                String data = "run d\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.append("<stopButton>");
                String data = "run stop\r";
                if (usbService != null) {
                    usbService.write(data.getBytes());
                }
                else{
                    logTextView.append("<usbService:null>");
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logTextView.setText("");
            }
        });
    }
}
