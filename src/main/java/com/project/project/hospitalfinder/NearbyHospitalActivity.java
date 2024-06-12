package com.project.project.hospitalfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.project.project.R;
import com.project.project.Utils;
import com.project.project.acommon.LocationHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NearbyHospitalActivity extends Activity {

    static ArrayList<String> arrayl;
    static ArrayAdapter<String> aa;
    LocationManager locationManager;
    ListView lv;
    MyLocationListener locationListener;

    /**
     * Called when the activity is first created.
     */
    String name = "police";
    private String TAG = "Firestation";

    String searchName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalfinder_search);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        arrayl = new ArrayList<String>();

        Button viewAppointments = (Button) findViewById(R.id.viewappointment);
        lv = (ListView) findViewById(R.id.lv);

        searchName = getIntent().getStringExtra("name");

        searchName = "hospital";

        Utils.showToast(getApplicationContext(), "Fetching nearby hospital");
        new LocationHandler(NearbyHospitalActivity.this).initLocation(new LocationHandler.OnLocationChanged() {
            @Override
            public void onLocationAvailable(final Location location) {

                name = searchName;
                new Thread() {
                    @Override
                    public void run() {
                        post(location.getLatitude() + "", location.getLongitude() + "");
                    }
                }.start();
            }
        });


        viewAppointments.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NearbyHospitalActivity.this, ViewAppointmentsListActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NearbyHospitalActivity.this, SetAppointmentActivity.class);
                intent.putExtra("details", arrayl.get(i).split("\n")[0]);
                startActivity(intent);
            }
        });

    }


    String Name, address;

    List<String> loc = new ArrayList<>();

    public void post(String s, String s2) {
        arrayl.clear();
        String line = "";
        try {
            HttpPost httpPost = new HttpPost(
                    "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                            + s
                            + ","
                            + s2
                            + "&radius=6000&name="
                            + name
                            + "&sensor=false&key=AIzaSyAzjLiffpke-H4Z0UsmA1UzhFHWO3YNiKU");
            HttpParams httpParameters = new BasicHttpParams();
            System.out
                    .println("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                            + s
                            + ","
                            + s2
                            + "&radius=6000&name="
                            + name
                            + "&sensor=false&key=AIzaSyAzjLiffpke-H4Z0UsmA1UzhFHWO3YNiKU");
            int timeoutConnection = 2000;
            HttpConnectionParams.setConnectionTimeout(httpParameters,
                    timeoutConnection);
            int timeoutSocket = 2000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            line = EntityUtils.toString(httpEntity);

            try {
                JSONObject jo1 = new JSONObject(line);
                JSONArray ja = jo1.getJSONArray("results");
                // JSONArray ja1 = new JSONArray(line);
                // JSONObject jo = ja1.getJSONObject(0);
                // JSONArray ja = jo.getJSONArray("results");

                for (int i = 0; i < ja.length(); i++) {

                    String Name = ja.getJSONObject(i).get("name").toString();
                    String address = ja.getJSONObject(i).get("vicinity")
                            .toString();
                    double lat1 = ja.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    double lng1 = ja.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                    loc.add(lat1 + "," + lng1);

                    Log.i(TAG, "lat and lng: " + lat1 + ", " + lng1);
                    arrayl.add(Name + "\n" + address);

                }
            } catch (Exception e) {
                System.out.println("xxxxxxxx1" + line);

            }

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    aa = new ArrayAdapter<String>(NearbyHospitalActivity.this, android.R.layout.simple_list_item_1, arrayl);
                    lv.setAdapter(aa);
                }
            });

            System.out.println("responce" + line);
            Log.v("log", "  responce= " + line);

        } catch (Exception e) {
            System.out.println("xxxxxxxx1" + line);
            System.out.println("the exception is " + e.getMessage());

        }

    }

    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location location) {
            String message = String
                    .format("New Location of your friend \n Longitude: %1$s \n Latitude: %2$s",
                            location.getLongitude(), location.getLatitude());
            Toast.makeText(NearbyHospitalActivity.this, "got loc fix", 6000)
                    .show();
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            locationListener = null;
            new Thread() {

                @Override
                public void run() {
                    post("" + location.getLatitude(),
                            "" + location.getLongitude());
                }
            }.start();

            // smanager.sendTextMessage(incomingno, null,
            // "The locations is "+message, null, null);

        }

        public void onStatusChanged(String s, int i, Bundle b) {

        }

        public void onProviderDisabled(String s) {

        }

        public void onProviderEnable(String s) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

    }
}