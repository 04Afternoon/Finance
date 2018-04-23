package me.finance.finance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

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
                case R.id.navigation_months:
                    FragmentMonths months = new FragmentMonths();
                    fragmentManager.beginTransaction().replace(R.id.fragment, months).commit();
                    return true;
                case R.id.navigation_permanents:
                    FragmentPermanents perms = new FragmentPermanents();
                    fragmentManager.beginTransaction().replace(R.id.fragment, perms).commit();
                    return true;
                case R.id.navigation_stats:
                    FragmentStats stats = new FragmentStats();
                    fragmentManager.beginTransaction().replace(R.id.fragment, stats).commit();
                    return true;
                case R.id.navigation_settings:
                    FragmentSettings settings = new FragmentSettings();
                    fragmentManager.beginTransaction().replace(R.id.fragment, settings).commit();
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
