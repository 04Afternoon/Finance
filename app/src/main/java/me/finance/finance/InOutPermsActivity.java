package me.finance.finance;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.finance.finance.Model.Category;
import me.finance.finance.Model.Intake;
import me.finance.finance.Model.Payment;
import me.finance.finance.Model.Permanent;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static me.finance.finance.Utils.convertDate;

public class InOutPermsActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public static final String IS_OUT_GOING = "outgoing";
    public static final String IS_STANDING_ORDER = "standingOrder";

    private ImageButton exitButton;
    private Button finishButton;
    private DatabaseHandler databaseHandler;
    private Spinner categorySpinner, paymentSpinner;
    private ArrayList<Category> categoryList;
    private ArrayList<Payment> paymentList;
    private Toolbar myToolbar;


    private EditText name_text_field;
    private EditText value_text_field;
    private EditText start_date_text_field;

    private RadioButton intakeRadioButton;
    private RadioButton outgoingRadioButton;
    private RadioGroup radioGroup;


    private RadioButton weeklyRadioButton;
    private RadioButton monthlyRadioButton;
    private RadioGroup radioGroup_weekly_monthly;

    private TextView endDateLabel;
    private TextView intervallabel;

    private Button takePicture;
    private ImageView imageView;
    private String filePath = "";


    private boolean outGoing = false;
    private boolean isPermanent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_perms);
        outGoing = getIntent().getBooleanExtra(IS_OUT_GOING, false);
        isPermanent = getIntent().getBooleanExtra(IS_STANDING_ORDER, false);

        name_text_field = findViewById(R.id.name_text_field);
        value_text_field = findViewById(R.id.value_text_field);
        start_date_text_field = findViewById(R.id.start_text_field);
        endDateLabel = findViewById(R.id.endDateLabel);
        intervallabel = findViewById(R.id.intervalLabel);
        takePicture = findViewById(R.id.takePictureButton);
        imageView = findViewById(R.id.receiptImage);
        takePicture.setOnClickListener((view) -> {
//            ImagePicker
//                    .create(this)
//                    .start();
            EasyImage.openChooserWithGallery(InOutPermsActivity.this, "Choose your receipt", 0);
        });




        final EditText end_date_text_field = findViewById(R.id.end_text_field);

        Calendar calendar = Calendar.getInstance();
        start_date_text_field.setText(Utils.convertDate(calendar.getTime()));
        start_date_text_field.setOnClickListener(view -> {
            calendar.setTime(Utils.convertDate(start_date_text_field.getText().toString()));
            DatePickerDialog datePickerDialog = new DatePickerDialog(InOutPermsActivity.this);
            datePickerDialog.getDatePicker().init(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),null);
            if(isPermanent){
                datePickerDialog.getDatePicker().setMaxDate( Utils.convertDate(end_date_text_field.getText().toString()).getTime());
            }
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year,month,day);
                start_date_text_field.setText(Utils.convertDate(calendar1.getTime()));
            });
            datePickerDialog.show();
        });



        Calendar calendar_end = Calendar.getInstance();
        end_date_text_field.setText(Utils.convertDate(calendar.getTime()));
        end_date_text_field.setOnClickListener(view -> {
            calendar_end.setTime(Utils.convertDate(end_date_text_field.getText().toString()));
            DatePickerDialog datePickerDialog = new DatePickerDialog(InOutPermsActivity.this);
            datePickerDialog.getDatePicker().init(calendar_end.get(Calendar.YEAR),calendar_end.get(Calendar.MONTH),calendar_end.get(Calendar.DAY_OF_MONTH),null);
            if(isPermanent){
                datePickerDialog.getDatePicker().setMinDate( Utils.convertDate(start_date_text_field.getText().toString()).getTime());
            }
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year,month,day);
                end_date_text_field.setText(Utils.convertDate(calendar1.getTime()));
            });
            datePickerDialog.show();
        });


        intakeRadioButton = findViewById(R.id.radioButtonIncome);
        outgoingRadioButton = findViewById(R.id.radioButtonOutgoing);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);



        weeklyRadioButton = findViewById(R.id.radioButtonWeekly);
        monthlyRadioButton = findViewById(R.id.radioButtonMonthly);
        radioGroup_weekly_monthly = findViewById(R.id.radioGroup2);
        radioGroup_weekly_monthly.setOnCheckedChangeListener(this);


        if (outGoing) {
            outgoingRadioButton.setChecked(true);
            value_text_field.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_action_minus), null, null, null);
        } else {
            intakeRadioButton.setChecked(true);
            value_text_field.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_action_add), null, null, null);
        }

        databaseHandler = DatabaseHandler.getInstance(this);
        databaseHandler.open();

        getSupportActionBar().hide();

        myToolbar = (Toolbar) findViewById(R.id.toolbar_inoutperms);




        List<String> categorySpinnerList = new ArrayList<String>();
        categoryList = databaseHandler.getCategories();
        categorySpinnerList.add("no category");
        for (int i = 0; i < categoryList.size(); i++) {
            categorySpinnerList.add(categoryList.get(i).getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorySpinnerList);
        categorySpinner = findViewById(R.id.category_spinner);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayList<String> paymentSpinnerList = new ArrayList<String>();
        paymentList = databaseHandler.getPayments();
        for (int i = 0; i < paymentList.size(); i++) {
            paymentSpinnerList.add(paymentList.get(i).getName());
        }
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentSpinnerList);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentSpinner = findViewById(R.id.payment_opt_spinner);
        paymentSpinner.setAdapter(paymentAdapter);

        if (!isPermanent) {
            end_date_text_field.setVisibility(View.GONE);
            radioGroup_weekly_monthly.setVisibility(View.GONE);
            monthlyRadioButton.setVisibility(View.GONE);
            weeklyRadioButton.setVisibility(View.GONE);
            endDateLabel.setVisibility(View.GONE);
            intervallabel.setVisibility(View.GONE);
        }

        exitButton = findViewById(R.id.auftrag_exit_button);
        FragmentBalance.buttonEffect(exitButton);
        exitButton.setOnClickListener(view -> finish());

        finishButton = findViewById(R.id.auftrag_finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_text_field.getText().toString();
                String value_string = value_text_field.getText().toString();
                String startDate = start_date_text_field.getText().toString();
                String endDate = end_date_text_field.getText().toString();





                String error = "";

                double value = 0;
                if (!value_string.isEmpty()) {
                    value = Double.valueOf(value_string);
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if (!startDate.isEmpty()) {
                    try {
                        dateFormat.parse(startDate);
                    } catch (ParseException e) {
                        error += "Invalid startDate";
                    }
                }
                if (!endDate.isEmpty()) {
                    try {
                        dateFormat.parse(endDate);
                    } catch (ParseException e) {
                        if (!error.isEmpty()) {
                            error += "\n";
                        }
                        error += "Invalid endDate";
                    }
                }

                if (error.isEmpty()) {
                    DatabaseHandler database = DatabaseHandler.getInstance(getApplicationContext());
                    //database.open();
                    if (!isPermanent && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Transaction saved", Toast.LENGTH_SHORT);

                        if (outGoing) {
                            value *= -1;
                        }
                        String category = categorySpinner.getSelectedItem().toString();
                        Integer categoryId = null;
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryList.get(i).getName().equals(category)) {
                                categoryId = categoryList.get(i).getId();
                                break;
                            }
                        }

                        String payment = paymentSpinner.getSelectedItem().toString();
                        Integer paymentId = 0;
                        for (int i = 0; i < paymentList.size(); i++) {
                            if (paymentList.get(i).getName().equals(payment)) {
                                paymentId = paymentList.get(i).getId();
                                break;
                            }
                        }

                        database.addIntake(new Intake(value, startDate, name, filePath, categoryId, paymentId));
                        toast.show();
                        System.out.println("DEBUG: !Once! " + name + " " + value + " " + startDate + " " + categoryId + " " + "ONCE" + " " + paymentId);
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else if (isPermanent && !name.isEmpty() && !value_string.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Standing order saved!", Toast.LENGTH_SHORT);

                        if (outGoing) {
                            value *= -1;
                        }

                        String category = categorySpinner.getSelectedItem().toString();
                        Integer categoryId = null;
                        for (int i = 0; i < categoryList.size(); i++) {
                            if (categoryList.get(i).getName().equals(category)) {
                                categoryId = categoryList.get(i).getId();
                                break;
                            }
                        }

                        String payment = paymentSpinner.getSelectedItem().toString();
                        Integer paymentId = null;
                        for (int i = 0; i < paymentList.size(); i++) {
                            if (paymentList.get(i).getName().equals(payment)) {
                                paymentId = paymentList.get(i).getId();
                                break;
                            }
                        }

                        Calendar next_exec = Calendar.getInstance();
                        next_exec.setTime(convertDate(startDate));
                        Calendar endCal = Calendar.getInstance();
                        endCal.setTime(convertDate(endDate));
                        if (monthlyRadioButton.isChecked()) {
                            Calendar currentDate = Calendar.getInstance();
                            currentDate.set(Calendar.HOUR, 23);
                            currentDate.set(Calendar.MINUTE, 59);
                            currentDate.set(Calendar.SECOND, 59);
                            while (next_exec.before(currentDate)) {
                                database.addIntake(new Intake(value, next_exec.getTime(), name, filePath, categoryId, paymentId));
                                next_exec.add(Calendar.MONTH, 1);

                                if (next_exec.after(endCal)) {
                                    break;
                                }
                            }
                        } else {
                            Calendar currentDate = Calendar.getInstance();
                            currentDate.set(Calendar.HOUR, 23);
                            currentDate.set(Calendar.MINUTE, 59);
                            currentDate.set(Calendar.SECOND, 59);
                            while (next_exec.before(currentDate)) {
                                database.addIntake(new Intake(value, next_exec.getTime(), name, filePath, categoryId, paymentId));
                                next_exec.add(Calendar.DAY_OF_YEAR, 7);

                                if (next_exec.after(endCal)) {
                                    break;
                                }
                            }
                        }

                        database.addPermanet(new Permanent(value, convertDate(startDate), monthlyRadioButton.isChecked()?"M":"W", convertDate(endDate), name, filePath, categoryId, paymentId, next_exec.getTime()));

                        toast.show();
                        System.out.println("DEBUG: !Permanent! " + name + " " + value_string + " " + startDate + " " + endDate + " " + (monthlyRadioButton.isChecked()?"M":"W") + " " + 0);
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast toastError = Toast.makeText(getApplicationContext(), "Fields with * are required", Toast.LENGTH_SHORT);
                        toastError.show();
                        System.out.println("DEBUG:" + getIntent().getBooleanExtra("intake", false));

                    }
                    //database.close();
                } else {
                    Toast toastError = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                    toastError.show();
                }
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonIncome:
                value_text_field.setTextColor(Color.BLACK);
                value_text_field.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_action_add), null, null, null);
                outGoing = false;
                break;
            case R.id.radioButtonOutgoing:
                value_text_field.setTextColor(Color.RED);
                value_text_field.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_action_minus), null, null, null);
                outGoing = true;
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                File imageFile = imageFiles.get(0);
                filePath = imageFile.getAbsolutePath();
                Picasso
                        .get()
                        .load(imageFile)
                        .fit()
                        .centerInside()
                        .into(imageView);
            }
        });
    }

}
