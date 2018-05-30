package me.finance.finance;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;

//TODO http://www.android-graphview.org/ API EINBINDEN

public class FragmentStats extends Fragment implements DialogInterface.OnClickListener {

    private Context context;
    private PieChart categoryChartIn;
    private PieChart categoryChartOut;
    private PieChart paymentOptionsChartIn;
    private PieChart paymentOptionsChartOut;
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
        Calendar firstOfYear = Calendar.getInstance();
        firstOfYear.set(Calendar.MONTH, 0);
        firstOfYear.set(Calendar.DAY_OF_MONTH, 1);
        startDate = firstOfYear.getTime();
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

        categoryChartIn = view.findViewById(R.id.category_chart_in);
        categoryChartOut = view.findViewById(R.id.category_chart_out);
        inOutChart = view.findViewById(R.id.in_out_chart);
        paymentOptionsChartIn = view.findViewById(R.id.payment_opt_chart_in);
        paymentOptionsChartOut = view.findViewById(R.id.payment_opt_chart_out);
        calculateAndShowCategory();
        calculateAndShowPaymentOptions();
        calculateAndShowInOutChart();
        return view;
    }

    private void calculateAndShowPaymentOptions() {
        DatabaseHandler database = DatabaseHandler.getInstance(this.getContext());

        PieData pieDataIn = new PieData();
        pieDataIn.setValueFormatter(new PercentFormatter());
        PieData pieDataOut = new PieData();
        pieDataOut.setValueFormatter(new PercentFormatter());

        List<Payment> payments = database.getPayments();
        List<PieEntry> entriesIn = new ArrayList<>();
        List<PieEntry> entriesOut = new ArrayList<>();

        for (Payment payment : payments) {
            List<Intake> intakes = database.getIntakes(payment, startDate, endDate);

            double income = 0;
            double expenses = 0;
            for (Intake intake : intakes) {
                if (intake.getValue() > 0) {
                    income += intake.getValue();
                } else {
                    expenses -= intake.getValue();
                }
            }
            if (income > 0)
                entriesIn.add(new PieEntry((float)income, payment.getName()));
            if (expenses > 0)
                entriesOut.add(new PieEntry((float)expenses, payment.getName()));
        }

        PieDataSet dataSetIn = new PieDataSet(entriesIn, "Income per payment option");
        dataSetIn.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataIn.setDataSet(dataSetIn);
        PieDataSet dataSetOut = new PieDataSet(entriesOut, "Expenses per payment option");
        dataSetOut.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataOut.setDataSet(dataSetOut);

        paymentOptionsChartIn.setEntryLabelTextSize(14.f);
        paymentOptionsChartIn.setEntryLabelColor(ColorTemplate.rgb("000000"));
        pieDataIn.setValueTextSize(10.f);
        paymentOptionsChartOut.setEntryLabelTextSize(14.f);
        paymentOptionsChartOut.setEntryLabelColor(ColorTemplate.rgb("000000"));
        pieDataOut.setValueTextSize(10.f);
        paymentOptionsChartIn.getDescription().setEnabled(false);
        paymentOptionsChartOut.getDescription().setEnabled(false);

        paymentOptionsChartIn.setData(pieDataIn);
        paymentOptionsChartOut.setData(pieDataOut);

        paymentOptionsChartIn.invalidate();
        paymentOptionsChartOut.invalidate();
    }
    private void calculateAndShowCategory() {
        DatabaseHandler database = DatabaseHandler.getInstance(this.getContext());

        PieData pieDataIn = new PieData();
        pieDataIn.setValueFormatter(new PercentFormatter());
        PieData pieDataOut = new PieData();
        pieDataOut.setValueFormatter(new PercentFormatter());

        List<Category> categories = database.getCategories();
        List<PieEntry> entriesIn = new ArrayList<>();
        List<PieEntry> entriesOut = new ArrayList<>();

        for (Category category : categories) {
            List<Intake> intakes = database.getIntakes(category, startDate, endDate);

            double income = 0;
            double expenses = 0;
            for (Intake intake : intakes) {
                if (intake.getValue() > 0) {
                    income += intake.getValue();
                } else {
                    expenses -= intake.getValue();
                }
            }
            if (income > 0)
                entriesIn.add(new PieEntry((float)income, category.getName()));
            if (expenses > 0)
                entriesOut.add(new PieEntry((float)expenses, category.getName()));
        }

        PieDataSet dataSetIn = new PieDataSet(entriesIn, "Income per category");
        dataSetIn.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataIn.setDataSet(dataSetIn);
        PieDataSet dataSetOut = new PieDataSet(entriesOut, "Expenses per category");
        dataSetOut.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataOut.setDataSet(dataSetOut);

        categoryChartIn.setEntryLabelTextSize(14.f);
        categoryChartIn.setEntryLabelColor(ColorTemplate.rgb("000000"));
        categoryChartOut.setEntryLabelTextSize(14.f);
        categoryChartOut.setEntryLabelColor(ColorTemplate.rgb("000000"));
        dataSetIn.setValueTextSize(10.f);
        dataSetOut.setValueTextSize(10.f);
        categoryChartIn.getDescription().setEnabled(false);
        categoryChartOut.getDescription().setEnabled(false);

        categoryChartIn.setData(pieDataIn);
        categoryChartOut.setData(pieDataOut);

        categoryChartIn.invalidate();
        categoryChartOut.invalidate();
    }

    private void calculateAndShowInOutChart() {

        DatabaseHandler database = DatabaseHandler.getInstance(this.getContext());

        ArrayList<BarEntry> intakeEntries = new ArrayList<>();
        ArrayList<BarEntry> expenseEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        String[] months = new String[] {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        Calendar cal = Calendar.getInstance();
        final Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        float x = 0.5f;
        for (cal.setTime(startDate); !cal.after(endCal); cal.add(Calendar.MONTH, 1)) {
            labels.add(months[cal.get(Calendar.MONTH) % 12]);

            if (cal.after(startCal))
                cal.set(Calendar.DAY_OF_MONTH, 1);
            Calendar endOfMonth = Calendar.getInstance();
            endOfMonth.setTime(cal.getTime());
            endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (endCal.before(endOfMonth))
                endOfMonth.setTime(endDate);

            List<Intake> intakes = database.getIntakes(cal.getTime(), endOfMonth.getTime());
            double income = 0;
            double expenses = 0;
            for (Intake intake : intakes) {
                if (intake.getValue() > 0) {
                    income += intake.getValue();
                } else {
                    expenses -= intake.getValue();
                }
            }

            intakeEntries.add(new BarEntry(x, (float)income));
            expenseEntries.add(new BarEntry(x++, (float)expenses));
        }
        BarDataSet barDataSet = new BarDataSet(intakeEntries, "Intakes");
        barDataSet.setColors(ColorTemplate.rgb("33A008"));

        BarDataSet barDataSet2 = new BarDataSet(expenseEntries, "Outgoings");
        barDataSet2.setColors(ColorTemplate.rgb("FA0F0F"));

        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.40f; // x2 dataset

        float groupSpace = 1f - (barSpace + barWidth) * 2f;

        BarData barData = new BarData(barDataSet, barDataSet2);
        barData.setBarWidth(barWidth);

        inOutChart.setData(barData);
        inOutChart.getXAxis().setGranularity(1f);
        inOutChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        inOutChart.groupBars(0f, groupSpace, barSpace);
        inOutChart.getXAxis().setCenterAxisLabels(true);
        inOutChart.setExtraLeftOffset(10f);
        inOutChart.getDescription().setEnabled(false);
        inOutChart.setVisibleXRangeMaximum(4f);

        inOutChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentMonths fragment = new FragmentMonths();
                //fragment.setStartDate();
                float x = e.getX() - 0.29f;
                boolean outgoing = false;
                if ((int)x != x) {
                    outgoing = true;
                    x -= 0.42f;
                }
                Calendar selectedMonth = Calendar.getInstance();
                selectedMonth.setTime(startDate);
                selectedMonth.add(Calendar.MONTH, (int)x);
                Calendar endOfMonth = Calendar.getInstance();
                endOfMonth.setTime(selectedMonth.getTime());
                endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH));

                fragment.setStartDate(selectedMonth.getTime());
                fragment.setEndDate(endOfMonth.getTime());
                if (outgoing) {
                    fragment.setValueTo(0);
                } else {
                    fragment.setValueFrom(0);
                }
                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
            }

            @Override
            public void onNothingSelected() {

            }
        });

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

        calculateAndShowCategory();
        calculateAndShowPaymentOptions();
        calculateAndShowInOutChart();
    }
}
