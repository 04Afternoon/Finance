package me.finance.finance;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;

public class MonthAdapter extends BaseAdapter {
    private Context context; //context

    private List<Intake> items; //data source of the list adapter

    public MonthAdapter(Context context, List<Intake> items) {
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

        Intake currentItem = (Intake) getItem(i);

        TextView textViewItemName = convertView.findViewById(R.id.monthName);
        TextView textViewItemAmount = convertView.findViewById(R.id.monthAmount);
        TextView textViewItemDate = convertView.findViewById(R.id.monthDate);
        TextView textViewItemCategory = convertView.findViewById(R.id.monthCategory);
        TextView textViewItemPayment = convertView.findViewById(R.id.payment_textfield_months);
        TextView textViewItemImage = convertView.findViewById(R.id.image_textfield_month);

        textViewItemName.setText(currentItem.getName());

        double value = currentItem.getValue();
        if (value >= 0) {
            textViewItemAmount.setTextColor(Color.BLACK);
        } else {
            textViewItemAmount.setTextColor(Color.RED);
        }
        textViewItemAmount.setText(String.format(Locale.GERMAN, "%.2f€", value));
        textViewItemDate.setText(currentItem.getDateFormatted());
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
        convertView.setOnClickListener(view -> {
            if (currentItem.getComment() != null && !currentItem.getComment().isEmpty()) {

                Dialog settingsDialog = new Dialog(context);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                View v = LayoutInflater.from(context).inflate(R.layout.image_dialog, null);
                settingsDialog.setContentView(v);
                settingsDialog.show();
                ImageView view1 = v.findViewById(R.id.dialogReceipeImage);
                Bitmap myBitmap = BitmapFactory.decodeFile(currentItem.getComment());
                view1.setImageBitmap(myBitmap);
            }

        });

        if (currentItem.getComment() != null && !currentItem.getComment().isEmpty()) {
            textViewItemImage.setText("📷");
        }else{
            textViewItemImage.setText("");
        }

            return convertView;
    }

    public void setItems(List<Intake> items) {
        this.items = items;
    }

}
