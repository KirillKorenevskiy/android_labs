package com.example.cont;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.cont.databinding.ActivityAddEditContactBinding;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddEditContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityAddEditContactBinding binding;
    private ContactViewModel viewModel;
    private byte[] imageBytes;
    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactDatabase database = ContactDatabase.getInstance(this);
        ContactDao contactDao = database.contactDao();
        ContactRepository repository = new ContactRepository(contactDao);
        ContactViewModel.Factory factory = new ContactViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        int contactId = getIntent().getIntExtra("contact_id", -1);
        if (contactId != -1) {
            viewModel.getContactById(contactId).observe(this, contact -> {
                currentContact = contact;
                if (contact != null) {
                    binding.setContact(contact);
                    if (contact.getAvatar() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getAvatar(), 0, contact.getAvatar().length);
                        binding.imageAvatar.setImageBitmap(bitmap);
                    }
                }
            });
        } else {
            currentContact = new Contact("", "", "", null, "");
            binding.setContact(currentContact);
        }

        binding.btnSelectPhoto.setOnClickListener(v -> openGallery());

        binding.btnSave.setOnClickListener(v -> {
            if (currentContact != null) {
                currentContact.setName(binding.editName.getText().toString());
                currentContact.setPosition(binding.editPosition.getText().toString());
                currentContact.setPhoneNumber(binding.editPhone.getText().toString());
                currentContact.setLinks(binding.editLinks.getText().toString());

                if (imageBytes != null) {
                    currentContact.setAvatar(imageBytes);
                }

                // Вставка или обновление контакта
                if (contactId == -1) {
                    viewModel.insert(currentContact);
                    Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.update(currentContact);
                    Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });

        binding.btnCancell.setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            convertImageToBytes(selectedImageUri);
        }
    }

    private void convertImageToBytes(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();

            binding.imageAvatar.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }
}
