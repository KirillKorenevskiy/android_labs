package com.example.lab4;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lab4.LostItem;
import com.example.lab4.R;

public class DetailFragment extends Fragment {

    private EditText titleTextView;
    private EditText descriptionTextView;
    private EditText locationTextView;
    private EditText dateFoundTextView;
    private EditText foundByTextView;
    private EditText retrieveLocationTextView;
    private ImageView photoImageView;
    private LostItem lostItem;

    public DetailFragment() {
    }

    public void setLostItem(LostItem item) {
        this.lostItem = item;
        if (getView() != null) {
            updateUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detaile, container, false);
        titleTextView = rootView.findViewById(R.id.titleTextView);
        descriptionTextView = rootView.findViewById(R.id.descriptionTextView);
        locationTextView = rootView.findViewById(R.id.locationTextView);
        dateFoundTextView = rootView.findViewById(R.id.dateFoundTextView);
        foundByTextView = rootView.findViewById(R.id.foundByTextView);
        retrieveLocationTextView = rootView.findViewById(R.id.retrieveLocationTextView);
        photoImageView = rootView.findViewById(R.id.photoImageView);

        if (lostItem != null) {
            updateUI();
        }

        return rootView;
    }

    private void updateUI() {
        titleTextView.setText(lostItem.getTitle());
        descriptionTextView.setText(lostItem.getDescription());
        locationTextView.setText(lostItem.getLocation());
        dateFoundTextView.setText(lostItem.getDateFound());
        foundByTextView.setText(lostItem.getFoundBy());
        retrieveLocationTextView.setText(lostItem.getRetrieveLocation());

        if (lostItem.getPhoto() != null) {
            Glide.with(this)
                    .load(Uri.parse(lostItem.getPhoto()))
                    .into(photoImageView);
        }
    }
}