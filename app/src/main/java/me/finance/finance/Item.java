package me.finance.finance;

public class Item {
    private String itemZweck;
    private String itemIntOrVal;

    public Item(String zweck, String intervall_value) {
        this.itemZweck = zweck;
        this.itemIntOrVal = intervall_value;
    }

    public String getItemZweck() {
        return this.itemZweck;
    }

    public String getItemIntOrVal() {
        return itemIntOrVal;
    }
}