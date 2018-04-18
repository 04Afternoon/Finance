package me.finance.finance.Model;

import java.sql.Date;

public class Intake {

    private int id_;
    private double value_;
    private String date_;
    private String name_;
    private String comment_;

    public Intake(int id_, double value_, String date_, String name_, String comment_) {
        this.id_ = id_;
        this.value_ = value_;
        this.date_ = date_;
        this.name_ = name_;
        this.comment_ = comment_;
    }

    public Intake(double value_, String date_, String name_, String comment_) {
        this.value_ = value_;
        this.date_ = date_;
        this.name_ = name_;
        this.comment_ = comment_;
    }

    public int getId() {
        return id_;
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
}
