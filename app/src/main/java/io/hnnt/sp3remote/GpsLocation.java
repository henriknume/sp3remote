package io.hnnt.sp3remote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

/**
 * Created by Tobias Nielsen on 2016-05-03.
 */
public class GpsLocation implements LocationListener {

    private  LocationManager locationManager;
    private Location location;
    private static final double POS_CONTROL_VALUE = 9001.;

    public GpsLocation(){ }

    public void getLocationManager(String provider, Context context, Location myLocation, LocationListener locationListener){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria,true);
        getLocation(provider, context, locationListener);
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            myLocation = locationManager.getLastKnownLocation(provider);
        }
        location = myLocation;
    }

    protected void getLocation(String provider, Context context, LocationListener locationListener){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

            locationManager.requestSingleUpdate(provider,locationListener, null );
    }

    public Location getLastKnownLocation(){
        return location;
    }

    protected double getLatitude(){
        if(location != null)
        return location.getLatitude();
        else
            return POS_CONTROL_VALUE;
    }


    protected double getLongitude(){
        if(location != null)
            return location.getLongitude();
        else
            return POS_CONTROL_VALUE;    }

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
}
