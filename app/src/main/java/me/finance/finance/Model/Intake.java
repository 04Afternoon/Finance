package me.finance.finance.Model;

import java.sql.Date;

public class Intake {

    private int id_;
    private double value_;
    private String date_;
    private String name_;
    private String comment_;
    private int category_;
    private int payment_opt_;

/*
    public Intake(int id_, double value_, String date_, String name_, String comment_, int category_, int payment_opt_) {
        this.id_ = id_;
        this.value_ = value_;
        this.date_ = date_;
        this.name_ = name_;
        this.comment_ = comment_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
    }
    public Intake(double value_, String date_, String name_, String comment_) {
        this.value_ = value_;
        this.date_ = date_;
        this.name_ = name_;
        this.comment_ = comment_;
    }
*/
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

    public String getDate() {
        return date_;
    }

    public void setDate(String date_) {
        this.date_ = date_;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name_) {
        this.name_ = name_;
    }

    public String getComment() {
        return comment_;
    }

    public void setComment(String comment_) {
        this.comment_ = comment_;
    }

    public int getCategory(){
        return category_;
    }

    public void setCategory(int category_){
        this.category_ = category_;
    }

    public int getPayment_opt(){
        return payment_opt_;
    }

    public void setPayment_opt(int payment_opt_){
        this.payment_opt_ = payment_opt_;
    }
}
