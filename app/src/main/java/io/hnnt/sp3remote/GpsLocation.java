package io.hnnt.sp3remote;

import android.Manifest;
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
 * Created by Habitat on 2016-05-03.
 */
public class GpsLocation extends AsyncTask {

    private  LocationManager locationManager;
    private  Location location;
    private  String provider;

    protected GpsLocation(Context context, Location location){
        getLocationManager(context);
        this.location = location;
    }

    protected void getLocationManager(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria,true);
        getLocation(context);
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        location = locationManager.getLastKnownLocation(provider);
    }

    protected void getLocation(final Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            locationManager.requestSingleUpdate(provider, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    location = locationManager.getLastKnownLocation(provider);
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
        }, null);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

}
