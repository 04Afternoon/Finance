package me.finance.finance;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;
import me.finance.finance.Model.Permanent;

public class InOutPermsActivity extends AppCompatActivity {

    private ImageButton exitButton;
    private Button finishButton;
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    private Spinner categorySpinner, paymentSpinner;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_perms);
        setTitle(getIntent().getStringExtra("type"));

        getSupportActionBar().hide();

        myToolbar = (Toolbar) findViewById(R.id.toolbar_inoutperms);

        final EditText end_date_text_field = findViewById(R.id.end_text_field);
        final EditText intervall_text_field = findViewById(R.id.intervall_text_field);

        final ArrayList<Category> categoryList;
        ArrayList<String> categorySpinnerList = new ArrayList<String>();
        categoryList = databaseHandler.getCategories();
        categorySpinnerList.add("no category");
        for(int i = 0; i < categoryList.size(); i++)
        {
            categorySpinnerList.add(categoryList.get(i).getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorySpinnerList);
        categorySpinner = findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(categoryAdapter);

        final ArrayList<Payment> paymentList;
        ArrayList<String> paymentSpinnerList = new ArrayList<String>();
        paymentList = databaseHandler.getPayments();
        paymentSpinnerList.add("Cash");
        for(int i = 0; i < paymentList.size(); i++)
        {
            paymentSpinnerList.add(paymentList.get(i).getName());
        }
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentSpinnerList);
        paymentSpinner = findViewById(R.id.payment_opt_spinner);
        paymentSpinner.setAdapter(paymentAdapter);


        if(getIntent().getStringExtra("type").equals("Expense") || getIntent().getStringExtra("type").equals("Intake")) {
            end_date_text_field.setFocusable(false);
            end_date_text_field.setClickable(true);
            intervall_text_field.setClickable(true);
            intervall_text_field.setFocusable(false);
        }

        exitButton = findViewById(R.id.auftrag_exit_button);
        FragmentBalance.buttonEffect(exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        finishButton = findViewById(R.id.auftrag_finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name_text_field = findViewById(R.id.name_text_field);
                EditText value_text_field = findViewById(R.id.value_text_field);
                EditText start_date_text_field = findViewById(R.id.start_text_field);

                String name = name_text_field.getText().toString();
                String value_string = value_text_field.getText().toString();
                String startDate = start_date_text_field.getText().toString();
                String endDate = end_date_text_field.getText().toString();
                String intervall = intervall_text_field.getText().toString();

                String error = "";

                double value = 0;
                if (!value_string.isEmpty()) {
                    value = Double.valueOf(value_string);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (!startDate.isEmpty()) {
                    try {
                        dateFormat.parse(startDate);
                    } catch(ParseException e) {
                        error += "Invalid startDate";
                    }
                }
                if (!endDate.isEmpty()) {
                    try {
                        dateFormat.parse(endDate);
                    } catch(ParseException e) {
                        if (!error.isEmpty()) {
                            error += "\n";
                        }
                        error += "Invalid endDate";
                    }
                }

                if (error.isEmpty()) {
                    DatabaseHandler database = DatabaseHandler.getInstance(getApplicationContext());
                    //database.open();
                    if (!getIntent().getStringExtra("type").equals("Permanents") && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty() && intervall.isEmpty() && endDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Transaction saved", Toast.LENGTH_SHORT);
                        if (getIntent().getStringExtra("type").equals("Expense")) {
                            value *= -1;
                        }
                        String category = categorySpinner.getSelectedItem().toString();
                        Integer categoryId = 0;
                        for(int i = 0; i < categoryList.size(); i++){
                            if(categoryList.get(i).getName().equals(category)){
                                categoryId = categoryList.get(i).getId();
                                break;
                            }
                        }

                        String payment = paymentSpinner.getSelectedItem().toString();
                        Integer paymentId = 0;
                        for(int i = 0; i < paymentList.size(); i++){
                            if(paymentList.get(i).getName().equals(payment)){
                                paymentId = paymentList.get(i).getId();
                                break;
                            }
                        }

                        database.addIntake(new Intake(value, startDate, name, "", categoryId, paymentId));
                        toast.show();
                        System.out.println("DEBUG: !Once! " + name + " " + value + " " + startDate + " " + categoryId + " " + "ONCE" + " " + paymentId);
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else if (getIntent().getStringExtra("type").equals("Permanents") && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty() && !intervall.isEmpty() && !endDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "TODO -> push to DB", Toast.LENGTH_SHORT);

                        String category = categorySpinner.getSelectedItem().toString();
                        Integer categoryId = 0;
                        for(int i = 0; i < categoryList.size(); i++){
                            if(categoryList.get(i).getName().equals(category)){
                                categoryId = categoryList.get(i).getId();
                                break;
                            }
                        }

                        String payment = paymentSpinner.getSelectedItem().toString();
                        Integer paymentId = 0;
                        for(int i = 0; i < paymentList.size(); i++){
                            if(paymentList.get(i).getName().equals(payment)){
                                paymentId = paymentList.get(i).getId();
                                break;
                            }
                        }

                        toast.show();
                        System.out.println("DEBUG: !Permanent! " + name + " " + value_string + " " + startDate + " " + endDate + " " + intervall + " " + 0);
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast toastError = Toast.makeText(getApplicationContext(), "Fields with * are required", Toast.LENGTH_SHORT);
                        toastError.show();
                        System.out.println("DEBUG:" + getIntent().getBooleanExtra("intake", false));

                    }
                    //database.close();
                }
                else {
                    Toast toastError = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                    toastError.show();
                }
            }
        });

    }
}
