package me.finance.finance;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import me.finance.finance.Model.Payment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSettings extends Fragment {

    private Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getContext();
    }

    public FragmentSettings() {
        // Required empty public constructor
    }

    private DatabaseHandler databaseHandler = DatabaseHandler.getInstance(getContext());


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button categories = view.findViewById(R.id.categories_settings_button);
        Button accounts = view.findViewById(R.id.accounts_settings_button);
        Button clear_database = view.findViewById(R.id.clear_database_button);

        final Intent intent = new Intent(getActivity(), CategoryActivity.class);

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               intent.putExtra("settings", "manage categories");
               startActivity(intent);
            }
        });

        accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("settings", "manage accounts");
                startActivity(intent);
            }
        });

        clear_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(FragmentSettings.this.getContext())
                                .setTitle("Delete Database")
                                .setMessage("Do you want to delete the Database?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    databaseHandler.deleteTableContents();
                                    databaseHandler.addPayment(new Payment("Cash"));
                                    Toast toast = Toast.makeText(view.getContext(), "Database cleared successfully!", Toast.LENGTH_SHORT);
                                    toast.show();
                                })
                                .setNegativeButton("No", (dialog, which) -> dialog.cancel());

                alertDialogBuilder.show();

            }
        });
        return view;
    }

}
