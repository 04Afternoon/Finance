package me.finance.finance;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.savvi.rangedatepicker.CalendarPickerView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatePickerDialog extends DialogFragment implements View.OnClickListener {

    private Date startDate;
    private Date endDate;

    private CalendarPickerView calendar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select date-range");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
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
        Date startDate = c2.getTime();

        calendar = (CalendarPickerView) v.findViewById(R.id.calendar_view);

        Calendar c3 = Calendar.getInstance();
        int day = c3.get(Calendar.DAY_OF_MONTH);
        c3.set(Calendar.DAY_OF_MONTH, day+1);

        c2 = Calendar.getInstance();
        c2.set(Calendar.DATE, 1);

        calendar.init(startDate, c3.getTime(), new SimpleDateFormat("MMM yyyy")) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withSelectedDates(Arrays.asList(
                        c2.getTime(), new Date()
                ));
        return builder.create();

    }

    @Override
    public void onClick(View view) {
        List<Date> dates = calendar.getSelectedDates();
        startDate = dates.get(0);
        endDate = dates.get(dates.size()-1);
        dismiss();
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
