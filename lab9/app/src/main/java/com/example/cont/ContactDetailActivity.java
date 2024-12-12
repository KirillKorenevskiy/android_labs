package com.example.cont;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.cont.databinding.ActivityContactDetailBinding;

public class ContactDetailActivity extends AppCompatActivity {

    private ActivityContactDetailBinding binding;
    private ContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactDatabase database = ContactDatabase.getInstance(this);
        ContactDao contactDao = database.contactDao();
        ContactRepository repository = new ContactRepository(contactDao);
        ContactViewModel.Factory factory = new ContactViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        int contactId = getIntent().getIntExtra("contact_id", -1);

        if (contactId != -1) {
            viewModel.getContactById(contactId).observe(this, contact -> {
                if (contact != null) {
                    binding.textViewName.setText(contact.getName());
                    binding.textViewPosition.setText(contact.getPosition());
                    binding.textViewPhone.setText(contact.getPhoneNumber());
                    binding.textViewLinks.setText(contact.getLinks());

                    if (contact.getAvatar() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getAvatar(), 0, contact.getAvatar().length);
                        binding.imageViewAvatar.setImageBitmap(bitmap);
                    }
                }
            });
        }

        binding.btnCancell.setOnClickListener(v -> finish());

    }
}
