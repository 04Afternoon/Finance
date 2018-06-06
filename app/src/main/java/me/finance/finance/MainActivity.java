package me.finance.finance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;
    private DatabaseHandler DBHandler;
    private Toolbar myToolbar;


    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private FragmentPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this);
        databaseHandler.open();
        databaseHandler.createTables();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        myToolbar = (Toolbar) findViewById(R.id.toolbar_balance);
        getSupportActionBar().hide();

        switchFragment(R.id.navigation_balance);
/*

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        */
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return switchFragment(item.getItemId());
    }

    private boolean switchFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (id) {
            case R.id.navigation_balance:
                myToolbar = (Toolbar) findViewById(R.id.toolbar_balance);
                FragmentBalance balance = new FragmentBalance();
                fragmentManager.beginTransaction().replace(R.id.fragment, balance).commit();
                return true;
            case R.id.navigation_months:
                myToolbar = (Toolbar) findViewById(R.id.toolbar_months);
                FragmentMonths months = new FragmentMonths();
                fragmentManager.beginTransaction().replace(R.id.fragment, months).commit();
                return true;
            case R.id.navigation_permanents:
                myToolbar = (Toolbar) findViewById(R.id.toolbar_perms);
                FragmentPermanents perms = new FragmentPermanents();
                fragmentManager.beginTransaction().replace(R.id.fragment, perms).commit();
                return true;
            case R.id.navigation_stats:
                setTitle("Stats");
                FragmentStats stats = new FragmentStats();
                fragmentManager.beginTransaction().replace(R.id.fragment, stats).commit();
                return true;
            case R.id.navigation_settings:
                myToolbar = (Toolbar) findViewById(R.id.toolbar_settings);
                FragmentSettings settings = new FragmentSettings();
                fragmentManager.beginTransaction().replace(R.id.fragment, settings).commit();
                return true;
        }
        return false;

    }
}
