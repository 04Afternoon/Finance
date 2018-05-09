package me.finance.finance;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPermanents extends Fragment {
    private ListView mListView;
    private Context context, containerContext;
    private View view;

    public FragmentPermanents() {
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


        view = inflater.inflate(R.layout.fragment_permanents, container, false);
        mListView = (ListView) view.findViewById(R.id.perms_list);
        containerContext = container.getContext();

        ImageButton add_perms_button = view.findViewById(R.id.add_perms_button);
        buttonEffect(add_perms_button);

        add_perms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InOutPermsActivity.class);
                intent.putExtra(InOutPermsActivity.IS_STANDING_ORDER, true);
                startActivity(intent);
            }
        });

        populateListView();

        return view;
    }

    public void populateListView(){

        ArrayList<Item> itemsArrayList = new ArrayList<>();

        for(Integer i = 0; i < 20; i++){
            String string;
            Integer rand = ThreadLocalRandom.current().nextInt(0, 3);
            if(rand == 1){
                   string = "MONTHLY";
            } else if(rand  == 2){
                string = "WEEKLY";
            } else {
                string = "YEARLY";
            }
            Item item = new Item("Test" + i, string);
            itemsArrayList.add(item);
        }

        System.out.println("DEBUG:" + itemsArrayList);


        CustomAdapter adapter = new CustomAdapter(containerContext, itemsArrayList);

        ListView itemsListView  = (ListView) view.findViewById(R.id.perms_list);
        itemsListView.setAdapter(adapter);
    }


    //copied from https://stackoverflow.com/questions/7175873/click-effect-on-button-in-android
    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
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
