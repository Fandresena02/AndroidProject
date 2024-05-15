package fr.android.devmobproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FavoriteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        TextView favorisTextView = findViewById(R.id.Favoris);
        favorisTextView.setText(recupererFavoris());


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

        Button supprimerToutButton = findViewById(R.id.supprimerTout);
        supprimerToutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supprimerTousLesFavoris();
            }
        });

    }

    private String recupererFavoris() {
        StringBuilder favoris = new StringBuilder();
        BddSQLite bddSQLite = new BddSQLite(this);
        SQLiteDatabase db = bddSQLite.getReadableDatabase();
        String[] projection = {BddSQLite.COLUMN_VILLE, BddSQLite.COLUMN_ADRESSE};
        Cursor cursor = db.query(BddSQLite.TABLE_CITY, projection, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String ville = cursor.getString(cursor.getColumnIndexOrThrow(BddSQLite.COLUMN_VILLE));
            String adresse = cursor.getString(cursor.getColumnIndexOrThrow(BddSQLite.COLUMN_ADRESSE));
            favoris.append("VILLE: ").append(ville).append(", ADRESSE: ").append(adresse).append("\n");
        }
        cursor.close();
        return favoris.toString();
    }

    private void supprimerTousLesFavoris() {
        BddSQLite bddSQLite = new BddSQLite(this);
        SQLiteDatabase db = bddSQLite.getWritableDatabase();
        db.delete(BddSQLite.TABLE_CITY, null, null);
        db.close();
    }
}
