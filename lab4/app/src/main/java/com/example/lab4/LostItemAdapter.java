package com.example.lab4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lab4.LostItem;

import java.util.List;

public class LostItemAdapter extends ArrayAdapter<LostItem> {
    private final Context context;
    private final List<LostItem> lostItems;

    public LostItemAdapter(Context context, List<LostItem> lostItems) {
        super(context, R.layout.lost_item, lostItems);
        this.context = context;
        this.lostItems = lostItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lost_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView locationTextView = convertView.findViewById(R.id.locationTextView);

        LostItem lostItem = lostItems.get(position);
        titleTextView.setText(lostItem.getTitle());
        descriptionTextView.setText(lostItem.getDescription());
        locationTextView.setText(lostItem.getLocation());

        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("lostItem", lostItem);
            intent.putExtra("itemPosition", position);
            ((Activity) context).startActivityForResult(intent, 1);
        });

        convertView.setOnLongClickListener(v -> {
            return false;
        });

        return convertView;
    }
}