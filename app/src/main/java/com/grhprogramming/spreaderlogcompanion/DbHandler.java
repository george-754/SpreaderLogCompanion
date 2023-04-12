package com.grhprogramming.spreaderlogcompanion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Array;
import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "jobs_que.db";
    private static final String TABLE_NAME = "jobs_table";

    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_FARMER = "farmer";
    private static final String KEY_FIELD = "field";
    private static final String KEY_PRODUCT = "product";
    private static final String KEY_ACRES = "acres";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DATE + " TEXT," + KEY_FARMER + " TEXT," + KEY_FIELD + " TEXT," + KEY_PRODUCT + " TEXT," + KEY_ACRES + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // Add Record
    void insertJob(String s_Date, String s_Farmer, String s_Field, String s_Product, String s_Acres) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cValues = new ContentValues();

        cValues.put(KEY_DATE, s_Date);
        cValues.put(KEY_FARMER, s_Farmer);
        cValues.put(KEY_FIELD, s_Field);
        cValues.put(KEY_PRODUCT, s_Product);
        cValues.put(KEY_ACRES, s_Acres);

        long newRowID = db.insert(TABLE_NAME, null, cValues);
        db.close();
    }

    // Delete Record
    public void DeleteJob(int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{String.valueOf(jobId)});
        db.close();
    }

    public ArrayList<DataModel> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<DataModel> dataList = new ArrayList<>();

        String query = "SELECT id, date, farmer, field, product, acres FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            // ArrayList<DataModel> job = new ArrayList<>();

            dataList.add(new DataModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_FARMER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIELD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACRES))));
        }
        cursor.close();
        db.close();

        return dataList;
    }

    public long getQueryCount() {
        // Return the total entries in the sql database
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();

        return count;
    }

    public DataModel getFirst() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        DataModel data = new DataModel(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_FARMER)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_FIELD)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_ACRES)));

        cursor.close();
        db.close();

        return data;
    }
}
