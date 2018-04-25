package me.finance.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

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
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_layout, parent, false);
        }

        // get current item to be displayed
        Intake currentItem = (Intake)getItem(i);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.zweck);
        TextView textViewItemDescription = (TextView)
                convertView.findViewById(R.id.int_or_val);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getName());
        textViewItemDescription.setText(String.format(Locale.GERMAN,"%.2f€", currentItem.getValue()));

        // returns the view for the current row
        return convertView;
    }
}
