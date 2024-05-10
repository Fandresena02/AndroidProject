package fr.android.devmobproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);


        // On définit le bouton "Maps"
        Button mapsButton = findViewById(R.id.mapsbutton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On appelle MapActivity
                Log.d("UserActivity", "Attempting to start MapsActivity");
                Intent intent = new Intent(UserActivity.this, MapsActivity.class);
                try {
                    startActivity(intent);
                } catch(Exception e)
                    {
                        Log.d("UserActivity", "ERROR 1");
                        System.out.println(e.getMessage());
                        Log.d("UserActivity", e.getMessage());
                        Log.d("UserActivity", "ERROR 2");
                    }

                Log.d("UserActivity", "Intent to start MapsActivity sent");
            }
        });


        // On définit le bouton "Camera"
        Button cameraButton = findViewById(R.id.camerabutton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On appelle CameraActivity
                Intent intent = new Intent(UserActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        // On définit le bouton de déconnexion
        Button logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On appelle LoginActivity, pour retourner sur la page de connexion
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
