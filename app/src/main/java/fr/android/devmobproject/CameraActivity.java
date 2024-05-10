package fr.android.devmobproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Size;
import android.view.View;
import android.widget.Button;

import fr.android.devmobproject.UserActivity;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Pour revenir à UserActivity
        Button returnButton = findViewById(R.id.retour);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CameraActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}

