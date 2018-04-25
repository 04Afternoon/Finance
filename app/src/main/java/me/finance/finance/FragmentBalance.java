package me.finance.finance;


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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.text.NumberFormat;
import java.util.ArrayList;

import me.finance.finance.Model.Intake;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBalance extends Fragment {

    private Context context;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getContext();
    }


    public FragmentBalance() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        DatabaseHandler databaseHandler = DatabaseHandler.getInstance(view.getContext());
        databaseHandler.open();

        Button einnahmen_button = view.findViewById(R.id.einnahmen);
        Button ausgaben_button = view.findViewById(R.id.ausgaben);

        buttonEffect(einnahmen_button);
        buttonEffect(ausgaben_button);

        einnahmen_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InOutPermsActivity.class);
                intent.putExtra("type", "Intake");
                startActivity(intent);
            }
        });

        ausgaben_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InOutPermsActivity.class);
                intent.putExtra("type", "Expense");
                startActivity(intent);
            }
        });

        TextView intake = view.findViewById(R.id.einnahmen_monat);
        ArrayList<Intake> intakes = databaseHandler.getIntakes();
        TextView outgoing = view.findViewById(R.id.ausgaben_monat);
        Double ausgaben_monat = 0.0;
        Double einnahmen_monat = 0.0;
        for(int i = 0; i < intakes.size(); i++)
        {
            if(intakes.get(i).getValue() > 0)
              einnahmen_monat += intakes.get(i).getValue();
            else if(intakes.get(i).getValue() < 0)
                ausgaben_monat += intakes.get(i).getValue();
        }
        NumberFormat string_in = NumberFormat.getNumberInstance();
        intake.setText(String.format("%.2f", einnahmen_monat));
        NumberFormat string_out = NumberFormat.getNumberInstance();
        outgoing.setText(String.format("%.2f", ausgaben_monat));

        TextView total = view.findViewById(R.id.textView2);
        total.setText(String.format("%.2f", (ausgaben_monat + einnahmen_monat)));

        return view;
    }

    //copied from https://stackoverflow.com/questions/7175873/click-effect-on-button-in-android
    public static void buttonEffect(View button){
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
