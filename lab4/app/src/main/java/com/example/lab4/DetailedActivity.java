package com.example.lab4;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    private EditText titleTextView;
    private EditText descriptionTextView;
    private EditText locationTextView;
    private EditText dateFoundTextView;
    private EditText foundByTextView;
    private EditText retrieveLocationTextView;
    private ImageView photoImageView;

    private int itemPosition;
    private LostItem lostItem;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;


    Uri photoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        dateFoundTextView = findViewById(R.id.dateFoundTextView);
        foundByTextView = findViewById(R.id.foundByTextView);
        retrieveLocationTextView = findViewById(R.id.retrieveLocationTextView);
        photoImageView = findViewById(R.id.photoImageView);

        Intent intent = getIntent();
        if (intent != null) {
            lostItem = (LostItem) intent.getSerializableExtra("lostItem");
            itemPosition = intent.getIntExtra("itemPosition", -1);
            boolean isEditMode = intent.getBooleanExtra("isEditMode", false);
            if (lostItem != null) {
                titleTextView.setText(lostItem.getTitle());
                descriptionTextView.setText(lostItem.getDescription());
                locationTextView.setText(lostItem.getLocation());
                dateFoundTextView.setText(lostItem.getDateFound());
                foundByTextView.setText(lostItem.getFoundBy());
                retrieveLocationTextView.setText(lostItem.getRetrieveLocation());

                loadPhoto();
            }

            if (!isEditMode) {
                titleTextView.setEnabled(false);
                descriptionTextView.setEnabled(false);
                locationTextView.setEnabled(false);
                dateFoundTextView.setEnabled(false);
                foundByTextView.setEnabled(false);
                retrieveLocationTextView.setEnabled(false);

                Button retrieveButton = findViewById(R.id.retrieveButton);
                retrieveButton.setVisibility(View.GONE);
                Button choosePhotoButton = findViewById(R.id.choosePhotoButton);
                choosePhotoButton.setVisibility(View.GONE);
            } else {
                titleTextView.setEnabled(true);
                descriptionTextView.setEnabled(true);
                locationTextView.setEnabled(true);
                dateFoundTextView.setEnabled(true);
                foundByTextView.setEnabled(true);
                retrieveLocationTextView.setEnabled(true);

                Button retrieveButton = findViewById(R.id.retrieveButton);
                retrieveButton.setVisibility(View.VISIBLE);
                Button choosePhotoButton = findViewById(R.id.choosePhotoButton);
                choosePhotoButton.setVisibility(View.VISIBLE);
            }
        }

        Button retrieveButton = findViewById(R.id.retrieveButton);
        retrieveButton.setOnClickListener(v -> {
            if (lostItem != null) {
                new AlertDialog.Builder(this)
                        .setMessage("Вы уверены, что хотите сохранить изменения?")
                        .setPositiveButton("Да", (dialog, which) -> {
                            lostItem.setTitle(titleTextView.getText().toString());
                            lostItem.setDescription(descriptionTextView.getText().toString());
                            lostItem.setLocation(locationTextView.getText().toString());
                            lostItem.setDateFound(dateFoundTextView.getText().toString());
                            lostItem.setFoundBy(foundByTextView.getText().toString());
                            lostItem.setRetrieveLocation(retrieveLocationTextView.getText().toString());
                            lostItem.setPhoto(photoUri.toString());

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updatedItem", lostItem);
                            resultIntent.putExtra("itemPosition", itemPosition);
                            setResult(RESULT_OK, resultIntent);
                            updateJsonFile();
                            finish();
                        })
                        .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                        .show();
            }
        });

        Button choosePhotoButton = findViewById(R.id.choosePhotoButton);
        choosePhotoButton.setOnClickListener(v -> {
            Intent intentt = new Intent();
            intentt.setType("image/*");
            intentt.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intentt, "Выберите фото"), PICK_IMAGE_REQUEST);

        });
    }

    private void loadPhoto() {
        String path = lostItem.getPhoto();
        Uri uri = Uri.parse(path);
        photoUri = uri;
        Glide.with(this)
                .load(uri)
                .into(photoImageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            if (uri != null) {
                photoUri = uri;
                Glide.with(this)
                        .load(uri)
                        .into(photoImageView);
            } else {
                Toast.makeText(this, "Не удалось получить URI изображения", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoImageView.setImageBitmap(imageBitmap);
        }
    }

    private void updateJsonFile() {
        List<LostItem> lostItems = loadLostItemsFromJson();
        if (itemPosition >= 0 && itemPosition < lostItems.size()) {
            lostItems.set(itemPosition, lostItem);
            saveLostItemsToJsonFile(lostItems);
        }
    }

    private List<LostItem> loadLostItemsFromJson() {
        List<LostItem> lostItems = new ArrayList<>();
        Gson gson = new Gson();

        File file = new File(getExternalFilesDir(null), "lost_items_data.json");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Type lostItemListType = new TypeToken<List<LostItem>>() {
                }.getType();
                lostItems = gson.fromJson(reader, lostItemListType);
                Log.d("MainActivity", "Loaded " + lostItems.size() + " items from JSON");
            } catch (IOException e) {
                Log.e("MainActivity", "Error loading JSON data", e);
            }
        } else {
            Log.w("MainActivity", "JSON file does not exist");
        }
        return lostItems;
    }

    private void saveLostItemsToJsonFile(List<LostItem> lostItems) {
        Gson gson = new Gson();
        File file = new File(getExternalFilesDir(null), "lost_items_data.json");
        if (file.exists()) {
            if (!file.delete()) {
                Toast.makeText(this, "Error deleting existing file", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String json = gson.toJson(lostItems);
            writer.write(json);
            Toast.makeText(this, "Lost item data saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }
}