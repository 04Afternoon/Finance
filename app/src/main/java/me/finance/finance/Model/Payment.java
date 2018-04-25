package me.finance.finance.Model;

public class Payment {
    private int id_;
    private String name_;

    public Payment(int id_, String name_) {
        this.id_ = id_;
        this.name_ = name_;
    }

    public Payment(String name_) {
        this.name_ = name_;
    }

    public int getId(){
        return id_;
    }

    public void setId(int id_){
        this.id_ = id_;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name_) {
        this.name_ = name_;
    }
}
