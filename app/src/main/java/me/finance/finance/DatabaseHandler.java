package me.finance.finance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;
import me.finance.finance.Model.Permanent;
import me.finance.finance.Model.Sort;

import static me.finance.finance.Utils.convertDate;

public class DatabaseHandler{

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseHandler instance;

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
                "value DOUBLE, " +
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
                "value DOUBLE, " +
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

        database.execSQL("INSERT INTO payment (name)\n" +
                "SELECT * FROM (SELECT 'Cash') AS tmp\n" +
                "WHERE NOT EXISTS (\n" +
                "    SELECT name FROM payment WHERE name = 'Cash'\n" +
                ") LIMIT 1;");
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(new Category(cursor.getInt(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return categories;
    }

    public ArrayList<Category> getCategories(String name) {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM categories WHERE name = ?", new String[] {name});
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
        cursor.close();
        return category;
    }

    public Category getCategory(String name) {
        Category category = null;
        Cursor cursor = database.rawQuery("SELECT * FROM categories WHERE name = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            category = new Category(cursor.getInt(0), cursor.getString(1));
        }
        cursor.close();
        return category;
    }

    public Payment getPayment(String name) {
        Payment payment = null;
        Cursor cursor = database.rawQuery("SELECT * FROM payment WHERE name = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            payment = new Payment(cursor.getInt(0), cursor.getString(1));
        }
        cursor.close();
        return payment;
    }


    public ArrayList<Payment> getPayments() {
        ArrayList<Payment> payments = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM payment", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            payments.add(new Payment(cursor.getInt(0),cursor.getString(1)));
            cursor.moveToNext();
        }
        cursor.close();
        return payments;
    }

    public ArrayList<Payment> getPayments(String name) {
        ArrayList<Payment> payments = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM payment WHERE name = ?", new String[] {name});
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
        cursor.close();
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

    public void deleteIntakes() {
        database.delete("intakes", null, null);
    }

    public void deletePermanents() {
        database.delete("permanents", null, null);
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

    public boolean updatePayment(String new_name, String old_name){
        ContentValues values = new ContentValues();
        values.put("name", new_name);
        database.update("payment", values, "name = ?", new String[] { old_name });
        return true;
    }

    public boolean updateCategories(String new_name, String old_name){
        ContentValues values = new ContentValues();
        values.put("name", new_name);
        database.update("categories", values, "name = ?", new String[] { old_name });
        return true;
    }

    public boolean updatePermanent(Permanent... permanents){
        for(Permanent permanent : permanents){
            ContentValues values = new ContentValues();
            values.put("value",permanent.getValue());
            values.put("start_date",convertDate(permanent.getStartDate()));
            values.put("iteration",permanent.getIteration());
            values.put("end_date",convertDate(permanent.getEndDate()));
            values.put("name",permanent.getName());
            values.put("comment",permanent.getComment());
            values.put("category",permanent.getCategory());
            values.put("payment_opt",permanent.getPayment_opt());
            values.put("next_exec",convertDate(permanent.getNext_exec()));

            if(database.update("permanents",values,"_id = ?", new String[]{String.valueOf(permanent.getId())}) != 1){
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
            if (cursor.isNull(5)) {
                intake = new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), null, cursor.getInt(6));
            } else {
                intake = new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6));
            }
        }
        cursor.close();
        return intake;
    }

