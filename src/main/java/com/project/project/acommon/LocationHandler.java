package com.project.project.acommon;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.project.project.Utils;

/**
 * Created by dayanand on 21/03/18.
 */

public class LocationHandler {

    Context mContext;
    private LocationManager locationManager;
    OnLocationChanged locationChangedListener;

    public LocationHandler(Context mContext) {
        this.mContext = mContext;
    }

    public void initLocation(OnLocationChanged listener) {

        this.locationChangedListener = listener;
        final Criteria criteria = new Criteria();
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        final String bestProvider = locationManager.getBestProvider(criteria, true);
        Utils.showToast(mContext, "" + bestProvider);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utils.showToast(mContext, "Permission not granted");
            return;
        }

        try{
            locationManager.requestLocationUpdates(
                    bestProvider,
                    0,
                    0,
                    locationListener);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public interface OnLocationChanged {
        public void onLocationAvailable(Location location);
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(this);
            locationChangedListener.onLocationAvailable(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public void stopLocation() {
        locationManager.removeUpdates(locationListener);
    }
}
