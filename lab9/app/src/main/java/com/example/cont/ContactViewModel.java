package com.example.cont;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class ContactViewModel extends ViewModel {
    private final ContactRepository repository;
    private final LiveData<List<Contact>> allContacts;

    public ContactViewModel(ContactRepository repository) {
        this.repository = repository;
        this.allContacts = repository.getAllContacts();
    }
    public LiveData<Contact> getContactById(int contactId) {
        return repository.getContactById(contactId);
    }
    public LiveData<List<Contact>> getAllContacts() { return allContacts; }

    public void insert(Contact contact) { repository.insert(contact); }
    public void update(Contact contact) { repository.update(contact); }
    public void delete(Contact contact) { repository.delete(contact); }

    public static class Factory implements ViewModelProvider.Factory {
        private final ContactRepository repository;

        public Factory(ContactRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ContactViewModel.class)) {
                return (T) new ContactViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

class ContactRepository {
    private final ContactDao contactDao;
    private final LiveData<List<Contact>> allContacts;

    public ContactRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
        this.allContacts = contactDao.getAllContacts();
    }
    public LiveData<Contact> getContactById(int contactId) {
        return contactDao.getContactById(contactId);
    }
    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public void insert(Contact contact) {
        new Thread(() -> contactDao.insert(contact)).start();
    }

    public void update(Contact contact) {
        new Thread(() -> contactDao.update(contact)).start();
    }

    public void delete(Contact contact) {
        new Thread(() -> contactDao.delete(contact)).start();
    }
}