    public List<Intake> getIntakes(Payment payment_opt, Date startDate, Date endDate)
    {
        List<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes WHERE date BETWEEN ? AND ? AND payment_opt = ?", new String[]{convertDate(startDate), convertDate(endDate), String.valueOf(payment_opt.getId())});
        while (cursor.moveToNext()) {
            if (cursor.isNull(5)) {
                intakes.add(new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), null, cursor.getInt(6)));
            } else {
                intakes.add(new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
            }
        }
        cursor.close();
        return intakes;
    }

    public List<Intake> getIntakes(Category category, Date startDate, Date endDate)
    {
        List<Intake> intakes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM intakes WHERE date BETWEEN ? AND ? AND  category = ?", new String[]{convertDate(startDate), convertDate(endDate), String.valueOf(category.getId())});
        while (cursor.moveToNext()) {
            if (cursor.isNull(5)) {
                intakes.add(new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), null, cursor.getInt(6)));
            } else {
                intakes.add(new Intake(cursor.getInt(0), cursor.getDouble(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6)));
            }
        }
        cursor.close();
        return intakes;
    }

    public ArrayList<Intake> getIntakes()
    {
        ArrayList<Intake> intakes = new ArrayList<>();
        try {
            Cursor cursorIntakes = database.rawQuery("SELECT * FROM intakes", null);
            if (cursorIntakes.moveToFirst()) {
                do {
                    if (cursorIntakes.isNull(5)) {
                        intakes.add(new Intake(cursorIntakes.getInt(0), cursorIntakes.getDouble(1), cursorIntakes.getString(2), cursorIntakes.getString(3),
                                cursorIntakes.getString(4), null, cursorIntakes.getInt(6)));
                    } else {
                        intakes.add(new Intake(cursorIntakes.getInt(0), cursorIntakes.getDouble(1), cursorIntakes.getString(2), cursorIntakes.getString(3),
                                cursorIntakes.getString(4), cursorIntakes.getInt(5), cursorIntakes.getInt(6)));
                    }
                } while (cursorIntakes.moveToNext());
            }
            cursorIntakes.close();
        } catch(Exception e) {
            System.out.println("");
        }
        return intakes;
    }

    public long addIntake(Intake intake)
    {
        SQLiteStatement sql = database.compileStatement("INSERT INTO intakes (_id, value, date, name, comment, category, payment_opt) VALUES (NULL, ?, ?, ?,?,?,? )");
        sql.bindDouble(1, intake.getValue());
        sql.bindString(2, intake.getDateFormatted());
        sql.bindString(3, intake.getName());
        sql.bindString(4, intake.getComment());
        if (intake.getCategory() == null) {
            sql.bindNull(5);
        } else {
            sql.bindLong(5, intake.getCategory());
        }
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
        sql.bindString(2,convertDate(permanent.getStartDate()));
        sql.bindString(3,permanent.getIteration());
        sql.bindString(4,convertDate(permanent.getEndDate()));
        sql.bindString(5,permanent.getName());
        sql.bindString(6,permanent.getComment());
        if (permanent.getCategory() == null) {
            sql.bindNull(7);
        } else {
            sql.bindLong(7, permanent.getCategory());
        }
        if (permanent.getPayment_opt() == null) {
            sql.bindNull(8);
        } else {
            sql.bindLong(8, permanent.getPayment_opt());
        }
        sql.bindString(9,convertDate(permanent.getNext_exec()));

        long id = sql.executeInsert();

        return id;
    }

    public void removeStandingOrder(String name)
    {
      database.delete("permanents", "name = '" + name + "'", null);
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



        database.execSQL("INSERT INTO payment (name) VALUES ('Cash')");
        database.execSQL("INSERT INTO payment (name) VALUES ('Visa')");
        database.execSQL("INSERT INTO payment (name) VALUES ('Mastercard')");
        database.execSQL("INSERT INTO payment (name) VALUES ('American Express')");
        database.execSQL("INSERT INTO payment (name) VALUES ('Apple Pay')");
        database.execSQL("INSERT INTO payment (name) VALUES ('PayPal')");
        database.execSQL("INSERT INTO payment (name) VALUES ('VPay')");

        int cash_id = getPayments("Cash").get(0).getId();
        int visa_id = getPayments("Visa").get(0).getId();
        int mastercard_id = getPayments("Mastercard").get(0).getId();
        int american_id = getPayments("American Express").get(0).getId();
        int apple_id = getPayments("Apple Pay").get(0).getId();
        int paypal_id = getPayments("PayPal").get(0).getId();
        int Vpay_id = getPayments("PayPal").get(0).getId();


        database.execSQL("INSERT INTO categories (name) VALUES ('Home')");
        database.execSQL("INSERT INTO categories (name) VALUES ('Pet')");
        database.execSQL("INSERT INTO categories (name) VALUES ('Foods')");
        database.execSQL("INSERT INTO categories (name) VALUES ('Car')");
        database.execSQL("INSERT INTO categories (name) VALUES ('Books')");


        int home_id = getCategories("Home").get(0).getId();
        int pet_id = getCategories("Pet").get(0).getId();
        int foods_id = getCategories("Foods").get(0).getId();
        int car_id = getCategories("Car").get(0).getId();


        //database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (-200.5, '2018-05-30', 'Felix Auf', 'Lohn' , " + home_id + "," + visa_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (-95.5, '2018-05-30', 'Harald Koinig', '' , " + pet_id + "," + cash_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (95.5, '2018-06-01', 'Harald Koinig', '' , " + pet_id + "," + cash_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (19.80, '2018-06-02', 'Harald Koinig', ''," + car_id + "," + american_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (15.5, '2018-06-01', '1', '' , " + home_id + "," + visa_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (99.90, '2018-06-02', '2', ''," + foods_id + "," + apple_id + ");");

        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (5.51, '2018-06-01', '3', '' , " + home_id + "," + visa_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (399.99, '2018-06-02', '4', ''," + car_id + "," + american_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (101.05, '2018-06-01', '5', '' , " + foods_id + "," + apple_id + ");");
        database.execSQL("INSERT INTO intakes (value, date, name, comment, category, payment_opt) VALUES (33.10, '2018-06-02', '6', ''," + car_id + "," + paypal_id + ");");


        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment, category, payment_opt) " +
                "VALUES (5.55, '2014-01-07', 'MONTHLY' , '2019-12-12', 'Ignazius Bierus', 'Alimente', " + home_id + "," + visa_id + ");");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment, category, payment_opt) " +
                "VALUES (-9.35, '2015-05-17', 'WEEKLY' , '2019-01-02', 'Harald Koinig', 'Minus', " + foods_id + "," + american_id + ");");
        database.execSQL("INSERT INTO permanents (value, start_date, iteration, end_date, name, comment, category, payment_opt) " +
                "VALUES (255.25, '2016-01-01', 'YEARLY' , '2019-06-01', 'Fitnessstudio', 'Beitrag', " + car_id + "," + mastercard_id + ");");

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


    public List<Intake> getIntakes(Date startDate, Date endDate, Sort sort, Category category, Payment payment, double valueFrom, double valueTo) {

        ArrayList<Intake> intakes = new ArrayList<>();
        try {
            List<String> selectionArgs = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();


            selectionArgs.add(Utils.convertDate(startDate));
            selectionArgs.add(Utils.convertDate(endDate));
            stringBuilder.append("SELECT * FROM intakes WHERE date BETWEEN ? AND ?");

            if(category != null){
                selectionArgs.add(Integer.toString(category.getId()));
                stringBuilder.append(" AND category = ?");
            }

            if(payment != null){
                selectionArgs.add(Integer.toString(payment.getId()));
                stringBuilder.append(" AND payment_opt = ?");
            }

            if(!Double.isNaN(valueFrom)){
                selectionArgs.add(String.format("%.2f", valueFrom));
                stringBuilder.append(" AND value >= ?");
            }

            if(!Double.isNaN(valueTo)){
                selectionArgs.add(String.format("%.2f", valueTo));
                stringBuilder.append(" AND value <= ?");
            }




            stringBuilder.append(String.format(" ORDER BY %s %s",sort.getColumn().getDatabasename(),sort.getOrder().getDatabasename()));



            Cursor cursorIntakes = database.rawQuery(stringBuilder.toString(), selectionArgs.toArray(new String[0]));
            if (cursorIntakes.moveToFirst()) {
                do {
                    intakes.add(new Intake(cursorIntakes.getInt(0), cursorIntakes.getDouble(1), cursorIntakes.getString(2), cursorIntakes.getString(3),
                            cursorIntakes.getString(4), cursorIntakes.getInt(5), cursorIntakes.getInt(6)));
                } while (cursorIntakes.moveToNext());
            }
            cursorIntakes.close();
        } catch(Exception e) {
            System.out.println("");
        }
        return intakes;
    }

    public List<Intake> getIntakes(Date startDate, Date endDate, Sort sort, Category category, Payment payment) {
        return getIntakes(startDate,endDate,sort,category,payment,Double.NaN,Double.NaN);

    }

    public List<Intake> getIntakes(Date startDate, Date endDate, Sort sort, Category category) {
        return getIntakes(startDate,endDate,sort,category,null);
    }

    public List<Intake> getIntakes(Date startDate, Date endDate, Sort sort) {
        return getIntakes(startDate,endDate,sort,null);
    }

    public List<Intake> getIntakes(Date startDate, Date endDate) {
        return getIntakes(startDate,endDate,new Sort(Sort.Column.DATE,Sort.Order.ASC));
    }

    public List<Permanent> getPermanents() {
        ArrayList<Permanent> permanents = new ArrayList<>();
        String[] columns = new String[] {
                "_id", "value", "start_date", "iteration", "end_date",
                "name", "comment", "category", "payment_opt", "next_exec"
        };

        try {
            Cursor cursor = database.query("permanents", columns, "", null, "", "", "start_date");
            if (cursor.moveToFirst()) {
                do {
                    permanents.add(new Permanent(cursor.getInt(0),
                            cursor.getDouble(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6),
                            cursor.isNull(7) ? null : cursor.getInt(7),
                            cursor.isNull(8) ? null : cursor.getInt(8),
                            cursor.getString(9)));
                } while(cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return permanents;
    }

    public List<Permanent> getDuePermanents() {
        ArrayList<Permanent> permanents = new ArrayList<>();
        String[] columns = new String[] {
                "_id", "value", "start_date", "iteration", "end_date",
                "name", "comment", "category", "payment_opt", "next_exec"
        };

        try {
            Cursor cursor = database.query("permanents", columns, "next_exec < date('now') and end_date > date('now')", null, "", "", "start_date");
            if (cursor.moveToFirst()) {
                do {
                    permanents.add(new Permanent(cursor.getInt(0),
                            cursor.getDouble(1), cursor.getString(2),
                            cursor.getString(3), cursor.getString(4),
                            cursor.getString(5), cursor.getString(6),
                            cursor.isNull(7) ? null : cursor.getInt(7),
                            cursor.isNull(8) ? null : cursor.getInt(8),
                            cursor.getString(9)));
                } while(cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return permanents;
    }


}