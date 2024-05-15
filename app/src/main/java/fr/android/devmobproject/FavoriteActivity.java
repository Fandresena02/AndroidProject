package fr.android.devmobproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FavoriteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // On définit le bouton "Retour map"
        Button mapsButton = findViewById(R.id.retourMapButton);
        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        // On définit le bouton "Retour map"
        Button userButton = findViewById(R.id.retourUser);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });

    }
}
