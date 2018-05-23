package me.finance.finance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Sort;

public class CategoryAdapter extends ArrayAdapter<String>{


    private List<Category> categories;

    public CategoryAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());
        categories = databaseHandler.getCategories();

        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("All");
        for (int i = 0; i < categories.size(); i++){
            spinnerArray.add(categories.get(i).getName());
        }
        this.addAll(spinnerArray);
    }

    public List<Category> getCategories() {
        return categories;
    }
}
