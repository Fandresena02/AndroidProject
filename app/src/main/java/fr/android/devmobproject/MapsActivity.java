package fr.android.devmobproject;


import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.location.Geocoder;
import android.location.Address;
import android.widget.TextView;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import fr.android.devmobproject.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationManager lm;
    private Marker previousMarker;
    private final int FINE_PERMISSION_CODE = 1;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // 1- Request access to the location service
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        Button addFavorisButton = findViewById(R.id.ajouterFavoris);
        addFavorisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousMarker != null) {
                    LatLng markerPosition = previousMarker.getPosition();
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(markerPosition.latitude, markerPosition.longitude, 1);
                        if (!addresses.isEmpty()) {
                            String ville = addresses.get(0).getLocality();
                            String adresse = addresses.get(0).getAddressLine(0);
                            // On ajoute la ville à la base de données
                            BddSQLite bddSQLite = new BddSQLite(MapsActivity.this);
                            bddSQLite.ajouterFavori(ville, adresse);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



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

        // On définit le bouton de favoris pour voir la liste
        Button favorisButton = findViewById(R.id.favoris);
        favorisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getLastLocation(){
        if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.mapView);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
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
                // La position du marqueur
                String latLngText = String.format(Locale.US, "La %f, Lo %f", latLng.latitude, latLng.longitude);
                TextView recetteTextView = findViewById(R.id.descriptif);
                TextView locationTextView = findViewById(R.id.localisationAdresse);
                locationTextView.setText(latLngText + " | " + addressString);

                // la recette en fonction de la ville
                String cityName = addresses.get(0).getLocality();
                RecetteDAO uneRecette = new RecetteDAO();
                String [] recette = uneRecette.getRecette(cityName);
                recetteTextView.setText(recette[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCurrentLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                String addressString = addresses.get(0).getAddressLine(0);
                String latLngText = String.format(Locale.US, "La %f, Lo %f", latitude, longitude);
                TextView positionAdresseTextView = findViewById(R.id.positionAdresse);
                positionAdresseTextView.setText(latLngText + " | " + addressString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng newPos = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newPos));
        if (previousMarker != null) {
            previousMarker.remove();
        }
        previousMarker = mMap.addMarker(new MarkerOptions().position(newPos));
        updateLocation(newPos);

        updateCurrentLocation(lat, lng);
    }



}