package me.finance.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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
    private TextView title;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle(getIntent().getStringExtra("settings"));

        Button create_category_button = findViewById(R.id.create_category);
        ToggleButton remove_category_button = (ToggleButton) findViewById(R.id.remove_category);
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
                if(title.getText().toString().equals("manage categories") && !name.isEmpty())
                {
                    databaseHandler.addCategoryBetter(name);
                    ArrayList<Category> categories = databaseHandler.getCategories();
                    populateCategoryListView(categories);
                    ((EditText) findViewById(R.id.categoryName)).setText("");
                }
                else if(title.getText().toString().equals("manage accounts") && !name.isEmpty())
                {
                    databaseHandler.addPaymentBetter(name);
                    ArrayList<Payment> accounts = databaseHandler.getPayments();
                    populateAccountListView(accounts);
                    ((EditText) findViewById(R.id.categoryName)).setText("");
                } else {
                    Toast toast = Toast.makeText(view.getContext(), "Empty name!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        remove_category_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    final ListView categoryList = (ListView) findViewById(R.id.categoryList);
                    categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String name = (String) categoryList.getItemAtPosition(i);

                            if(title.getText().toString().equals("manage categories"))
                            {
                                databaseHandler.removeCategory(name);
                                ArrayList<Category> categories = databaseHandler.getCategories();
                                populateCategoryListView(categories);
                            }
                            else if(title.getText().toString().equals("manage accounts"))
                            {
                                databaseHandler.removePayment(name);
                                ArrayList<Payment> accounts = databaseHandler.getPayments();
                                populateAccountListView(accounts);
                            }
                        }
                    });
                }
                else
                {
                    ListView categoryList = (ListView) findViewById(R.id.categoryList);
                    categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast toast = Toast.makeText(view.getContext(), "Jesus Christ what are you doing??", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
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
