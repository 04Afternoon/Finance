package me.finance.finance;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler{

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseHandler instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finances.db";

    private DatabaseHandler(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

     /*
     *
     * Establishes connection to database
     *
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /*
    *
    * Closes connection if exists
    *
    */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

     /*
     *
     * Hard coded creation of a table in database, just for testing purposes
     *
     */
    public void createTables() {
        database.execSQL("CREATE TABLE IF NOT EXISTS intakes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value FLOAT, " +
                "date TEXT, " +
                "name TEXT," +
                "comment TEXT," +
                "PRIMARY KEY(_id)" +
                ");");
        database.execSQL("CREATE TABLE IF NOT EXISTS permanents(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value FLOAT, " +
                "date TEXT, " +
                "name TEXT, " +
                "comment TEXT, " +
                "PRIMARY KEY(_id)" +
                ");");
        System.out.println(":)))");
    }

     /*
     *
     * Inserts dummy values into test table, also shows how to query
     *
     */
    public void insertTable() {
        database.execSQL("INSERT INTO intakes VALUES (1, 2, 'TEST', 'TEST', 'TEST');");
        List<String> name = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
         cursor.moveToFirst();
         while(!cursor.isAfterLast()){
             name.add(cursor.getString(3));
             cursor.moveToNext();
         }
         cursor.close();
         System.out.println(name);
    }


}