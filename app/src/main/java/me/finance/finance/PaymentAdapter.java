package me.finance.finance;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Payment;

public class PaymentAdapter extends ArrayAdapter<String>{


    private List<Payment> payments;

    public PaymentAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());
        payments = databaseHandler.getPayments();

        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add("All payment options");
        for (int i = 0; i < payments.size(); i++){
            spinnerArray.add(payments.get(i).getName());
        }
        this.addAll(spinnerArray);
    }

    public List<Payment> getPayments() {
        return payments;
    }
}
