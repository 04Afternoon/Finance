package me.finance.finance;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMonths extends Fragment {
    private ListView mListView;
    private Context context;
    private View view;

    public FragmentMonths() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_months, container, false);
        mListView = (ListView) view.findViewById(R.id.monthly_list);
        context = container.getContext();

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

        CustomAdapter adapter = new CustomAdapter(context, itemsArrayList);

        ListView itemsListView  = (ListView) view.findViewById(R.id.monthly_list);
        itemsListView.setAdapter(adapter);
    }

}
