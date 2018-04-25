package me.finance.finance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;

public class MonthAdapter extends BaseAdapter {
    private Context context; //context
    private List<Intake> items; //data source of the list adapter

    public MonthAdapter(Context context, List<Intake> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.month_list_layout, parent, false);
        }

        Intake currentItem = (Intake)getItem(i);

        TextView textViewItemName = convertView.findViewById(R.id.monthName);
        TextView textViewItemAmount = convertView.findViewById(R.id.monthAmount);
        TextView textViewItemDate = convertView.findViewById(R.id.monthDate);
        TextView textViewItemCategory = convertView.findViewById(R.id.monthCategory);

        textViewItemName.setText(currentItem.getName());

        double value = currentItem.getValue();
        if (value >= 0) {
            textViewItemAmount.setTextColor(Color.BLACK);
        } else {
            textViewItemAmount.setTextColor(Color.RED);
        }
        textViewItemAmount.setText(String.format(Locale.GERMAN,"%.2f€", value));
        textViewItemDate.setText(currentItem.getDate());
        Category category  = DatabaseHandler.getInstance(context).getCategory(currentItem.getCategory());
        if (category != null && category.getName() != null) {
            textViewItemCategory.setText(category.getName());
        } else {
            textViewItemCategory.setText(R.string.no_category);
        }

        return convertView;
    }
}
