package com.ringerbell;


import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactBaseAdapter extends BaseAdapter {

    private List<Contact> contacts;

    private String tabName = "";

    private RingerBellActivity ringerActivity;

    private RingerBellActivity.PlaceholderFragment placeholderFragment = null;

    public ContactBaseAdapter(List<Contact> contacts, RingerBellActivity ringerActivity, String tabName, RingerBellActivity.PlaceholderFragment placeholderFragment) {
        this.contacts = contacts;
        this.ringerActivity = ringerActivity;
        this.tabName = tabName;
        this.placeholderFragment = placeholderFragment;
    }

    @Override
    public int getCount() {
        if(null != this.contacts) {
            return this.contacts.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return this.contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(null == convertView) {
            LayoutInflater layoutInflater = (LayoutInflater) ringerActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_contact, viewGroup, false);
            holder = new ViewHolder();
            holder.displayName = (TextView) convertView.findViewById(R.id.display_name);
            holder.phoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Contact contact = contacts.get(position);
        holder.displayName.setText(contact.getDisplayName());
        holder.phoneNumber.setText(contact.getPhoneNumber());
        convertView.setOnClickListener(new ContactPopupMenuListener(ringerActivity, contact, position, this.tabName, placeholderFragment));
        return convertView;
    }

    private static class ViewHolder {
        TextView displayName;
        TextView phoneNumber;
    }
}
