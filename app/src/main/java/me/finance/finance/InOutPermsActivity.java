package me.finance.finance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.finance.finance.Model.Intake;

public class InOutPermsActivity extends AppCompatActivity {

    private ImageButton exitButton;
    private Button finishButton;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_perms);
        title = findViewById(R.id.auftrag_text);
        title.setText(getIntent().getStringExtra("type"));

        final EditText end_date_text_field = findViewById(R.id.end_text_field);
        final EditText intervall_text_field = findViewById(R.id.intervall_text_field);

        if(title.getText().toString().equals("Expense") || title.getText().toString().equals("Intake")) {
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
                EditText payment_text_field = findViewById(R.id.payment_text_field);

                String name = name_text_field.getText().toString();
                String value_string = value_text_field.getText().toString();
                String startDate = start_date_text_field.getText().toString();
                String endDate = end_date_text_field.getText().toString();
                String intervall = intervall_text_field.getText().toString();
                String payment = payment_text_field.getText().toString();

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
                    if (!title.getText().toString().equals("Permanents") && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty() && !payment.isEmpty() && intervall.isEmpty() && endDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Transaction saved", Toast.LENGTH_SHORT);
                        if (title.getText().toString().equals("Expense")) {
                            value *= -1;
                        }
                        database.addIntake(new Intake(value, startDate, name, "", 0, 0));
                        toast.show();
                        System.out.println("DEBUG: !Once! " + name + " " + value + " " + startDate + " " + "ONCE" + " " + "ONCE" + " " + payment);
                        finish();
                    } else if (title.getText().toString().equals("Permanents") && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty() && !payment.isEmpty() && !intervall.isEmpty() && !endDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "TODO -> push to DB", Toast.LENGTH_SHORT);

                        toast.show();
                        System.out.println("DEBUG: !Permanent! " + name + " " + value_string + " " + startDate + " " + endDate + " " + intervall + " " + payment);
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
