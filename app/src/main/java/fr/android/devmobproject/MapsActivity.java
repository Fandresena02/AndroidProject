package fr.android.devmobproject;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Geocoder;
import android.location.Address;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fr.android.devmobproject.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager lm;
    private Marker previousMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // 1- Request access to the location service
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        // On définit le bouton de déconnexion
        Button logoutButton = findViewById(R.id.retour);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On appelle LoginActivity, pour retourner sur la page de connexion
                Intent intent = new Intent(MapsActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        // 2 - register to receive the location events before the activity becomes visible
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 10, this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 4- unregister from the service when the activity becomes invisible
        lm.removeUpdates(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (previousMarker != null) {
                    previousMarker.remove();
                }
                previousMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                updateLocation(latLng);
            }
        });
    }

    private void updateLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                String addressString = addresses.get(0).getAddressLine(0);
                TextView recetteTextView = findViewById(R.id.location);
                recetteTextView.setText(addressString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // 3- received a new location from the GPS
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        LatLng newPos = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newPos));
    }

}