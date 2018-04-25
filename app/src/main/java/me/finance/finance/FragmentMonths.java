package me.finance.finance;


import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import me.finance.finance.Model.Intake;

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
        mListView = view.findViewById(R.id.monthly_list);
        containerContext = container.getContext();

        ImageButton calender_button = view.findViewById(R.id.calender_button);
        ImageButton search_button = view.findViewById(R.id.months_search_button);

        buttonEffect(calender_button);
        buttonEffect(search_button);

        calender_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog newFragment = new DatePickerDialog();
                newFragment.show(getFragmentManager(), "datePicker");
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

    public void populateListView() {


        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());
        List<Intake> intakes = databaseHandler.getIntakes();

        MonthAdapter adapter = new MonthAdapter(containerContext, intakes);

        ListView itemsListView = (ListView) view.findViewById(R.id.monthly_list);
        itemsListView.setAdapter(adapter);
    }


    //copied from https://stackoverflow.com/questions/7175873/click-effect-on-button-in-android
    public static void buttonEffect(View button) {
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
