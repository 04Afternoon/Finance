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
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//TODO http://www.android-graphview.org/ API EINBINDEN

public class FragmentStats extends Fragment implements DialogInterface.OnClickListener {

    private Context context;
    private PieChart categoryChart;
    private PieChart paymentOptionsChart;
    private BarChart inOutChart;

    private CalendarPickerView calendar;
    private AlertDialog calenderDialog;
    private Date startDate;
    private Date endDate;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getContext();
    }

    public FragmentStats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        Calendar firstOfMonth = Calendar.getInstance();
        firstOfMonth.set(Calendar.DATE, 1);
        startDate = firstOfMonth.getTime();
        endDate = new Date();

        ImageButton calender_button = view.findViewById(R.id.stats_calender_button);

        calender_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calenderDialog = createDialog();
                calenderDialog.show();
            }
        });

        buttonEffect(calender_button);

        categoryChart = view.findViewById(R.id.category_chart);
        inOutChart = view.findViewById(R.id.in_out_chart);
        paymentOptionsChart = view.findViewById(R.id.payment_opt_chart);
        calculateAndShowCategory();
        calculateAndPaymentOptions();
        calculateAndShowInOutChart();
        return view;
    }

    private void calculateAndPaymentOptions() {
        PieData pieData = new PieData();
        pieData.setValueFormatter(new PercentFormatter());

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "foo"));
        entries.add(new PieEntry(30, "bar"));
        entries.add(new PieEntry(50, "stuff"));

        PieDataSet dataSet = new PieDataSet(entries, "Payment options");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieData.setDataSet(dataSet);

        paymentOptionsChart.setData(pieData);

    }
    private void calculateAndShowCategory() {
        PieData pieData = new PieData();
        pieData.setValueFormatter(new PercentFormatter());

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "foo"));
        entries.add(new PieEntry(30, "bar"));
        entries.add(new PieEntry(50, "stuff"));

        PieDataSet dataSet = new PieDataSet(entries, "Categories");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieData.setDataSet(dataSet);

        categoryChart.setData(pieData);

    }

    private void calculateAndShowInOutChart() {


        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        ArrayList<BarEntry> intakes = new ArrayList<>();
        intakes.add(new BarEntry(0.5f, 0));
        intakes.add(new BarEntry(2f, 2));
        intakes.add(new BarEntry(3f, 4));
        intakes.add(new BarEntry(4f, 6));
        intakes.add(new BarEntry(5f, 8));
        intakes.add(new BarEntry(5.5f,10));
        BarDataSet barDataSet = new BarDataSet(intakes, "categories 1");
        barDataSet.setColors(ColorTemplate.rgb("33A008"));

        ArrayList<BarEntry> outgoings = new ArrayList<>();
        outgoings.add(new BarEntry(0.5f, 1));
        outgoings.add(new BarEntry(2f, 3));
        outgoings.add(new BarEntry(3f, 4));
        outgoings.add(new BarEntry(4f, 7));
        outgoings.add(new BarEntry(5f,9));
        outgoings.add(new BarEntry(5.5f, 11));
        BarDataSet barDataSet2 = new BarDataSet(outgoings, "categories");
        barDataSet2.setColors(ColorTemplate.rgb("FA0F0F"));

        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.40f; // x2 dataset

        float groupSpace = 1f - (barSpace + barWidth) * 2f;

        BarData barData = new BarData(barDataSet, barDataSet2);
        barData.setBarWidth(barWidth);

        inOutChart.setData(barData);
//        inOutChart.getXAxis().setGranularity(1f);
        inOutChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        inOutChart.groupBars(0f, groupSpace, barSpace);
        inOutChart.getXAxis().setCenterAxisLabels(true);
        inOutChart.setExtraLeftOffset(10f);

        inOutChart.invalidate();


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

    public AlertDialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select date-range");
        builder.setPositiveButton("OK",
                FragmentStats.this
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
        c3.set(Calendar.DAY_OF_MONTH, day + 1);


        calendar.init(firstDate, c3.getTime(), new SimpleDateFormat("MMM yyyy")) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(Arrays.asList(
                        startDate, endDate
                ));
        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        AlertDialog dialog = (AlertDialog) dialogInterface;
        List<Date> dates = calendar.getSelectedDates();
        startDate = dates.get(0);
        endDate = dates.get(dates.size() - 1);
    }
}
