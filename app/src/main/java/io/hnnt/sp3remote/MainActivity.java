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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Set;

import butterknife.ButterKnife;
import io.hnnt.sp3remote.fragments.ControlFragment;
import io.hnnt.sp3remote.fragments.InfoFragment;
import io.hnnt.sp3remote.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity{

    private Context mainContext = this;

    private GpsLocation gpsLocation;
    private Location location;
    private LocationListener locationListener;
    private boolean setPosButtonBoolean = true;
    private String provider;
    private Double latitude;
    private Double longitude;

    private UsbService usbService;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CommandHandler commandHandler;

    private static final double POS_CONTROL_VALUE = 9001.;
    private static final int REQUEST_CODE_ACCESS_FINE_LOCATION = 9001;
    private static final int THREAD_SLEEP_TIMER = 50;

    /*
    *  Service connection to the UsbService
    */
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.d("USBservice","onServiceConnected()");
            usbService = ((UsbService.UsbBinder) arg1).getService();
            commandHandler.updateService(usbService);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("USBservice","onServiceDisconnected()");
            usbService = null;
            commandHandler.updateService(usbService);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        commandHandler = new CommandHandler(usbService);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
        EventBus.getDefault().register(commandHandler);
    }

    @Override
    public void onPause() {
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
        EventBus.getDefault().unregister(commandHandler);
        commandHandler.clearQueue();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     *  Setting up ViewPager and adding fragments
     */

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoFragment(), "info");
        adapter.addFragment(new SettingsFragment(), "settings");
        adapter.addFragment(new ControlFragment(), "control");
        //adapter.addFragment(new FourFragment(), "log");
        viewPager.setAdapter(adapter);
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

    /*
     * These methods are used to check that the app has the proper permissions
     * and if not it will request them from the user.
     * Note that this only applies to SDK23 and above.
     */
    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){

        if ((requestCode == 9001)
                && (grantResults.length > 0)
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
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
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ACCESS_FINE_LOCATION);
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
                    if(latitude != POS_CONTROL_VALUE){
                        sendCommand("lat " +latitude);
                    }else{
                        Toast.makeText(mainContext, R.string.toast_message_no_valid_loc,Toast.LENGTH_SHORT);
                    }
                    logTextView.append("lat: " + latitude + "\n");
                    try {
                        Thread.sleep(THREAD_SLEEP_TIMER);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logTextView.append("lon: " + longitude + "\n");
                    if(longitude != POS_CONTROL_VALUE){
                        sendCommand("lon " + longitude);
                    }else{
                        Toast.makeText(mainContext, R.string.toast_message_no_valid_loc,Toast.LENGTH_SHORT);
                    }
                }
            }
        }));
    }
*/
}
