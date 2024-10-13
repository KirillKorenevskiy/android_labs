package com.example.lab3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    private static final String CAR_MARK_KEY = "carMark";
    private static final String CAR_MODEL_KEY = "carModel";
    private static final String CAR_YEAR_KEY = "carYear";
    private static final String CAR_RUN_KEY = "carRun";

    private String carMarkText = "";
    private String carModelText = "";
    private String carYearText = "";
    private String carRunText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null) {
            carMarkText = savedInstanceState.getString(CAR_MARK_KEY);
            carModelText = savedInstanceState.getString(CAR_MODEL_KEY);
            carYearText = savedInstanceState.getString(CAR_YEAR_KEY);
            carRunText = savedInstanceState.getString(CAR_RUN_KEY);
        }

        EditText carModel = findViewById(R.id.carModel);
        EditText carMark = findViewById(R.id.carMark);
        EditText carYear = findViewById(R.id.carYear);
        EditText carRun = findViewById(R.id.carRun);
        CheckBox ch1 = (CheckBox)findViewById(R.id.chBox);



        carYear.setOnTouchListener((view, motionEvent) -> {
            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (carYear.getRight() - carYear.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                            (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                                carYear.setText(String.valueOf(selectedYear));
                            }, year, 0, 1);

                    datePickerDialog.show();

                    Button okButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE);
                    Button cancelButton = datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE);
                    okButton.setTextColor(Color.BLACK);
                    cancelButton.setTextColor(Color.BLACK);

                    return true;
                }
            }
            return false;
        });

        Button next = (Button)findViewById(R.id.nextView);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {
                    String carMarkText = carMark.getText().toString();
                    String carModelText = carModel.getText().toString();
                    String carYearText = carYear.getText().toString();
                    String carRunText = carRun.getText().toString();

                    boolean isCarInAccident = ch1.isChecked();

                    Intent intent;

                    if (isCarInAccident) {
                        intent = new Intent(MainActivity.this, DtpActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, SecondActivity.class);
                    }

                    intent.putExtra("carMark", carMarkText);
                    intent.putExtra("carModel", carModelText);
                    intent.putExtra("carYear", carYearText);
                    intent.putExtra("carRun", carRunText);

                    startActivity(intent);
                }
            });
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(CAR_MARK_KEY, carMarkText);
        outState.putString(CAR_MODEL_KEY, carModelText);
        outState.putString(CAR_YEAR_KEY, carYearText);
        outState.putString(CAR_RUN_KEY, carRunText);
        super.onSaveInstanceState(outState);
    }

}