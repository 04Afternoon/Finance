package me.finance.finance.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Intake {

    private int id_;
    private double value_;
    private Date date_;
    private String name_;
    private String filePath_;
    private Integer category_;
    private int payment_opt_;

    public Intake(int id_, double value_, String date_, String name_, String filePath_, Integer category_, int payment_opt_) {
        this(value_,date_,name_, filePath_,category_,payment_opt_);
        this.id_ = id_;
    }

    public Intake(double value_, String date_, String name_, String filePath_, Integer category_, int payment_opt_) {
        this.value_ = value_;
        this.setDateFormatted(date_);
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
    }

    public Intake(int id_, double value_, Date date_, String name_, String filePath_, Integer category_, int payment_opt_) {
        this(value_,date_,name_, filePath_,category_,payment_opt_);
        this.id_ = id_;
    }

    public Intake(double value_, Date date_, String name_, String filePath_, Integer category_, int payment_opt_) {
        this.value_ = value_;
        this.date_ = date_;
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
    }

    public int getId() {
        return id_;
    }

    public void setId(int id_){
        this.id_ = id_;
    }

    public double getValue() {
        return value_;
    }

    public void setValue(float value_) {
        this.value_ = value_;
    }

    public Date getDate() {
        return date_;
    }

    public String getDateFormatted() {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
         return dateFormat.format(date_);
    }

    public void setDate(Date date_) {
        this.date_ = date_;
    }

    public void setDateFormatted(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        try {
            date_ = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name_;
    }

    public void setName(String name_) {
        this.name_ = name_;
    }

    public String getComment() {
        return filePath_;
    }

    public void setComment(String comment_) {
        this.filePath_ = comment_;
    }

    public Integer getCategory(){
        return category_;
    }

    public void setCategory(Integer category_){
        this.category_ = category_;
    }

    public int getPayment_opt(){
        return payment_opt_;
    }

    public void setPayment_opt(int payment_opt_){
        this.payment_opt_ = payment_opt_;
    }
}
