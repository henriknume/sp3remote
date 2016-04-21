package io.hnnt.sp3remote;

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
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    /*
    @Bind (R.id.log_textview) TextView      logTextView;
    @Bind (R.id.info_button) Button         infoButton;
    @Bind (R.id.show_date_button) Button    showDateButton;
    @Bind (R.id.logga_button) Button        loggaButton;
    @Bind (R.id.show_lon_button) Button     showLonButton;
    @Bind (R.id.show_lat_button) Button     showLatButton;
    @Bind (R.id.auto_button) Button         autoButton;
    @Bind (R.id.set_date_button) Button     setDateButton;
    @Bind (R.id.up_button) Button           upButton;
    @Bind (R.id.left_button) Button         leftButton;
    @Bind (R.id.right_button) Button        rightButton;
    @Bind (R.id.down_button) Button         downButton;
    @Bind (R.id.stop_button) Button         stopButton;
    @Bind (R.id.clear_button) Button        clearButton;
*/
    private UsbService usbService;
    private MyHandler mHandler;

    private TextView display;

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.bind(this);
        mHandler = new MyHandler(this);

        display = (TextView) findViewById(R.id.log_textview);

        Button infoButton = (Button) findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = "info\r";
                if (usbService != null) { // if UsbService was correctly binded, Send data
                    //display.append(data);
                    usbService.write(data.getBytes());
                }
            }
        });

        Button clearButton = (Button) findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
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
                    mActivity.get().display.append(data);
                    break;
            }
        }
    }

    /*
    *   Buttonlisteners
    */

    /*

    @OnClick(R.id.info_button)
    public void onInfoButton() {
        String command = "info\r";
        if (usbService != null) { // if UsbService was correctly binded, Send command
            logTextView.append("# " + command + "\n");
            usbService.write(command.getBytes());
        }
    }

    @OnClick(R.id.show_date_button)
    public void onShowDateButton() {

    }

    @OnClick(R.id.logga_button)
    public void onLoggaButton() {

    }

    @OnClick(R.id.show_lon_button)
    public void onShowLonButton() {

    }

    @OnClick(R.id.show_lat_button)
    public void onShowLatButton() {

    }

    @OnClick(R.id.auto_button)
    public void onAutoButton() {

    }

    @OnClick(R.id.set_date_button)
    public void onSetDateButton() {

    }

    @OnClick(R.id.up_button)
    public void onUpButton() {

    }

    @OnClick(R.id.left_button)
    public void onLeftButton() {

    }

    @OnClick(R.id.right_button)
    public void onRightButton() {

    }

    @OnClick(R.id.down_button)
    public void onDownButton() {

    }

    @OnClick(R.id.stop_button)
    public void onStopButton() {

    }

    @OnClick(R.id.clear_button)
    public void onClearButton() {
        logTextView.setText("");
    }
    */
}
