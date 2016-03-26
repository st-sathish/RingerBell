package com.ringerbell.db;


import android.provider.BaseColumns;

public class OfferMessageBaseColumns {

    public OfferMessageBaseColumns() {

    }

    public static abstract class OfferMessageColumns implements BaseColumns {

        public static final String COLUMN_MESSAGE = "message";

        public static final String COLUMN_SHOP_ID = "shop_id";

        public static final String COLUMN_IS_UNREAD = "is_unread";

        public static final String COLUMN_CREATED_AT = "created_at";

        public static final String COLUMN_MODIFIED_AT = "modified_at";
    }
}
