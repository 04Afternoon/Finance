package me.finance.finance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.TextView;

public class OverviewActivity extends FragmentActivity {

    private TextView mTextMessage;
    private DatabaseHandler DBHandler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_balance:
                    setTitle("BALANCE");
                    FragmentBalance balance = new FragmentBalance();
                    fragmentManager.beginTransaction().replace(R.id.fragment, balance).commit();
                    return true;
                case R.id.navigation_one:
                    setTitle("ONE");
                    FragmentOne one = new FragmentOne();
                    fragmentManager.beginTransaction().replace(R.id.fragment, one).commit();
                    return true;
                case R.id.navigation_two:
                    setTitle("TWO");
                    FragmentTwo two = new FragmentTwo();
                    fragmentManager.beginTransaction().replace(R.id.fragment, two).commit();
                    return true;
                case R.id.navigation_three:
                    setTitle("THREE");
                    FragmentThree three = new FragmentThree();
                    fragmentManager.beginTransaction().replace(R.id.fragment, three).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        databaseHandler.open();
        databaseHandler.createTables();
        //databaseHandler.insertDummyValues();


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        System.out.println("Database closed");
        databaseHandler.close();
    }

}
