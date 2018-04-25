package me.finance.finance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import me.finance.finance.Model.Category;

public class CategoryActivity extends AppCompatActivity {

    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        populateListView();

        Button create_category_button = findViewById(R.id.create_category);
        ToggleButton remove_category_button = (ToggleButton) findViewById(R.id.remove_category);

        EditText categoryName = (EditText) findViewById(R.id.categoryName);
        String name = categoryName.toString();

        create_category_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(view.getContext(), "TODO: IMPLEMENT DATABASE FIRST", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        remove_category_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    ListView categoryList = (ListView) findViewById(R.id.categoryList);
                    categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast toast = Toast.makeText(view.getContext(), "TODO: IMPLEMENT DATABASE", Toast.LENGTH_SHORT);
                            toast.show();
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
    }

    public void populateListView(){

        ArrayList<String> categoryNames = new ArrayList<String>();
        //TODO: Implement database first
        /*ArrayList<Category> categories = databaseHandler.getCategories();
        for(int i = 0; i < categories.size(); i++)
        {
            categoryNames.add(categories.get(i).getName());
        }*/

        for(int i = 0; i < 10; i++)
        {
            categoryNames.add("Test" + i);
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryNames);

        ListView categoryList  = (ListView) findViewById(R.id.categoryList);
        categoryList.setAdapter(adapter);
    }
}
