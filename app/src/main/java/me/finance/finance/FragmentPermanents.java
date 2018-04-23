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
public class FragmentPermanents extends Fragment {
    private ListView mListView;
    private Context context;
    private View view;

    public FragmentPermanents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_permanents, container, false);
        mListView = (ListView) view.findViewById(R.id.perms_list);
        context = container.getContext();

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


        CustomAdapter adapter = new CustomAdapter(context, itemsArrayList);

        ListView itemsListView  = (ListView) view.findViewById(R.id.perms_list);
        itemsListView.setAdapter(adapter);
    }
}
