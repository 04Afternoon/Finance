package me.finance.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Payment;

public class CategoryActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    private ListAdapter adapter;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle(getIntent().getStringExtra("settings"));

        getSupportActionBar().hide();

        myToolbar = (Toolbar) findViewById(R.id.toolbar_balance);

        Button create_category_button = findViewById(R.id.create_category);
        Button exit_categories_button = findViewById(R.id.exitCategoriesButton);

        if(getIntent().getStringExtra("settings").equals("manage accounts")){
            create_category_button.setText("Add Account");
        }

        databaseHandler.open();

        if(getIntent().getStringExtra("settings").equals("manage categories"))
        {
          ArrayList<Category> categories = databaseHandler.getCategories();
          populateCategoryListView(categories);
        }
        else if(getIntent().getStringExtra("settings").equals("manage accounts"))
        {
            ArrayList<Payment> accounts = databaseHandler.getPayments();
            populateAccountListView(accounts);
        }


        create_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.categoryName)).getText().toString();
                if(getIntent().getStringExtra("settings").equals("manage categories") && !name.isEmpty())
                {
                    if (databaseHandler.getCategories(name).isEmpty()) {
                        databaseHandler.addCategoryBetter(name);
                        ArrayList<Category> categories = databaseHandler.getCategories();
                        populateCategoryListView(categories);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Category already exists!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    ((EditText) findViewById(R.id.categoryName)).setText("");
                }
                else if(getIntent().getStringExtra("settings").equals("manage accounts") && !name.isEmpty())
                {
                    if (databaseHandler.getPayments(name).isEmpty()) {
                        databaseHandler.addPaymentBetter(name);
                        ArrayList<Payment> accounts = databaseHandler.getPayments();
                        populateAccountListView(accounts);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Account already exists!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    ((EditText) findViewById(R.id.categoryName)).setText("");
                } else {
                    Toast toast = Toast.makeText(view.getContext(), "Empty name!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        final Intent intent = new Intent(this, EditAccounts.class);


        final ListView categoryList = (ListView) findViewById(R.id.categoryList);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = (String) categoryList.getItemAtPosition(i);
                intent.putExtra("name", name);
                if(getIntent().getStringExtra("settings").equals("manage categories"))
                {
                    intent.putExtra("status", "categories");
                    finish();
                    startActivity(intent);
                }
                else if(getIntent().getStringExtra("settings").equals("manage accounts"))
                {
                    intent.putExtra("status", "payments");
                    finish();
                    startActivity(intent);
                }
            }
        });

        exit_categories_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void populateCategoryListView(ArrayList<Category> categories){

        ArrayList<String> categoryNames = new ArrayList<String>();
        for(int i = 0; i < categories.size(); i++)
        {
            categoryNames.add(categories.get(i).getName());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryNames);

        ListView categoryList  = (ListView) findViewById(R.id.categoryList);
        categoryList.setAdapter(adapter);
    }

    public void populateAccountListView(ArrayList<Payment> accounts){

        ArrayList<String> accountNames = new ArrayList<String>();
        for(int i = 0; i < accounts.size(); i++)
        {
            accountNames.add(accounts.get(i).getName());
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, accountNames);

        ListView accountList  = (ListView) findViewById(R.id.categoryList);
        accountList.setAdapter(adapter);
    }
}
