package com.ringerbell.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ringerbell.ContactBaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class DatabaseHelper extends SQLiteOpenHelper {

        private static final String TAG = "DatabaseHelper";

        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_NAME = "ringer_bell.db";

        private static final String TABLE_NAME = "contact";

        // we do this so there is only one helper
        private static DatabaseHelper helper = null;

        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                ContactBaseColumns.ContactColumns._ID + " INTEGER PRIMARY KEY autoincrement, "+
                ContactBaseColumns.ContactColumns.COLUMN_MOBILE_NUMBER +" TEXT, "+
                ContactBaseColumns.ContactColumns.COLUMN_IS_BLOCKED +" TINYINT "+")";

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Get the helper, possibly constructing it if necessary. For each call to this method, there should be 1 and only 1
         * call to {@link #close()}.
         */
        public static synchronized DatabaseHelper getHelper(Context context) {
            if (helper == null) {
                helper = new DatabaseHelper(context);
            }
            return helper;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                Log.d(TAG, "Table has been created");
                db.execSQL(CREATE_TABLE);
            }catch(Exception e){
                Log.d(TAG, "Caught with Exception");
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
}
