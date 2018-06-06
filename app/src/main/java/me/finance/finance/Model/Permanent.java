package me.finance.finance.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static me.finance.finance.Utils.convertDate;

public class Permanent{

    private int id_;
    private double value_;
    private Date start_date_;
    private String iteration;
    private Date end_date_;
    private String name_;
    private String filePath_;
    private Integer category_;
    private Integer payment_opt_;
    private Date next_exec_;

    public Permanent(int id_, double value_, Date start_date_, String iteration, Date end_date_, String name_, String filePath_, Integer category_, Integer payment_opt_, Date next_exec_) {
        this.id_ = id_;
        this.value_ = value_;
        this.start_date_ = start_date_;
        this.iteration = iteration;
        this.end_date_ = end_date_;
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = next_exec_;
    }

    public Permanent(double value_, Date start_date_, String iteration, Date end_date_, String name_, String filePath_, Integer category_, Integer payment_opt_, Date next_exec_) {
        this.value_ = value_;
        this.start_date_ = start_date_;
        this.iteration = iteration;
        this.end_date_ = end_date_;
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = next_exec_;
    }

    public Permanent(int id_, double value_, String start_date_, String iteration, String end_date_, String name_, String filePath_, Integer category_, Integer payment_opt_, String next_exec_) {
        this.id_ = id_;
        this.value_ = value_;
        this.start_date_ = convertDate(start_date_);
        this.iteration = iteration;
        this.end_date_ = convertDate(end_date_);
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = convertDate(next_exec_);
    }

    public Permanent(double value_, String start_date_, String iteration, String end_date_, String name_, String filePath_, Integer category_, Integer payment_opt_, String next_exec_) {
        this.value_ = value_;
        this.start_date_ = convertDate(start_date_);
        this.iteration = iteration;
        this.end_date_ = convertDate(end_date_);
        this.name_ = name_;
        this.filePath_ = filePath_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = convertDate(next_exec_);
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

    public void setValue(double value_) {
        this.value_ = value_;
    }

    public Date getStartDate() {
        return start_date_;
    }

    public String getStartDateFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        return dateFormat.format(start_date_);
    }

    public void setStartDate(Date start_date_) {
        this.start_date_ = start_date_;
    }

    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }

    public Date getEndDate() {
        return end_date_;
    }

    public String getEndDateFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN);
        return dateFormat.format(end_date_);
    }

    public void setEndDate(Date end_date_) {
        this.end_date_ = end_date_;
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

    public Integer getPayment_opt(){
        return payment_opt_;
    }

    public void setPayment_opt(Integer payment_opt_){
        this.payment_opt_ = payment_opt_;
    }

    public Date  getNext_exec(){
        return next_exec_;
    }

    public void setNext_exec(Date next_exec_){
        this.next_exec_ = next_exec_;
    }
}
