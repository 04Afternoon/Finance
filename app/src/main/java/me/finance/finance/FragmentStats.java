package me.finance.finance;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

//TODO http://www.android-graphview.org/ API EINBINDEN

public class FragmentStats extends Fragment {

    private Context context;

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

        ImageButton calender_button = view.findViewById(R.id.stats_calender_button);
        ImageButton menu_button = view.findViewById(R.id.stats_menu_button);

        buttonEffect(calender_button);
        buttonEffect(menu_button);

        calender_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "TODO: IMPLEMENT CALENDER BUTTON", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context, "TODO: IMPLEMENT MENU BUTTON", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

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
