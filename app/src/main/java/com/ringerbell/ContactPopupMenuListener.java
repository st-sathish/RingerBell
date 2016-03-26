package com.ringerbell;

import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ringerbell.dao.ContactDAO;
import com.ringerbell.daoImpl.ContactDAOImpl;


public class ContactPopupMenuListener implements View.OnClickListener {

    private Contact contact;
    private RingerBellActivity ringerBellActivity;
    private int position;
    private String tabName;
    private RingerBellActivity.PlaceholderFragment placeholderFragment = null;

    public ContactPopupMenuListener(RingerBellActivity ringerBellActivity, Contact contact, int position, String tabName, RingerBellActivity.PlaceholderFragment placeholderFragment) {
        this.ringerBellActivity = ringerBellActivity;
        this.contact = contact;
        this.position = position;
        this.tabName = tabName;
        this.placeholderFragment = placeholderFragment;
    }

    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(ringerBellActivity, v) {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                ContactDAO contactDAO = new ContactDAOImpl(ringerBellActivity, null);
                switch (item.getItemId()) {
                    case R.id.mark_as_black_list:
                        int markAsBLackList = contactDAO.saveOrUpdate(contact, true);
                        if(null != placeholderFragment) {
                            placeholderFragment.removeObjectFromListView(position);
                        }
                        Toast.makeText(ringerBellActivity, "Marked as Black List", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.mark_as_white_list:
                        int markAsWhiteList = contactDAO.saveOrUpdate(contact, false);
                        if(null != placeholderFragment) {
                            placeholderFragment.removeObjectFromListView(position);
                        }
                        Toast.makeText(ringerBellActivity, "Marked as White List", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.remove:
                        int remove = contactDAO.delete(contact);
                        if(null != placeholderFragment) {
                            placeholderFragment.removeObjectFromListView(position);
                        }
                        Toast.makeText(ringerBellActivity, "Marked as White List", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };
        if(tabName.equals("black_list")) {
            popupMenu.inflate(R.menu.contact_popup_wl_menu);
        } else if(tabName.equals("white_list")) {
            popupMenu.inflate(R.menu.contact_popup_bl_menu);
        } else {
            popupMenu.inflate(R.menu.contact_popup_menu);
        }
        popupMenu.show();
    }
}
