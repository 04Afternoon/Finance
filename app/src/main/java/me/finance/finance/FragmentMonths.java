package me.finance.finance;


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMonths extends Fragment {
    private ListView mListView;
    private Context context, containerContext;
    private View view;

    public FragmentMonths() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_months, container, false);
        mListView = (ListView) view.findViewById(R.id.monthly_list);
        containerContext = container.getContext();

        ImageButton calender_button = view.findViewById(R.id.calender_button);
        ImageButton search_button = view.findViewById(R.id.months_search_button);

        buttonEffect(calender_button);
        buttonEffect(search_button);

        calender_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "TODO: IMPLEMENT CALENDER BUTTON", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "TODO: IMPLEMENT SEARCH BUTTON", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        populateListView();

        return view;
    }

    public void populateListView(){

        ArrayList<Item> itemsArrayList = new ArrayList<>();

        for(Integer i = 0; i < 20; i++){
            Item item = new Item("Test" + Integer.toString(i), Integer.toString(ThreadLocalRandom.current().nextInt(30, 150)));
            itemsArrayList.add(item);
        }

        System.out.println("DEBUG:" + itemsArrayList);

        CustomAdapter adapter = new CustomAdapter(containerContext, itemsArrayList);

        ListView itemsListView  = (ListView) view.findViewById(R.id.monthly_list);
        itemsListView.setAdapter(adapter);
    }


    //copied from https://stackoverflow.com/questions/7175873/click-effect-on-button-in-android
    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

}
