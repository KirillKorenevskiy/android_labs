package com.example.cont;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cont.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ContactAdapter.ContactClickListener {

    private ActivityMainBinding binding;
    private ContactAdapter adapter;
    private ContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactDatabase database = ContactDatabase.getInstance(this);
        ContactDao contactDao = database.contactDao();

        ContactRepository repository = new ContactRepository(contactDao);
        ContactViewModel.Factory factory = new ContactViewModel.Factory(repository);
        viewModel = new ViewModelProvider(this, factory).get(ContactViewModel.class);

        setupRecyclerView();
        viewModel.getAllContacts().observe(this, contacts -> adapter.submitList(contacts));

        binding.fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditContactActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new ContactAdapter(this); 

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Contact contact) {
        Intent intent = new Intent(this, ContactDetailActivity.class);
        intent.putExtra("contact_id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void onEdit(Contact contact) {
        Intent intent = new Intent(this, AddEditContactActivity.class);
        intent.putExtra("contact_id", contact.getId()); // Передаем ID контакта для редактирования
        startActivity(intent); // Запускаем Activity для редактирования контакта
    }
    @Override
    public void onDelete(Contact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Удалить контакт")
                .setMessage("Вы действительно хотите удалить этот контакт?")
                .setPositiveButton("Да", (dialog, which) -> {
                    viewModel.delete(contact);
                    Toast.makeText(this, "Контакт удален", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
