package me.finance.finance;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMonths extends Fragment implements DialogInterface.OnClickListener {
    private ListView mListView;
    private Context context, containerContext;
    private View view;
    private CalendarPickerView calendar;
    private MonthAdapter adapter;
    private AlertDialog calenderDialog;
    private AlertDialog sortFilterDialog;

    private Date startDate;
    private Date endDate;
    private Sort sort;

    public FragmentMonths() {
        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(Calendar.DATE, 1);
        startDate = firstOfMonth.getTime();
        endDate = new Date();
        sort = new Sort(Sort.Column.values()[0], Sort.Order.values()[0]);
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
                calenderDialog = createDialog();
                calenderDialog.show();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortFilterDialog = createSortAndFilterDialog();
                sortFilterDialog.show();
            }
        });

        populateListView();

        return view;
    }

    public void populateListView() {


        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());

        List<Intake> intakes = databaseHandler.getIntakes(startDate,endDate,sort);

        adapter = new MonthAdapter(containerContext, intakes);

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

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        AlertDialog dialog = (AlertDialog) dialogInterface;
        if(dialog.equals(sortFilterDialog)){
            Spinner spinner = dialog.findViewById(R.id.sort_spinner);
            if(spinner.getSelectedItem() == null){
                return;
            }
            sort = (Sort) spinner.getSelectedItem();
        }
        else{
            List<Date> dates = calendar.getSelectedDates();
            startDate = dates.get(0);
            endDate = dates.get(dates.size()-1);
        }
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());
        List<Intake> intakes = databaseHandler.getIntakes(startDate, endDate, sort);
        adapter.setItems(intakes);
        adapter.notifyDataSetChanged();
    }

    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select date-range");
        builder.setPositiveButton("OK",
                FragmentMonths.this
        )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.date_picker_dialog, null);

        builder.setView(v);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.YEAR, 2000);
        c2.set(Calendar.MONTH, 0);
        c2.set(Calendar.DATE, 1);
        Date firstDate = c2.getTime();

        calendar = (CalendarPickerView) v.findViewById(R.id.calendar_view);

        Calendar c3 = Calendar.getInstance();
        int day = c3.get(Calendar.DAY_OF_MONTH);
        c3.set(Calendar.DAY_OF_MONTH, day+1);


        calendar.init(firstDate, c3.getTime(), new SimpleDateFormat("MMM yyyy")) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(Arrays.asList(
                        startDate, endDate
                ));
        return builder.create();
    }

    public AlertDialog createSortAndFilterDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort and Filter");
        builder.setPositiveButton("OK",
                FragmentMonths.this
        )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.sort_filter_dialog, null);
        builder.setView(v);


        Spinner spinner = v.findViewById(R.id.sort_spinner);
        List<Sort> spinnerArray =  new ArrayList<>();
        for (Sort.Column column : Sort.Column.values()) {
            for (Sort.Order order: Sort.Order.values()) {
                spinnerArray.add(new Sort(column, order));
            }
        }

        ArrayAdapter<Sort> adapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        for(int i = 0; i < spinnerArray.size();i++){
            Sort element = spinnerArray.get(i);
            if(element.equals(sort)){
                spinner.setSelection(i);
            }
        }
        return builder.create();
    }



}
