package me.finance.finance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.TextView;

public class EditAccounts extends AppCompatActivity {



    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
    private TextView title;
    private ListAdapter adapter;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_accounts);

    }



}
