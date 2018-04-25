package me.finance.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;

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
     * Creation of tables for Database:
     * intakes (_id (int), value(float), date(date), name(text), comment(text), category(int), payment_opt(int))
     * permanents (_id(int), value(float), start_date(date), iteration(text), end_date(date), name(text), comment(text), category(int), payment_opt(int), next_exec(date))
     * categories (_id(int), name(text))
     * payment (_id(int), name(text))
     *
     */
    public void createTables() {
        database.execSQL("CREATE TABLE IF NOT EXISTS intakes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value FLOAT, " +
                "date DATE, " +
                "name TEXT," +
                "comment TEXT, " +
                "category INTEGER, " +
                "payment_opt INTEGER, " +
                "FOREIGN KEY(category) REFERENCES categories(_id), " +
                "FOREIGN KEY(payment_opt) REFERENCES payment(_id)" +
                ");");

        database.execSQL("CREATE TABLE IF NOT EXISTS permanents(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "value FLOAT, " +
                "start_date DATE, " +
                "iteration TEXT, " +
                "end_date DATE, " +
                "name TEXT, " +
                "comment TEXT, " +
                "category INTEGER, " +
                "payment_opt INTEGER, " +
                "next_exec DATE, " +
                "FOREIGN KEY(category) REFERENCES categories(_id), " +
                "FOREIGN KEY(payment_opt) REFERENCES payment(_id)" +
                ");");

        database.execSQL("CREATE TABLE IF NOT EXISTS categories(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT " +
                ");");

        database.execSQL("CREATE TABLE IF NOT EXISTS payment(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT " +
                ");");
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Category category = null;
        Cursor cursor = database.rawQuery("SELECT name FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            category.setId(cursor.getInt(0));
            category.setName(cursor.getString(1));
            categories.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    public List<Payment> getPayment() {
        List<Payment> payments = new ArrayList<>();
        Payment payment = null;
        Cursor cursor = database.rawQuery("SELECT name FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            payment.setId(cursor.getInt(0));
            payment.setName(cursor.getString(1));
            payments.add(payment);
            cursor.moveToNext();
        }
        cursor.close();
        return payments;
    }


    public void deleteTableContents(){
        database.delete("intakes", null, null);
        database.delete("permanents", null, null);
        database.delete("categories", null, null);
        database.delete("payment", null, null);
    }

    public boolean updateIntakes(Intake... intakes){
        for(Intake intake : intakes){
            ContentValues values = new ContentValues();
            values.put("value",intake.getValue());
            values.put("date",intake.getDate());
            values.put("name",intake.getName());
            values.put("comment",intake.getComment());
            //values.put("category",(Byte)null);//TODO replace
            //values.put("payment_opt",(Byte)null);//TODO replace

            if(database.update("intakes",values,"_id = ?", new String[]{String.valueOf(intake.getId())}) != 1){
                return false;
            }
        }
        return true;
    }


    public List<Intake> getIntakes()
    {
        List<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
        if(cursor.moveToFirst())
        {
       do {
            intakes.add(new Intake(cursor.getInt(1), cursor.getFloat(2), cursor.getString(3), cursor.getString(4), "Hallo"/*cursor.getString(5)*/));
            cursor.moveToNext();

        }while(cursor.moveToNext());
        }
        cursor.close();
        return intakes;
    }

    public void addIntake(Intake intake)
    {
        /*String sql = "INSERT INTO intakes (value, date, name, comment) VALUES (" + intake.getValue() + ", '" + intake.getDate() + "', '" +
                intake.getName() + "', '" + intake.getComment() + "');";
        database.execSQL(sql);*/

        SQLiteStatement sql = database.compileStatement("INSERT INTO intakes (_id, value, date, name, comment) VALUES (NULL, ?, ?, ?,? )");
        sql.bindDouble(1, intake.getValue());
        sql.bindString(2, intake.getDate());
        sql.bindString(3, intake.getName());
        sql.bindString(4, intake.getComment());
        sql.execute();

    }


    /*
     * ***TESTING***
     *
     * Inserts dummy values into test table, also shows how to query
     *
     * ***TESTING***
     */
    public void insertDummyValues() {
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (200.5, '2017-12-17', 'Felix Auf', 'Lohn');");
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (95.5, '2017-06-07', 'Harald Koinig', 'Biergeld');");
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (19.80, '2018-01-07', 'Harald Koinig', 'OEH Beitrag');");

        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (5.55, '2014-01-07', 'MONTHLY' , '2019-12-12', 'Ignazius Bierus', 'Alimente');");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (-9.35, '2015-05-17', 'WEEKLY' , '2019-01-02', 'Harald Koinig', 'Minus');");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (255.25, '2016-01-01', 'YEARLY' , '2019-06-01', 'Fitnessstudio', 'Beitrag');");

        database.execSQL("INSERT INTO payment (name) VALUES ('Bar');");

        database.execSQL("INSERT INTO categories (name) VALUES ('Bier');");


        List<String> intakes = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
         cursor.moveToFirst();
         while(!cursor.isAfterLast()){
             intakes.add(cursor.getString(4));
             cursor.moveToNext();
         }
         cursor.close();
         System.out.println("INTAKES " + intakes);

        List<String> permanents = new ArrayList<String>();
        Cursor cursor2 = database.rawQuery("SELECT * FROM permanents", null);
        cursor2.moveToFirst();
        while(!cursor2.isAfterLast()){
            permanents.add(cursor2.getString(6));
            cursor2.moveToNext();

        }
        cursor2.close();
        System.out.println("PERMANENTS " + permanents);

        List<String> category = new ArrayList<String>();
        Cursor cursor3 = database.rawQuery("SELECT * FROM categories", null);
        cursor3.moveToFirst();
        while(!cursor3.isAfterLast()){
            category.add(cursor3.getString(1));
            cursor3.moveToNext();

        }
        cursor3.close();
        System.out.println("CATEGORIES " + category);

        List<String> payment = new ArrayList<String>();
        Cursor cursor4 = database.rawQuery("SELECT * FROM payment", null);
        cursor4.moveToFirst();
        while(!cursor4.isAfterLast()){
            payment.add(cursor4.getString(1));
            cursor4.moveToNext();

        }
        cursor4.close();
        System.out.println("PAYMENT " + payment);
    }


}