package com.example.stopcall.app.dal;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import com.example.stopcall.app.Constants;

public class DalFactory extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = Constants.DB_VERSION;
    // Database Name
    private static final String DATABASE_NAME = Constants.DB_NAME;

    private PhoneDal phoneDal;
    private CommentDal commentDal;

    public DalFactory(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        phoneDal = new PhoneDal(context);
        commentDal = new CommentDal(context);
        this.getReadableDatabase(); //just a trigger
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.execSQL("PRAGMA foreign_keys=ON;");
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
        phoneDal.onCreate(sqLiteDatabase);
        commentDal.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        phoneDal.onUpgrade(sqLiteDatabase, i, i1);
        commentDal.onUpgrade(sqLiteDatabase, i, i1);
    }




}
