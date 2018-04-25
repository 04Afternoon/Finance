package me.finance.finance.Model;

public class Permanent{

    private int id_;
    private float value_;
    private String start_date_;
    private String iteration;
    private String end_date_;
    private String name_;
    private String comment_;
    private int category_;
    private int payment_opt_;
    private String next_exec_;

    public Permanent(int id_, float value_, String start_date_, String iteration, String end_date_, String name_, String comment_, int category_, int payment_opt_, String next_exec_) {
        this.id_ = id_;
        this.value_ = value_;
        this.start_date_ = start_date_;
        this.iteration = iteration;
        this.end_date_ = end_date_;
        this.name_ = name_;
        this.comment_ = comment_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = next_exec_;
    }

    public Permanent(float value_, String start_date_, String iteration, String end_date_, String name_, String comment_, int category_, int payment_opt_, String next_exec_) {
        this.value_ = value_;
        this.start_date_ = start_date_;
        this.iteration = iteration;
        this.end_date_ = end_date_;
        this.name_ = name_;
        this.comment_ = comment_;
        this.category_ = category_;
        this.payment_opt_ = payment_opt_;
        this.next_exec_ = next_exec_;
    }


    public int getId() {
        return id_;
    }

    public void setId(int id_){
        this.id_ = id_;
    }

    public float getValue() {
        return value_;
    }

    public void setValue(float value_) {
        this.value_ = value_;
    }

    public String getStartDate() {
        return start_date_;
    }

    public void setStartDate(String start_date_) {
        this.start_date_ = start_date_;
    }

    public String getIteration() {
        return iteration;
    }

    public void setIteration(String iteration) {
        this.iteration = iteration;
    }

    public String getEndDate() {
        return end_date_;
    }

    public void setEndDate(String end_date_) {
        this.end_date_ = end_date_;
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

    public String  getNext_ecex(){
        return next_exec_;
    }

    public void setNext_exec(String next_exec_){
        this.next_exec_ = next_exec_;
    }
}
