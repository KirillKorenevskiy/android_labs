package com.example.cont;

import static com.example.cont.Contact.MIGRATION_1_2;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

@Database(entities = {Contact.class}, version = 2)
public abstract class ContactDatabase extends RoomDatabase {

    private static volatile ContactDatabase INSTANCE;

    public abstract ContactDao contactDao();

    public static ContactDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ContactDatabase.class, "contact_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
