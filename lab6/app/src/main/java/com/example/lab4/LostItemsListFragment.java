package com.example.lab4;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostItemsListFragment extends Fragment {

    private ListView listView;
    private LostItemAdapter adapter;
    private List<LostItem> lostItems;
    private List<LostItem> originalLostItems;

    public LostItemsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lost_items_list, container, false);
        listView = rootView.findViewById(R.id.findListView);

        loadLostItems();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            LostItem selectedItem = lostItems.get(position);
            ((MainActivity) getActivity()).showDetailFragment(selectedItem);
        });

        return rootView;
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        int itemId = item.getItemId();
        if(itemId == R.id.action_view){
            //openDetailActivity(position, false);
            return true;
        }else if(itemId == R.id.action_edit){
            //openEditActivity(position, true);
            return true;
        }else if(itemId == R.id.action_delete){
            deleteItem(position);
            loadLostItems();
            return true;
        }
        return true;
    }

    private void deleteItem(int position) {
        LostItem itemToDelete = lostItems.get(position);
        new AlertDialog.Builder(getActivity())
                .setMessage("Вы уверены, что хотите удалить \"" + itemToDelete.getTitle() + "\"?")
                .setPositiveButton("Да", (dialog, which) -> {
                    lostItems.remove(position);
                    adapter.notifyDataSetChanged();
                    saveLostItemsToJsonFile(lostItems);
                    Toast.makeText(getActivity(), "Элемент удален", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Нет", (dialog, which) -> dialog.cancel())
                .show();
    }

    private void saveLostItemsToJsonFile(List<LostItem> lostItems) {
        Gson gson = new Gson();
        File file = new File(getActivity().getExternalFilesDir(null), "lost_items_data.json");
        if (file.exists()) {
            if (!file.delete()) {
                return;
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String json = gson.toJson(lostItems);
            writer.write(json);
        } catch (IOException e) {
        }
    }

    private List<LostItem> loadLostItemsFromJson() {
        List<LostItem> lostItems = new ArrayList<>();
        Gson gson = new Gson();
        File file = new File(getActivity().getExternalFilesDir(null), "lost_items_data.json");
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.findListView) {
            getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLostItems();
    }

    private void showMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
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
            Log.d("MainActivity", "After sort: " + lostItems);
        } else {
        }
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final EditText input = new EditText(getActivity());
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
    }
}