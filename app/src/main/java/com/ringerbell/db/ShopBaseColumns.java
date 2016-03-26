package com.ringerbell.db;


import android.provider.BaseColumns;

public class ShopBaseColumns {

    public ShopBaseColumns(){}

    public static abstract class ShopColumns implements BaseColumns {

        public static final String COLUMN_SHOP_NAME = "shop_name";

        public static final String COLUMN_LOGO_URL = "logo_url";

        public static final String COLUMN_CREATED_AT = "created_at";

    }
}
