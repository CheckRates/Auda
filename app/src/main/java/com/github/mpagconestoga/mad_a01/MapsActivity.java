package com.github.mpagconestoga.mad_a01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mpagconestoga.mad_a01.objects.HideKeyBoardUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MapsActivity";

    private Intent returnIntent;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationClient;

    private Marker selectedLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        returnIntent = new Intent();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.task_view_map);
        mapFragment.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        final Button buttonSetLocation = findViewById(R.id.button_set_location);
        buttonSetLocation.setOnClickListener(new SetLocationClickListener());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.setMyLocationEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                selectedLocation = map.addMarker(new MarkerOptions().position(latLng));
                Log.d(TAG, "onMapClick: SELECTED POSITION: " + latLng.latitude + " " + latLng.longitude);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            setCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            if (requestCode == 1) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    setCurrentLocation();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentLocation() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d(TAG, "onSuccess: LAST LOCATION: " + latLng.latitude  + " " + latLng.longitude);

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Log.d(TAG, "onSuccess: MOVED CAMERA TO: " + map.getCameraPosition().target.latitude + " " + map.getCameraPosition().target.longitude);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d(TAG, "onSuccess: LAST LOCATION: " + latLng.latitude  + " " + latLng.longitude);

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // CLASS      : SetLocationClickListener
    // DESCRIPTION: Sets the location
    private class SetLocationClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            if (selectedLocation == null) {
                Toast.makeText(getApplicationContext(), "Please select a location", Toast.LENGTH_SHORT).show();
                return;
            }

            returnIntent.setAction("SET_LOCATION");
            returnIntent.putExtra("lattitude", selectedLocation.getPosition().latitude);
            returnIntent.putExtra("longitude", selectedLocation.getPosition().longitude);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
