package com.example.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com. example. lab4.R;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private LostItemAdapter adapter;
    private List<LostItem> lostItems;
    private List<LostItem> originalLostItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        listView = findViewById(R.id.findListView);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        registerForContextMenu(listView);

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        listView = findViewById(R.id.findListView);

        loadLostItems();

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            showMenu(v);
        });
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.points_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_sort) {
                sortItems();
                return true;
            } else if (item.getItemId() == R.id.action_search) {
                showSearchDialog();
            return true;
            } else {
                return false;
            }
        });
        popup.show();
    }

    private void sortItems() {
        if (lostItems != null && !lostItems.isEmpty()) {
            Log.d("MainActivity", "Before sort: " + lostItems);
            Collections.sort(lostItems, new LostItemComparator());
            adapter = new LostItemAdapter(this, lostItems);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Список отсортирован", Toast.LENGTH_SHORT).show();
            Log.d("MainActivity", "After sort: " + lostItems);
        } else {
            Toast.makeText(this, "Список пуст", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Поиск", (dialog, which) -> {
            String query = input.getText().toString();
            searchItems(query);
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void searchItems(String query) {
        List<LostItem> filteredList = new ArrayList<>();
        for (LostItem item : originalLostItems) {
            if (item.getTitle().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Элементы не найдены", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.findListView) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLostItems();
    }

    private void loadLostItems() {
        lostItems = loadLostItemsFromJson();
        originalLostItems = new ArrayList<>(lostItems);
        if (adapter == null) {
            adapter = new LostItemAdapter(this, lostItems);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(lostItems);
            adapter.notifyDataSetChanged();
        }
    }

    private List<LostItem> loadLostItemsFromJson() {
        List<LostItem> lostItems = new ArrayList<>();
        Gson gson = new Gson();
        File file = new File(getExternalFilesDir(null), "lost_items_data.json");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Type lostItemListType = new TypeToken<List<LostItem>>() {}.getType();
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        int itemId = item.getItemId();
        if(itemId == R.id.action_view){
            openDetailActivity(position, false);
            return true;
        }else if(itemId == R.id.action_edit){
            openEditActivity(position, true);
            return true;
        }else if(itemId == R.id.action_delete){
            deleteItem(position);
            loadLostItems();
            return true;
        }
        return true;
    }

    private void openDetailActivity(int position, boolean isEditMode) {
        LostItem item = lostItems.get(position);
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("lostItem", item);
        intent.putExtra("itemPosition", position);
        intent.putExtra("isEditMode", isEditMode);
        startActivityForResult(intent, 1);
    }

    private void openEditActivity(int position, boolean isEditMode) {
        openDetailActivity(position, isEditMode);
    }

    private void deleteItem(int position) {
        LostItem itemToDelete = lostItems.get(position);
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите удалить \"" + itemToDelete.getTitle() + "\"?")
                .setPositiveButton("Да", (dialog, which) -> {
                    lostItems.remove(position);
                    adapter.notifyDataSetChanged();
                    saveLostItemsToJsonFile(lostItems);
                    Toast.makeText(this, "Элемент удален", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                .show();
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