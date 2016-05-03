package io.hnnt.sp3remote;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by Tobias Nielsen on 2016-05-03.
 */
public class GpsLocation  {

    private  LocationManager locationManager;
    private Location location;


    protected GpsLocation(){ }

    protected void getLocationManager(String provider, Context context, Location myLocation, LocationListener locationListener){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,true);
        getLocation(provider, context, locationListener);
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        myLocation = locationManager.getLastKnownLocation(provider);
        location = myLocation;
    }

    protected void getLocation(String provider, Context context, LocationListener locationListener){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            locationManager.requestSingleUpdate(provider,locationListener, null );
    }

    protected Location getLastKnownLocation(){
        return location;
    }
}
