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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;
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
    private Category category;
    private Payment payment;
    private double valueFrom;
    private double valueTo;

    public FragmentMonths() {
        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(Calendar.DATE, 1);
        startDate = firstOfMonth.getTime();
        endDate = new Date();
        sort = new Sort(Sort.Column.values()[0], Sort.Order.values()[0]);
        category = null;
        payment = null;
        valueFrom = Double.NaN;
        valueTo = Double.NaN;
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

        Button calender_button = view.findViewById(R.id.calender_button);
        Button search_button = view.findViewById(R.id.months_search_button);

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

        List<Intake> intakes = databaseHandler.getIntakes(startDate,endDate,sort,category,payment,valueFrom,valueTo);

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

            EditText valueFromTextField =  dialog.findViewById(R.id.valueFromTextField);
            String valueFromString = valueFromTextField.getText().toString();

            double tmpValueFrom = Double.NaN;

            if (!valueFromString.isEmpty()) {
                tmpValueFrom = Double.valueOf(valueFromString);
            }


            EditText valeuToTextField =  dialog.findViewById(R.id.valueToTextField);
            String valueToString = valeuToTextField.getText().toString();


            double tmpValueTo = Double.NaN;

            if (!valueToString.isEmpty()) {
                tmpValueTo = Double.valueOf(valueToString);
            }


            if(!Double.isNaN(tmpValueTo) && !Double.isNaN(tmpValueFrom) && tmpValueFrom>tmpValueTo){
                Toast toastError = Toast.makeText(this.getContext(), "\"To\" must be equal or greater than \"From\"", Toast.LENGTH_SHORT);
                toastError.show();
                return;
            }

            valueFrom = tmpValueFrom;
            valueTo = tmpValueTo;

            Spinner sortSpinner = dialog.findViewById(R.id.sort_spinner);
            if(sortSpinner != null && sortSpinner.getSelectedItem() != null){
                sort = (Sort) sortSpinner.getSelectedItem();
            }

            Spinner categorieSpinner = dialog.findViewById(R.id.filterCategorieSpinner);
            if(categorieSpinner != null && categorieSpinner.getSelectedItem() != null){
                if(categorieSpinner.getSelectedItemPosition() == 0){
                    category = null;
                }else {
                    CategoryAdapter adapter = (CategoryAdapter) categorieSpinner.getAdapter();
                    category = adapter.getCategories().get(categorieSpinner.getSelectedItemPosition() - 1);
                }
            }

            Spinner paymentSpinner = dialog.findViewById(R.id.filterPaymentOptionSpinner);
            if(paymentSpinner != null && paymentSpinner.getSelectedItem() != null){
                if(paymentSpinner.getSelectedItemPosition() == 0){
                    payment = null;
                }else {
                    PaymentAdapter adapter = (PaymentAdapter) paymentSpinner.getAdapter();
                    payment = adapter.getPayments().get(paymentSpinner.getSelectedItemPosition() - 1);
                }
            }



        }
        else{
            List<Date> dates = calendar.getSelectedDates();
            startDate = dates.get(0);
            endDate = dates.get(dates.size()-1);
        }
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(this.getContext());
        List<Intake> intakes = databaseHandler.getIntakes(startDate, endDate, sort, category, payment,valueFrom,valueTo);
        adapter.setItems(intakes);
        adapter.notifyDataSetChanged();

        dialog.dismiss();
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


        Spinner sortSpinner = v.findViewById(R.id.sort_spinner);
        SortAdapter sortAdapter = new SortAdapter(this.getContext());

        sortSpinner.setAdapter(sortAdapter);
        for(int i = 0; i < sortAdapter.getCount();i++){
            Sort element = sortAdapter.getItem(i);
            if(element == null){
                continue;
            }
            if(element.equals(sort)){
                sortSpinner.setSelection(i);
            }
        }

        Spinner categorySpinner = v.findViewById(R.id.filterCategorieSpinner);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this.getContext());

        categorySpinner.setAdapter(categoryAdapter);

        if(category == null){
            categorySpinner.setSelection(0);
        }else{
            for(int i = 0; i < categoryAdapter.getCount(); i++){
                String element = categoryAdapter.getItem(i);
                if(element == null){
                    continue;
                }
                if(element.equals(category.getName())){
                    categorySpinner.setSelection(i);
                }
            }

        }

        Spinner paymentSpinner = v.findViewById(R.id.filterPaymentOptionSpinner);
        PaymentAdapter paymentAdapter = new PaymentAdapter(this.getContext());

        paymentSpinner.setAdapter(paymentAdapter);

        if(payment == null){
            paymentSpinner.setSelection(0);
        }else{
            for(int i = 0; i < paymentAdapter.getCount(); i++){
                String element = paymentAdapter.getItem(i);
                if(element == null){
                    continue;
                }
                if(element.equals(payment.getName())){
                    paymentSpinner.setSelection(i);
                }
            }

        }

        EditText valueFromTextField =  v.findViewById(R.id.valueFromTextField);

        if(!Double.isNaN(valueFrom)){

            valueFromTextField.setText(String.format("%.2f", valueFrom));
        }

        EditText valueToTextField =  v.findViewById(R.id.valueToTextField);

        if(!Double.isNaN(valueTo)){

            valueToTextField.setText(String.format("%.2f", valueTo));
        }


        AlertDialog alertDialog = builder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        FragmentMonths.this.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
        });


        return alertDialog;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setValueFrom(double valueFrom) {
        this.valueFrom = valueFrom;
    }

    public void setValueTo(double valueTo) {
        this.valueTo = valueTo;
    }
}
