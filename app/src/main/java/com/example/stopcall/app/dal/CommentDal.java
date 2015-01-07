package com.example.stopcall.app.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.stopcall.app.Constants;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

public class CommentDal extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constants.DB_VERSION;
    // Database Name
    private static final String DATABASE_NAME = Constants.DB_NAME;

    private static final String COMMENTS_TABLE = "COMMENTS_TABLE";

    @Inject
    public CommentDal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table

        String CREATE_COMMENTS_TABLE =
                "CREATE TABLE " + COMMENTS_TABLE +
                        " ( "+ KEY_ID+" INTEGER, "
                             + KEY_COMMENT_TEXT+" TEXT, "
                             + "FOREIGN KEY("+KEY_ID+") REFERENCES "+PhoneDal.BLOCKED_PHONES_TABLE+"("+KEY_ID+")" +
                        "ON DELETE CASCADE," +
                        "unique ("+KEY_ID+", "+KEY_COMMENT_TEXT+"))";

        db.execSQL(CREATE_COMMENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            Log.w("MyAppTag", "Updating database from version " + oldVersion
                    + " to " + newVersion + " .Existing data will be lost.");
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS " + COMMENTS_TABLE);

            // create fresh books table
            this.onCreate(db);
        }
    }


    private static final String KEY_ID = "KEY_ID";
    private static final String KEY_COMMENT_TEXT = "KEY_COMMENT_TEXT";

    public boolean addItem(long phoneID, String comment) {
        Log.d(Constants.LOGGER_TAG, "add saved-offer");
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, phoneID);
        values.put(KEY_COMMENT_TEXT, comment);

        // 3. insert
        long newRowId = db.insertWithOnConflict(COMMENTS_TABLE, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
        // 4. close
        db.close();

        return newRowId >0;
    }

    public List<String> getAllItems(int phoneId) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = getReadableDatabase().query(COMMENTS_TABLE,
                new String[] { KEY_COMMENT_TEXT }, KEY_ID+" = ?",
                new String[] { String.valueOf(phoneId) },
                null, null, null);

        List<String> comments = new ArrayList<String>();

        if (cursor.moveToFirst()) {
            do {
                comments.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return comments;
    }
}
