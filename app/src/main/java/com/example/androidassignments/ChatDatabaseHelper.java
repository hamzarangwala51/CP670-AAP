package com.example.androidassignments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Messages.db";
    private  static  final  int VERSION_NUM = 7;
    public static final String TABLE_NAME = "messages";
    public static final String KEY_ID = "id";
    public static final String KEY_MESSAGE = "message";
    private static ChatDatabaseHelper sInstance;
    public ChatDatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,VERSION_NUM);
    }


    public static synchronized ChatDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ChatDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MESSAGE + " TEXT);";
        db.execSQL(createTableQuery);
        Log.i("ChatDatabaseHelper", "onCreate called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
            onCreate(db);
        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }
}
