package com.example.lab4;

import java.util.Comparator;

public class LostItemComparator implements Comparator<LostItem> {
    @Override
    public int compare(LostItem item1, LostItem item2) {
        return item1.getTitle().compareTo(item2.getTitle());
    }
}
