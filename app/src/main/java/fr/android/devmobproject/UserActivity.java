package fr.android.devmobproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
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
                Intent intent = new Intent(UserActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


        // On définit le bouton "Camera"
        Button cameraButton = findViewById(R.id.camerabutton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        // On définit le bouton "favoris"
        Button favorisButton = findViewById(R.id.favorisbutton);
        favorisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, FavoriteActivity.class);
                startActivity(intent);
            }
        });

        // On définit le bouton de déconnexion
        Button logoutButton = findViewById(R.id.logoutbutton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
