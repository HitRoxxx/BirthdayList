package com.example.hitro.birthdaylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by HitRo on 02-02-2017.
 */

public class MyDatabase extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;
    MainActivity ma;

    public MyDatabase(Context context) {
        super(context, "Birthday_database", null, 1);
        this.context = context;
        ma = (MainActivity) context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // String query = "CREATE TABLE"+ " tb "+"("+" ID "+" TEXT PRIMARY KEY,"+ " NAME "+ " TEXT" +")";
        // Toast.makeText(context, "" + db, Toast.LENGTH_SHORT).show();
        this.db = db;

       /* String query = "CREATE TABLE BIRTHDAY ( ID  INTEGER PRIMARY KEY AUTOINCREMENT , NAME  TEXT , PHONE_NO  TEXT , EMAIL TEXT , DATE TEXT , IMAGE  BLOB NOT NULL)";
        db.execSQL(query);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(String name, String birthday, String email, String phone_no, byte[] image) {
        //String query ="insert into tb value()"
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();//insertion command
        cv.put("NAME", name);
        cv.put("PHONE_NO", phone_no);
        cv.put("EMAIL", email);
        cv.put("DATE", birthday);
        cv.put("IMAGE", image);

        Long val = db.insert("BIRTHDAY", null, cv);
      /*  Toast.makeText(context, val.toString(), Toast.LENGTH_SHORT).show();*/
        if (val > 0) {
            Toast.makeText(context, "Birthday Saved", Toast.LENGTH_SHORT).show();
            ma.getData(true);
        }
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }
}
