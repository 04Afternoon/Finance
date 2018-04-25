package me.finance.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;
import me.finance.finance.Model.Permanent;

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

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(new Category(cursor.getInt(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    public Category getCategory(int id) {
        Category category = null;
        Cursor cursor = database.rawQuery("SELECT * FROM categories WHERE _id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            category = new Category(cursor.getInt(0), cursor.getString(1));
        }
        return category;
    }

    public ArrayList<Payment> getPayments() {
        ArrayList<Payment> payments = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            payments.add(new Payment(cursor.getInt(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return payments;
    }

    public Payment getPayment(int id) {
        Payment payment = null;
        Cursor cursor = database.rawQuery("SELECT * FROM payment WHERE _id = ?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            payment = new Payment(cursor.getInt(0), cursor.getString(1));
        }
        return payment;
    }

    public void removeCategory(String name){
        database.delete("categories", "name = '" + name + "'", null);
    }

    public void removePayment(String name){
        database.delete("payment", "name = '" + name + "'", null);
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
            values.put("date",intake.getDateFormatted());
            values.put("name",intake.getName());
            values.put("comment",intake.getComment());
            values.put("category",intake.getCategory());
            values.put("payment_opt",intake.getPayment_opt());

            if(database.update("intakes",values,"_id = ?", new String[]{String.valueOf(intake.getId())}) != 1){
                return false;
            }
        }
        return true;
    }

    public boolean updateCategories(Category... categories){
        for(Category category : categories){
            ContentValues values = new ContentValues();
            values.put("name",category.getName());

            if(database.update("categories",values,"_id = ?", new String[]{String.valueOf(category.getId())}) != 1){
                return false;
            }
        }
        return true;
    }

    public boolean updatePayment(Payment... payments){
        for(Payment payment : payments){
            ContentValues values = new ContentValues();
            values.put("name",payment.getName());

            if(database.update("categories",values,"_id = ?", new String[]{String.valueOf(payment.getId())}) != 1){
                return false;
            }
        }
        return true;
    }

    public boolean updatePermanent(Permanent... permanents){
        for(Permanent permanent : permanents){
            ContentValues values = new ContentValues();
            values.put("value",permanent.getValue());
            values.put("start_date",permanent.getStartDate());
            values.put("iteration",permanent.getIteration());
            values.put("end_date",permanent.getEndDate());
            values.put("name",permanent.getName());
            values.put("comment",permanent.getComment());
            values.put("category",permanent.getCategory());
            values.put("payment_opt",permanent.getPayment_opt());
            values.put("next_exec",permanent.getNext_exec());

            if(database.update("categories",values,"_id = ?", new String[]{String.valueOf(permanent.getId())}) != 1){
                return false;
            }
        }
        return true;
    }




    public Intake getIntake(int id)
    {
        Intake intake = null;
        Cursor cursor = database.rawQuery("SELECT * FROM intakes WHERE _id = ?", new String[]{String.valueOf(id)});
        if(cursor.moveToFirst())
        {
            intake =  new Intake(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6));
        }
        cursor.close();
        return intake;
    }

    public ArrayList<Intake> getPositiveIntakes()
    {
        ArrayList<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
        if(cursor.moveToFirst())
        {
            do {
                if(cursor.getFloat(1) > 0)
                  intakes.add(new Intake(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        return intakes;
    }

    public ArrayList<Intake> getNegativeIntakes()
    {
        ArrayList<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
        if(cursor.moveToFirst())
        {
            do {
                if(cursor.getFloat(1) < 0)
                    intakes.add(new Intake(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));

            }while(cursor.moveToNext());
        }
        cursor.close();
        return intakes;
    }

    public ArrayList<Intake> getIntakes()
    {
        ArrayList<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes", null);
        if(cursor.moveToFirst())
        {
          do {
            intakes.add(new Intake(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));

          }while(cursor.moveToNext());
        }
        cursor.close();
        return intakes;
    }

    public long addIntake(Intake intake)
    {
        SQLiteStatement sql = database.compileStatement("INSERT INTO intakes (_id, value, date, name, comment, category, payment_opt) VALUES (NULL, ?, ?, ?,?,?,? )");
        sql.bindDouble(1, intake.getValue());
        sql.bindString(2, intake.getDateFormatted());
        sql.bindString(3, intake.getName());
        sql.bindString(4, intake.getComment());
        sql.bindLong(5,intake.getCategory());
        sql.bindLong(6, intake.getPayment_opt());
        long id = sql.executeInsert();

        return id;
    }

    public long addPayment(Payment payment)
    {
        SQLiteStatement sql = database.compileStatement("INSERT INTO payment (_id, name) VALUES (NULL, ?)");
        sql.bindString(1,payment.getName());
        long id = sql.executeInsert();

        return id;
    }

    public long addPermanet(Permanent permanent)
    {
        SQLiteStatement sql = database.compileStatement("INSERT INTO permanents (_id, value, start_date, iteration, end_date, name, comment, category, payment_opt, next_exec) " +
                "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        sql.bindDouble(1, permanent.getValue());
        sql.bindString(2,permanent.getStartDate());
        sql.bindString(3,permanent.getIteration());
        sql.bindString(4,permanent.getEndDate());
        sql.bindString(5,permanent.getName());
        sql.bindString(6,permanent.getComment());
        sql.bindLong(7,permanent.getCategory());
        sql.bindLong(8,permanent.getPayment_opt());
        sql.bindString(9,permanent.getNext_exec());

        long id = sql.executeInsert();

        return id;
    }

    public long addCategory(Category category)
    {
        SQLiteStatement sql = database.compileStatement("INSERT INTO categories (_id, name) VALUES (NULL, ?)");
        sql.bindString(1, category.getName());
        long id = sql.executeInsert();

        return id;
    }


    //  ;)
    public void addCategoryBetter(String name_)
    {
        String sql = "INSERT INTO categories (name) VALUES (\"" + name_+ "\");";
        database.execSQL(sql);
    }

    //  ;)
    public void addPaymentBetter(String name_)
    {
        String sql = "INSERT INTO payment (name) VALUES (\"" + name_+ "\");";
        database.execSQL(sql);
    }


    /*
     * ***TESTING***
     *
     * Inserts dummy values into test table, also shows how to query
     *
     * ***TESTING***
     */
    public void insertDummyValues() {
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (-200.5, '2017-12-17', 'Felix Auf', 'Lohn');");
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (95.5, '2017-06-07', 'Harald Koinig', 'Biergeld');");
        database.execSQL("INSERT INTO intakes (value, date, name, comment) VALUES (19.80, '2018-01-07', 'Harald Koinig', 'OEH Beitrag');");

        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (5.55, '2014-01-07', 'MONTHLY' , '2019-12-12', 'Ignazius Bierus', 'Alimente');");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (-9.35, '2015-05-17', 'WEEKLY' , '2019-01-02', 'Harald Koinig', 'Minus');");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment) VALUES (255.25, '2016-01-01', 'YEARLY' , '2019-06-01', 'Fitnessstudio', 'Beitrag');");

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