package com.ringerbell.daoImpl;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ringerbell.Contact;
import com.ringerbell.ContactBaseColumns;
import com.ringerbell.dao.ContactDAO;
import com.ringerbell.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDAOImpl implements ContactDAO {

    private DatabaseHelper databaseHelper = null;

    private List<Contact> contactList = null;

    public ContactDAOImpl(Activity activity, List<Contact> contactList) {
        databaseHelper = DatabaseHelper.getHelper(activity);
        this.contactList = contactList;
    }

    public ContactDAOImpl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int saveOrUpdate(Contact contact, boolean shouldBlockNumber) {
        int savedOrUpdated = 0;
        ContentValues cv = null;
        Contact c = getContactByMobileNumber(contact.getPhoneNumber());
        if(c == null) {
            cv = new ContentValues();
            cv.put(ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER, contact.getPhoneNumber());
            cv.put(ContactBaseColumns.ContactColumns.COLUMN_IS_BLOCKED, shouldBlockNumber);
            savedOrUpdated = (int) databaseHelper.getReadableDatabase().insert("contact", null, cv);
        } else {
            cv = new ContentValues();
            cv.put(ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER, contact.getPhoneNumber());
            cv.put(ContactBaseColumns.ContactColumns.COLUMN_IS_BLOCKED, (contact.getBlocked() == 1 ? 0 : contact.getBlocked()));
            savedOrUpdated = databaseHelper.getReadableDatabase().update("contact", cv, ContactBaseColumns.ContactColumns._ID+" = ?", new String[]{String.valueOf(contact.getId())});
        }
        return savedOrUpdated;
    }

    @Override
    public int delete(Contact contact) {
        return databaseHelper.getReadableDatabase().delete("contact", ContactBaseColumns.ContactColumns._ID+" = ?", new String[]{String.valueOf(contact.getId())});
    }

    @Override
    public Contact getContactByMobileNumber(String mobileNumber) {
        String selectQuery = "SELECT  * FROM contact where "+ ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER+ " = '"+mobileNumber+"'";
        Cursor cursor = null;
        SQLiteDatabase database = null;
        Contact contact = null;
        try {
            database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String contactNumber = cursor.getString(cursor.getColumnIndex(ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER));
                    int blocked = cursor.getInt(cursor.getColumnIndex(ContactBaseColumns.ContactColumns.COLUMN_IS_BLOCKED));
                    contact = new Contact();
                    contact.setId(cursor.getInt(cursor.getColumnIndex(ContactBaseColumns.ContactColumns._ID)));
                    contact.setBlocked(blocked);
                    contact.setPhoneNumber(contactNumber);
                }while(cursor.moveToNext());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(null != cursor) {
                cursor.close();
            }
            if(null != database) {
                database.close();
            }
        }
        return contact;
    }

    @Override
    public List<Contact> getContacts(boolean isBlocked) {
        String selectQuery = "SELECT  * FROM contact where "+ ContactBaseColumns.ContactColumns.COLUMN_IS_BLOCKED+ " = "+((isBlocked) ? 1 : 0);
        Cursor cursor = null;
        SQLiteDatabase database = null;
        List<Contact> contacts = new ArrayList<Contact>();

        try {
            database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String contactNumber = cursor.getString(cursor.getColumnIndex(ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER));
                    String displayName = null;
                    if(null != contactList) {
                        for(Contact c : contactList) {
                            if(c.getPhoneNumber() != null && c.getPhoneNumber().equals(contactNumber)) {
                                displayName = c.getDisplayName();
                                break;
                            }
                        }
                    }
                    Contact contact = new Contact();
                    contact.setId(cursor.getInt(cursor.getColumnIndex(ContactBaseColumns.ContactColumns._ID)));
                    contact.setDisplayName(displayName);
                    contact.setPhoneNumber(contactNumber);
                    contacts.add(contact);
                }while(cursor.moveToNext());
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(null != cursor) {
                cursor.close();
            }
            if(null != database) {
                database.close();
            }
        }
        return contacts;
    }
}
