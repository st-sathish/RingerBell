package com.ringerbell;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.widget.ListView;

import com.ringerbell.dao.ContactDAO;
import com.ringerbell.daoImpl.ContactDAOImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadContactAsync extends AsyncTask<Void, Void, List<Contact>> {

    private ProgressDialog progressDialog;

    private final RingerBellActivity ringerBellActivity;

    private final ListView listView;

    private static final String HAS_PHONE_NUMBER_INDEX = "1";

    private String tabName = null;

    private RingerBellActivity.PlaceholderFragment placeholderFragment = null;

    public LoadContactAsync(RingerBellActivity ringerBellActivity, ListView listView, String tabName, RingerBellActivity.PlaceholderFragment placeholderFragment) {
        this.ringerBellActivity = ringerBellActivity;
        this.listView = listView;
        this.tabName = tabName;
        this.placeholderFragment = placeholderFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(ringerBellActivity, "Loading Contacts", "Please Wait");
    }

    @Override
    protected List<Contact> doInBackground(Void... params) {
        ContentResolver contentResolver = this.ringerBellActivity.getContentResolver();
        Cursor cursor = null;
        List<Contact> contactList = null;
        try {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (null != cursor && 0 < cursor.getCount()) {
                contactList = new ArrayList<Contact>();
                while (cursor.moveToNext()) {
                    Contact contact = new Contact();
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    contact.setId(Integer.parseInt(id));
                    contact.setDisplayName(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (HAS_PHONE_NUMBER_INDEX.equals(hasPhone)) {
                        contact.setPhoneNumber(this.getPhoneNumber(contentResolver, id));
                    }
                    contactList.add(contact);
                }
            }

            if(null != contactList && !(tabName.equals("contact"))) {
                ContactDAO contactDAO = new ContactDAOImpl(this.ringerBellActivity, contactList);
                contactList = (tabName.equals("black_list")) ? contactDAO.getContacts(true) : contactDAO.getContacts(false);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contactList;
    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        super.onPostExecute(contacts);
        progressDialog.cancel();
        ContactBaseAdapter contactBaseAdapter = new ContactBaseAdapter(contacts, this.ringerBellActivity, tabName, placeholderFragment);
        this.listView.setAdapter(contactBaseAdapter);
        placeholderFragment.setAdapter(contacts, contactBaseAdapter);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(List<Contact> contacts) {
        super.onCancelled(contacts);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     * read phone number from contacts
     * @param contentResolver
     * @param contactId
     * @return
     */
    private String getPhoneNumber(ContentResolver contentResolver, String contactId) {
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{contactId}, null);
        String phoneNumber = "";
        try {
            if(null != phones) {
                while (phones.moveToNext()) {
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(null != phones) {
                phones.close();
            }
        }
        return phoneNumber;
    }
}
