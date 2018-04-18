package me.finance.finance.Model;

public class Permanent{
    private int id_;
    private float value_;
    private String start_date_;
    private String iteration;
    private String end_date_;
    private String name_;
    private String comment_;

    public int getId() {
        return id_;
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
}
