package me.finance.finance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.finance.finance.Model.Sort;

public class SortAdapter extends ArrayAdapter<Sort>{


    public SortAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        List<Sort> spinnerArray =  new ArrayList<>();
        for (Sort.Column column : Sort.Column.values()) {
            for (Sort.Order order: Sort.Order.values()) {
                spinnerArray.add(new Sort(column, order));
            }
        }
        this.addAll(spinnerArray);

    }
}
