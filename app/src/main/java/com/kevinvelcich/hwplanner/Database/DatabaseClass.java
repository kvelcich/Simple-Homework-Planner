package com.kevinvelcich.hwplanner.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.kevinvelcich.hwplanner.Classes.Class;

public class DatabaseClass extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "classDB.db";

    private static final String TABLE = "classes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "classname";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_COLOR = "color";

    public DatabaseClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase dB) {
        String CREATE_TABLE_CLASS = "CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " " + "INTEGER PRIMARY KEY," +
                COLUMN_NAME + " TEXT," +
                COLUMN_QUANTITY + " INTEGER," +
                COLUMN_COLOR + " INTEGER" + ")";

        dB.execSQL(CREATE_TABLE_CLASS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            onCreate(db);
    }

    public void addClass(Class mClass) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, mClass.getClassName());
        values.put(COLUMN_QUANTITY, mClass.getQuantity());
        values.put(COLUMN_COLOR, mClass.getColor());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE, null, values);
        db.close();
    }

    public boolean deleteClass(String classname) {

        boolean result = false;

        String query = "Select * FROM " + TABLE + " WHERE " + COLUMN_NAME + " =  \"" + classname + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Class mClass = new Class();

        if (cursor.moveToFirst()) {
            mClass.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(mClass.getID()) });
            cursor.close();
            result = true;
        }
        db.close();

        return result;
    }

    public void addQuantity(String name) {
        String query = "Select * FROM " + TABLE + " WHERE " + COLUMN_NAME + " =  \"" + name + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues();

        values.put(COLUMN_QUANTITY, cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)) + 1);

        db.update(TABLE, values, COLUMN_NAME + " = '" + name + "'", null);
        cursor.close();
        db.close();
    }

    public void changeName(String name, String newname) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newname);

        db.update(TABLE, values, COLUMN_NAME + " = '" + name + "'", null);
        db.close();
    }

    public void changeColor(String name, int color) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COLOR, color);

        db.update(TABLE, values, COLUMN_NAME + " = '" + name + "'", null);
        db.close();
    }

    public void decrementQuantity(String classname) {
        String query = "Select * FROM " + TABLE + " WHERE " + COLUMN_NAME + " =  \"" + classname + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        ContentValues values = new ContentValues();

        values.put(COLUMN_QUANTITY, cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)) - 1);

        db.update(TABLE, values, COLUMN_NAME + " = '" + classname + "'", null);
        cursor.close();
        db.close();
    }

    public Cursor getClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_QUANTITY + ", " + COLUMN_COLOR + " FROM " + TABLE;
        Cursor cursor =  db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        return cursor;
    }

    public boolean findClass(String name) {
        String query = "Select * FROM " + TABLE + " WHERE " + COLUMN_NAME + " =  \"" + name + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        boolean val = cursor.moveToFirst();
        cursor.close();
        db.close();
        return val;
    }
}
