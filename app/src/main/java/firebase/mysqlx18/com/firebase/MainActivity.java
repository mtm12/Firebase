package firebase.mysqlx18.com.firebase;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics firebaseAnalytics;
    private Button btn1;
    Bundle params = new Bundle();
    private String zipCode = "";
    Context context;
    private LocationManager locationManager;
    private LocationListener listener;
    double longitude;
    double latitude;
    public List<Address> addresses = new ArrayList<>();
    private TextView textViewData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        btn1 = (Button) findViewById(R.id.btn1);
        textViewData = (TextView)findViewById(R.id.textViewData);
        textViewData.setText("");
        getZipCode();

//        String token = FirebaseInstanceId.getInstance().getToken();
//        Log.d("token1", "Token: " + token);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Zipcode", zipCode);
                textViewData.setText("Longitude: " + longitude + "\nLatitude: " + latitude + "\nZip code: "+ zipCode);
                params.putString(FirebaseAnalytics.Param.VALUE, zipCode);
                firebaseAnalytics.logEvent("Zipcode", params);
            }
        });

    }

    public void getZipCode(){
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Zipcode", "checkSelfPermission");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d("Zipcode", "Build version");
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}
                        , 10);
            }
            Log.d("Zipcode", "No permission");
            return;
        }

        this.context = context;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                Log.d("Zipcode", "Long: " + longitude);
                Log.d("Zipcode", "Lat: " + latitude);


                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    zipCode = addresses.get(0).getPostalCode().toString();
                    Log.d("Zipcode", zipCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //for(iCount=0; iCount < addresses.size(); iCount++){
//            Log.d("coords", addresses.get(iCount).toString());

                //}

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

//        // first check for permissions
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
//                        , 10);
//            }
//            Log.d("Zipcode", "No permission");
//            return;
//        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.

        locationManager.requestLocationUpdates("gps", 60000, 0, listener);
    }
}
