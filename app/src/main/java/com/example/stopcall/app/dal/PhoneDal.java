package com.example.stopcall.app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.stopcall.app.Constants;
import com.example.stopcall.app.dal.dto.Phone;
import com.google.inject.Inject;

public class PhoneDal extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constants.DB_VERSION;
    // Database Name
    private static final String DATABASE_NAME = Constants.DB_NAME;

    public static final String BLOCKED_PHONES_TABLE = "BLOCKED_PHONES_TABLE";

    @Inject
    public PhoneDal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BLOCKED_PHONES_TABLE =
                "CREATE TABLE "+ BLOCKED_PHONES_TABLE +
                        " ( "+ KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 1, "
                        + KEY_PHONE + " TEXT UNIQUE,"
                        + KEY_IS_BLOCKED+" INTEGER )";

        db.execSQL(CREATE_BLOCKED_PHONES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w("MyAppTag", "Updating database from version " + oldVersion
                    + " to " + newVersion + " .Existing data will be lost.");
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS " + BLOCKED_PHONES_TABLE);

            // create fresh books table
            this.onCreate(db);
        }

    }


    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_PHONE = "KEY_PHONE";
    private static final String KEY_IS_BLOCKED = "KEY_IS_BLOCKED";

    public long addItem(Phone phone) {
        Log.d(Constants.LOGGER_TAG, "add saved-offer");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        //values.put(KEY_ID, phone.id);
        values.put(KEY_PHONE, phone.phone);
        values.put(KEY_IS_BLOCKED, phone.isBlocked);

        // 3. insert
        long newRowId =
                db.insertWithOnConflict(BLOCKED_PHONES_TABLE, KEY_ID,
                values, SQLiteDatabase.CONFLICT_IGNORE);

        if (newRowId > 0) {
            final String text = String.format("item was added to table: %s",
                    BLOCKED_PHONES_TABLE);
            Log.d(Constants.LOGGER_TAG, text);

            // BaseApplication.getCurrentActivity().runOnUiThread(new Runnable()
            // {
            //
            // @Override
            // public void run() {
            // if (Constants.IS_DEBUG) {
            // Toast.makeText(mContext, text, Toast.LENGTH_LONG)
            // .show();
            // }
            // }
            // });
        }

        // 4. close
        db.close();
        return newRowId;
    }

    public long updateItem(Phone phone) {
        Log.d(Constants.LOGGER_TAG, "add saved-offer");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, phone.id);

        // 3. update
        long newRowId =
                db.update(BLOCKED_PHONES_TABLE, values,
                        KEY_ID + " = ?",
                        new String[]{String.valueOf(phone.id)});

        if (newRowId > 0) {
            final String text = String.format("item was added to table: %s",
                    BLOCKED_PHONES_TABLE);
            Log.d(Constants.LOGGER_TAG, text);
        }

        // 4. close
        db.close();
        return newRowId;
    }


    public Phone getItem(String phone, boolean isBlocked) {
        Phone result = null;

        Cursor cursor = this.getReadableDatabase().query(
                BLOCKED_PHONES_TABLE,
                new String[] { KEY_ID  }, KEY_PHONE+" = ? AND "+KEY_IS_BLOCKED+" = ?",
                new String[] { phone , isBlocked? "1" : "0"}, null, null, null);

        while(cursor.moveToNext()) {
            result = new Phone();
            result.id = (cursor.getInt(0));
            result.phone = phone;
            result.isBlocked = true;
        }
        return result;
    }


    public Phone getItem(String phone) {
        Phone result = null;

        Cursor cursor = this.getReadableDatabase().query(
                BLOCKED_PHONES_TABLE,
                new String[] { KEY_ID  }, KEY_PHONE+" = ?",
                new String[] { phone }, null, null, null);

        while(cursor.moveToNext()) {
            result = new Phone();
            result.id = (cursor.getInt(0));
            result.phone = phone;
            result.isBlocked = true;
        }
        return result;
    }

}
