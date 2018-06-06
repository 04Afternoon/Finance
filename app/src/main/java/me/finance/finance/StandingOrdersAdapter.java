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
import me.finance.finance.Model.Payment;
import me.finance.finance.Model.Permanent;

public class StandingOrdersAdapter extends BaseAdapter {
    private Context context; //context

    private List<Permanent> items; //data source of the list adapter

    public StandingOrdersAdapter(Context context, List<Permanent> items){
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
                    inflate(R.layout.standing_orders_list_layout, parent, false);
        }

        Permanent currentItem = (Permanent)getItem(i);

        TextView textViewItemName = convertView.findViewById(R.id.permsName);
        TextView textViewItemAmount = convertView.findViewById(R.id.permsAmount);
        TextView textViewItemStartDate = convertView.findViewById(R.id.permsStartDate);
        TextView textViewItemEndDate = convertView.findViewById(R.id.permsEndDate);
        TextView textViewItemCategory = convertView.findViewById(R.id.permsCategory);
        TextView textViewItemPayment = convertView.findViewById(R.id.payment_textfield_perms);

        textViewItemName.setText(currentItem.getName());

        double value = currentItem.getValue();
        if (value >= 0) {
            textViewItemAmount.setTextColor(Color.BLACK);
        } else {
            textViewItemAmount.setTextColor(Color.RED);
        }
        textViewItemAmount.setText(String.format(Locale.GERMAN,"%.2f€", value));
        textViewItemStartDate.setText(currentItem.getStartDateFormatted());
        textViewItemEndDate.setText(currentItem.getEndDateFormatted());
        Category category;
        if (currentItem.getCategory() == null) {
            category = null;
        } else {
            category = DatabaseHandler.getInstance(context).getCategory(currentItem.getCategory());
        }
        if (category != null && category.getName() != null) {
            textViewItemCategory.setText(category.getName());
        } else {
            textViewItemCategory.setText(R.string.no_category);
        }
        Payment payment = DatabaseHandler.getInstance(context).getPayment(currentItem.getPayment_opt());
        if (payment != null && payment.getName() != null) {
            textViewItemPayment.setText(payment.getName());
        } else {
            textViewItemPayment.setText(R.string.cash);
        }

        return convertView;
    }

    public List<Permanent> getItems() {
        return items;
    }

    public void setItems(List<Permanent> items) {
        this.items = items;
    }

}
