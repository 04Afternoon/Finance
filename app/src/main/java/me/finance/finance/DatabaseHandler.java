package me.finance.finance;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHandler{

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseHandler instance;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finances.db";
    public static final String INTAKE_TABLE_NAME = "intakes";
    public static final String INTAKE_COLUMN_ID = "id";
    public static final String INTAKE_COLUMN_VALUE = "value";
    public static final String INTAKE_COLUMN_DATE = "date";
    public static final String INTAKE_COLUMN_NAME = "name";
    public static final String INTAKE_COLUMN_COMMENT = "comment";

    private DatabaseHandler(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static DatabaseHandler getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHandler(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void createTable() {
        database.execSQL("CREATE TABLE IF NOT EXISTS intakes (" +
                "_id INTEGER, " +
                "value FLOAT, " +
                "date DATE, " +
                "name TEXT," +
                "comment TEXT" +
                ");");
        System.out.println(":)))");
    }


}