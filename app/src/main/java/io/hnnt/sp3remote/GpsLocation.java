package io.hnnt.sp3remote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Tobias Nielsen on 2016-05-03.
 */
public class GpsLocation implements LocationListener {

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

    protected double getLatitude(){
        if(location != null)
        return location.getLatitude();
        else
            return (double)9001;
    }


    protected double getLongitude(){
        if(location != null)
            return location.getLongitude();
        else
            return (double)9001;    }

    @Override
    public void onLocationChanged(Location location) {
        location = getLastKnownLocation();
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
}
