package com.app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.model.AssociateModel;
import com.app.model.ContactModel;
import com.app.model.ContactModelNew;
import com.app.model.StoreListModel;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 83;
    public static final String DATABASE_NAME = "SQLiddstdddcdsdleDfvfawskwadhmdakfs4sdsccvtasdsdwaaessbasesde.db";

    public static final String TABLE_NAME = "ContactOriginal";                      //Table Name First
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "FIRST_NAME";
    public static final String COLUMN_PHONE_NUMBER = "CONTACT_NUMBER";


    public static final String TABLE_NAME_NEW = "ContactUpdated";                   //Table Name Second
    public static final String COLUMN_ID_NEW = "ID_NEW";
    public static final String COLUMN_NAME_NEW = "FIRST_NAME_NEW";
    public static final String COLUMN_PHONE_NUMBER_NEW = "CONTACT_NUMBER_NEW";
    public static final String ISEXISTED = "IS_EXISTED";


    public static final String TABLE_NAME_CATEGORY = "categorytask";                   //Table Name category
    public static final String COLUMN_ID_NEW_CATEGORY = "ID_NEW_CATEGORY";
    public static final String COLUMN_NAME_CATEGORY = "CATEGORY_NAME_NEW";
    public static final String IMAGE_NAME = "IMAGE";


    public static final String TABLE_NAME_ASSOCIATE = "AssociateUserDetail";                   //Table Name associate
    public static final String COLUMN_ID_NEW_ASSOCIATE = "COLUMN_ID_NEW_ASSOCIATE";
    public static final String COLUMN_NAME_ASSOCIATE = "COLUMN_NAME_ASSOCIATE";
    public static final String ASSOCIATE_PASSWORD = "ASSOCIATE_PASSWORD";




    private SQLiteDatabase database;

    private static SQLiteHelper sqLiteHelper = null;

    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SQLiteHelper getInstance(Context context) {
        if (sqLiteHelper == null)
            return new SQLiteHelper(context);
        else
            return sqLiteHelper;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " VARCHAR, " + COLUMN_PHONE_NUMBER + " VARCHAR);");
        db.execSQL("create table " + TABLE_NAME_NEW + " ( " + COLUMN_ID_NEW + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_NEW + " VARCHAR, " + COLUMN_PHONE_NUMBER_NEW + " VARCHAR, " + ISEXISTED + " INTEGER);");
        db.execSQL("create table " + TABLE_NAME_CATEGORY + " ( " + COLUMN_ID_NEW_CATEGORY + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_CATEGORY + " VARCHAR, " + IMAGE_NAME + " VARCHAR);");
        db.execSQL("create table " + TABLE_NAME_ASSOCIATE + " ( " + COLUMN_ID_NEW_ASSOCIATE + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_ASSOCIATE + " VARCHAR, " + ASSOCIATE_PASSWORD + " VARCHAR);");

        Log.i("Table Created", "");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ASSOCIATE);
        onCreate(db);
    }


    public long insertRecordOriginal(ContactModel contact) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, contact.getName());
        contentValues.put(COLUMN_PHONE_NUMBER, contact.getMobileNumber());
        long res = database.insert(TABLE_NAME, null, contentValues);
        Log.i("res", res + "");
        database.close();
        return res;
    }

    public long insertCategory(StoreListModel contact) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CATEGORY, contact.getStoreName());
        contentValues.put(IMAGE_NAME, contact.getStoreImage());
        long res = database.insert(TABLE_NAME_CATEGORY, null, contentValues);

        Log.i("res", res + "");
        database.close();
        return res;
    }

    public long insertAssociate(AssociateModel associateModel) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_ASSOCIATE, associateModel.getUserName());
        contentValues.put(ASSOCIATE_PASSWORD, associateModel.getPassword());
        long res = database.insert(TABLE_NAME_ASSOCIATE, null, contentValues);

        Log.i("res", res + "");
        database.close();
        return res;
    }



    public void deleteCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME_CATEGORY); //delete all rows in a table
        db.close();
    }


    public ArrayList<ContactModel> getAllRecordsOriginal() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ContactModel> contacts = new ArrayList<>();
        ContactModel contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ContactModel();
                contactModel.setId(cursor.getString(0));
                contactModel.setName(cursor.getString(1));
                contactModel.setMobileNumber(cursor.getString(2));
                contacts.add(contactModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public ArrayList<StoreListModel> getCategoryListing() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME_CATEGORY, null, null, null, null, null, null);
        ArrayList<StoreListModel> contacts = new ArrayList<>();
        StoreListModel storeListModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                storeListModel = new StoreListModel();
                //  storeListModel.setId(cursor.getString(0));
                storeListModel.setStoreName(cursor.getString(1));
                storeListModel.setStoreImage(cursor.getString(2));
                contacts.add(storeListModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }

    public ArrayList<AssociateModel> getAssociate() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME_ASSOCIATE, null, null, null, null, null, null);
        ArrayList<AssociateModel> contacts = new ArrayList<>();
        AssociateModel associateModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                associateModel= new AssociateModel();
                //  storeListModel.setId(cursor.getString(0));
                associateModel.setUserName(cursor.getString(1));
                associateModel.setPassword(cursor.getString(2));
                contacts.add(associateModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }


    public void insertRecordNew(ContactModelNew contact) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_NEW, contact.getName());
        contentValues.put(COLUMN_PHONE_NUMBER_NEW, contact.getPhonNum());
        contentValues.put(ISEXISTED, contact.isChecked);
        database.insert(TABLE_NAME_NEW, null, contentValues);
        database.close();
    }


    public ArrayList<ContactModelNew> getAllRecordNew() {
        database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME_NEW, null, null, null, null, null, null);
        ArrayList<ContactModelNew> contacts = new ArrayList<>();
        ContactModelNew contactModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                contactModel = new ContactModelNew();
                contactModel.setId(cursor.getString(0));
                contactModel.setName(cursor.getString(1));
                contactModel.setPhonNum(cursor.getString(2));
                contactModel.isExist = cursor.getInt(3);
                contacts.add(contactModel);
            }
        }
        cursor.close();
        database.close();
        return contacts;
    }


    public void clearDB() {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME); //delete all rows in a table
        db.execSQL("DELETE FROM " + TABLE_NAME_NEW); //delete all rows in a table
        db.execSQL("DELETE FROM " + TABLE_NAME_CATEGORY); //delete all rows in a table
        db.execSQL("DELETE FROM " + TABLE_NAME_ASSOCIATE); //delete all rows in a table
        db.close();
    }
}