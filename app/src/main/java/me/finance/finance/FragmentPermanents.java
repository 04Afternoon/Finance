package me.finance.finance;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import me.finance.finance.Model.Permanent;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPermanents extends Fragment {
    private ListView mListView;
    private Context context, containerContext;
    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getContext());
    private View view;
    private StandingOrdersAdapter adapter;
    private TextView selected;

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
        databaseHandler.open();
        final AlertDialog dialog = createDialog();

        populateListView();

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

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mListView.getSelectedView().findViewById(R.id.permsName);
                //selected = (TextView) mListView.getSelectedView().findViewById(R.id.permsName);
                //System.out.println(selected.getText());
                dialog.show();
            }
        });

        return view;
    }

    public void populateListView(){

        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());

        List<Permanent> perms = databaseHandler.getPermanents();

        adapter = new StandingOrdersAdapter(containerContext, perms);

        ListView itemsListView = (ListView) view.findViewById(R.id.perms_list);
        itemsListView.setAdapter(adapter);
        /*
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
        */
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

    public void deleteStandingOrder()
    {
      System.out.println();
      databaseHandler.removeStandingOrder((String) mListView.getItemAtPosition(mListView.getSelectedItemPosition()));
    }

    public AlertDialog createDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you really want to delete the standing order?");
        builder.setTitle("DELETE");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.out.println("Clicked on OK button\n");
                deleteStandingOrder();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }
}
