package com.ringerbell;


import android.provider.BaseColumns;

public class ContactBaseColumns {

    public ContactBaseColumns(){}

    public static abstract class ContactColumns implements BaseColumns {

        public static final String COLUMN_MOBILE_NUMBER = "mobile_number";

        public static final String COLUMN_IS_BLOCKED = "is_blocked";

    }
}
