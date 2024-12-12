package com.example.cont;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;

@Entity(tableName = "contacts")
public class Contact extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String position;
    private String phoneNumber;
    private String links;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;

    public Contact(String name, String position, String phoneNumber, byte[] avatar, String links) {
        this.name = name;
        this.position = position;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.links = links;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        notifyPropertyChanged(BR.position);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Bindable
    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
        notifyPropertyChanged(BR.links);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contact)) return false;

        Contact other = (Contact) obj;

        return id == other.id &&
                name.equals(other.name) &&
                position.equals(other.position) &&
                phoneNumber.equals(other.phoneNumber) &&
                Arrays.equals(avatar, other.avatar) &&
                links.equals(other.links);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, name, position, phoneNumber, Arrays.hashCode(avatar), links);
    }
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS new_contacts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT, position TEXT, phoneNumber TEXT, links TEXT, avatar BLOB)");

            database.execSQL("INSERT INTO new_contacts (id, name, position, phoneNumber, links) SELECT id, name, position, phoneNumber, links FROM contacts");

            database.execSQL("DROP TABLE contacts");

            database.execSQL("ALTER TABLE new_contacts RENAME TO contacts");
        }
    };
}
