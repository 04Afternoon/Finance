package me.finance.finance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
                String name = name_text_field.getText().toString();
                EditText value_text_field = findViewById(R.id.value_text_field);
                String value = value_text_field.getText().toString();
                EditText start_date_text_field = findViewById(R.id.start_text_field);
                String startDate = start_date_text_field.getText().toString();
                String endDate = end_date_text_field.getText().toString();
                String intervall = intervall_text_field.getText().toString();
                EditText payment_text_field = findViewById(R.id.payment_text_field);
                String payment = payment_text_field.getText().toString();

                if(!name.isEmpty() && !value.isEmpty() && !startDate.isEmpty() && !payment.isEmpty() && intervall.isEmpty() && endDate.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "TODO -> push to DB", Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println("DEBUG: !Once! " + name + " " + value + " " + startDate + " " + "ONCE" + " " + "ONCE" + " " + payment);
                    finish();
                } else if(!name.isEmpty() && !value.isEmpty() && !startDate.isEmpty() && !payment.isEmpty() && !intervall.isEmpty() && !endDate.isEmpty()){
                    Toast toast = Toast.makeText(getApplicationContext(), "TODO -> push to DB", Toast.LENGTH_SHORT);
                    toast.show();
                    System.out.println("DEBUG: !Permanent! " + name + " " + value + " " + startDate + " " + endDate + " " + intervall + " " + payment);
                    finish();
                } else {
                    Toast toastError = Toast.makeText(getApplicationContext(), "Fields with * are required", Toast.LENGTH_SHORT);
                    toastError.show();
                    System.out.println("DEBUG:" + getIntent().getBooleanExtra("intake", false));

                }

            }
        });

    }
}
