package com.example.contactlist.framework.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactlist.framework.model.DataBaseModel;

import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contacts_db";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(DataBaseModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DataBaseModel.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insert(String cid,String name,String number) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(DataBaseModel.COLUMN_CID, cid);
        values.put(DataBaseModel.COLUMN_NAME, name);
        values.put(DataBaseModel.COLUMN_NUMBER, number);

        // insert row
        long id = db.insertWithOnConflict(DataBaseModel.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public DataBaseModel getContact(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DataBaseModel.TABLE_NAME,
                new String[]{DataBaseModel.COLUMN_CID, DataBaseModel.COLUMN_NAME, DataBaseModel.COLUMN_NUMBER},
                DataBaseModel.COLUMN_CID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Data object
        DataBaseModel contact = new DataBaseModel(

                cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_CID)),
                cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_NUMBER)));

        // close the db connection
        cursor.close();

        return contact;
    }

    public List<DataBaseModel> getAllContact() {
        List<DataBaseModel> contact = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DataBaseModel.TABLE_NAME + " ORDER BY " +
                DataBaseModel.COLUMN_NAME+" COLLATE NOCASE; " ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseModel contacts = new DataBaseModel();
                contacts.setCid(cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_CID)));
                contacts.setName(cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_NAME)));
                contacts.setNumber(cursor.getString(cursor.getColumnIndex(DataBaseModel.COLUMN_NUMBER)));

                contact.add(contacts);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return contact;
    }

    public int getContactCount() {
        String countQuery = "SELECT  * FROM " + DataBaseModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateContact(String name,String number,String cid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBaseModel.COLUMN_NAME, name);
        values.put(DataBaseModel.COLUMN_NUMBER, number);


        // updating row
        return db.update(DataBaseModel.TABLE_NAME, values, DataBaseModel.COLUMN_CID + " = ?",
                new String[]{cid});
    }

    public void delete(String cid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataBaseModel.TABLE_NAME, DataBaseModel.COLUMN_CID + " = ?",
                new String[]{cid});
        db.close();
    }
}
