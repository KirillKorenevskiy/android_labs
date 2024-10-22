package com.example.lab4;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private EditText titleTextView, descriptionTextView, locationTextView, dateFoundTextView, foundByTextView, retrieveLocationTextView;
    private Button retrieveButton;
    private Uri photoUri;
    private ImageView imageView;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
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
        retrieveButton = findViewById(R.id.retrieveButton);
        imageView = findViewById(R.id.photoImageView);



        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleTextView.getText().toString();
                String description = descriptionTextView.getText().toString();
                String location = locationTextView.getText().toString();
                String dateFound = dateFoundTextView.getText().toString();
                String foundBy = foundByTextView.getText().toString();
                String retrieveLocation = retrieveLocationTextView.getText().toString();
                String photo = photoUri.toString();

                LostItem lostItem = new LostItem(title, description, location, dateFound, foundBy, retrieveLocation, photo);
                saveLostItemToJsonFile(lostItem);
            }
        });

        Button galleryButton = findViewById(R.id.choosePhotoButton);
        galleryButton.setOnClickListener(v -> dispatchPickImageIntent());
    }


    private void saveLostItemToJsonFile(LostItem lostItem) {
        Gson gson = new Gson();
        File file = new File(getExternalFilesDir(null), "lost_items_data.json");

        List<LostItem> lostItemList = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Type lostItemListType = new TypeToken<ArrayList<LostItem>>() {
                }.getType();
                lostItemList = gson.fromJson(reader, lostItemListType);
                if (lostItemList == null) {
                    lostItemList = new ArrayList<>();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error reading existing data", Toast.LENGTH_SHORT).show();
            }
        }
        lostItemList.add(lostItem);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String json = gson.toJson(lostItemList);
            writer.write(json);
            Toast.makeText(this, "Lost item data saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Выберите фото"), PICK_IMAGE_REQUEST);
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
                        .into(imageView);
            } else {
                Toast.makeText(this, "Не удалось получить URI изображения", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }
}
