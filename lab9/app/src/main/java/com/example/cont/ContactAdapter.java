package com.example.cont;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cont.databinding.ItemContactBinding;

public class ContactAdapter extends ListAdapter<Contact, ContactAdapter.ContactViewHolder> {

    private final ContactClickListener clickListener;

    public ContactAdapter(ContactClickListener clickListener) {
        super(new ContactViewHolder.ContactDiffCallback());
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_contact,
                parent,
                false
        );
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ItemContactBinding binding;

        ContactViewHolder(ItemContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Contact contact, ContactClickListener clickListener) {
            binding.setContact(contact);

            binding.getRoot().setOnClickListener(v -> clickListener.onClick(contact));

            binding.getRoot().setOnLongClickListener(v -> {
                clickListener.onDelete(contact);
                return true;
            });

            binding.btnEditContact.setOnClickListener(v -> clickListener.onEdit(contact));

            if (contact.getAvatar() != null && contact.getAvatar().length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getAvatar(), 0, contact.getAvatar().length);
                Glide.with(binding.imageContactAvatar.getContext())
                        .load(bitmap)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.imageContactAvatar);
            } else {
                binding.imageContactAvatar.setImageResource(R.drawable.ic_launcher_foreground);
            }

            binding.executePendingBindings();
        }

        static class ContactDiffCallback extends DiffUtil.ItemCallback<Contact> {
            @Override
            public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
                return oldItem.equals(newItem);
            }
        }
    }

    public interface ContactClickListener {
        void onClick(Contact contact);
        void onEdit(Contact contact);
        void onDelete(Contact contact);
    }
}
