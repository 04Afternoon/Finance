package me.finance.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import me.finance.finance.Model.Payment;

public class EditAccounts extends AppCompatActivity {



    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    private TextView title;
    private ListAdapter adapter;
    private Toolbar myToolbar;
    private String account_name, new_name;
    private EditText account_name_input;
    private Button delete_button, submit_button, cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accounts);
        account_name = getIntent().getStringExtra("name");
        TextView name_view = (TextView)findViewById(R.id.accountName);
        name_view.setText(account_name);
        delete_button = findViewById(R.id.delete);
        submit_button = findViewById(R.id.submit);
        cancel_button = findViewById(R.id.cancel);
        account_name_input = findViewById(R.id.accountNameInput);

        final Intent intent = new Intent(this, CategoryActivity.class);


        if(getIntent().getStringExtra("status").equals("categories"))
        {
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHandler.removeCategory(account_name);
                    intent.putExtra("settings", "manage categories");
                    finish();
                    startActivity(intent);
                }
            });
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new_name = account_name_input.getText().toString();
                    databaseHandler.updateCategories(new_name, account_name);
                    intent.putExtra("settings", "manage categories");
                    finish();
                    startActivity(intent);
                }
            });
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("settings", "manage categories");
                    finish();
                    startActivity(intent);
                }
            });
        }
        else if(getIntent().getStringExtra("status").equals("payments"))
        {
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseHandler.removePayment(account_name);
                    intent.putExtra("settings", "manage accounts");
                    finish();
                    startActivity(intent);
                }
            });
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new_name = account_name_input.getText().toString();
                    databaseHandler.updatePayment(new_name, account_name);
                    intent.putExtra("settings", "manage accounts");
                    finish();
                    startActivity(intent);
                }
            });
            cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent.putExtra("settings", "manage accounts");
                    finish();
                    startActivity(intent);
                }
            });
        }











    }




}
