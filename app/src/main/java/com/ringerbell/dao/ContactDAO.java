package com.ringerbell.dao;


import com.ringerbell.Contact;

import java.util.List;

public interface ContactDAO {

    public List<Contact> getContacts(boolean isBlocked);

    int saveOrUpdate(Contact contact, boolean shouldBlockNumber);

    int delete(Contact contact);

    public Contact getContactByMobileNumber(String mobileNumber);

}
