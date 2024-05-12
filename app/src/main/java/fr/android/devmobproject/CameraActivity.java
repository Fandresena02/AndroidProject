package fr.android.devmobproject;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.Objects;

import fr.android.devmobproject.UserActivity;

public class CameraActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    private static final int REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.imageView = (ImageView) this.findViewById(R.id.camera);
        Button photoButton = (Button) this.findViewById(R.id.photoButton);
        Button downloadButton = (Button) this.findViewById(R.id.telechargerButton);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        downloadButton.setOnClickListener(v -> {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            if (bitmap != null && ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                saveImageToGallery(this, bitmap); // Utilisation de la méthode saveImageToGallery
            } else {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        });

        // On définit le bouton de déconnexion
        Button logoutButton = findViewById(R.id.retour);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On appelle LoginActivity, pour retourner sur la page de connexion
                Intent intent = new Intent(CameraActivity.this, UserActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);

            // Enregistrer l'image dans la galerie
            saveImageToGallery(this, photo);
        }
    }

    public static void saveImageToGallery(Context context, Bitmap image) {
        // Récupérer le chemin de sauvegarde
        String savedImagePath = MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                image,
                "title",
                "description"
        );

        // Scanner le fichier pour l'ajouter à la galerie
        if (savedImagePath != null) {
            Uri savedImageURI = Uri.parse(savedImagePath);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(savedImageURI);
            context.sendBroadcast(mediaScanIntent);
        } else {
            Toast.makeText(context, "Impossible de sauvegarder l'image", Toast.LENGTH_SHORT).show();
        }
    }


}

