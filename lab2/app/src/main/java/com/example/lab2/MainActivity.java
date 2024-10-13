package com.example.lab2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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

        TextView nameTextView = findViewById(R.id.editTextText);
        Spinner daySpinner = findViewById(R.id.daySpinner);
        Spinner monthSpinner = findViewById(R.id.monthSpinner);
        Spinner hourSpinner = findViewById(R.id.hourSpinner);
        Spinner minuteSpinner = findViewById(R.id.minuteSpinner);
        CheckBox unknownTimeCheckbox = findViewById(R.id.checkBox);
        TextView resultField = findViewById(R.id.textViewResult);
        Spinner countrySpinner = findViewById(R.id.citySpinner);

        Map<Integer, String> horoscopes = new HashMap<Integer,String>() {{
            put(1, "Сегодня будет удачный день для новых начинаний. Поверьте в себя!");
            put(2, "Встреча с старыми друзьями подарит вам радость и вдохновение.");
            put(3, "Будьте осторожны с финансами сегодня. Планируйте свои траты внимательно.");
            put(4, "Любовь будет цветущей сегодня. Уделите внимание близким людям.");
            put(5, "Энергия и оптимизм будут сопровождать вас весь день. Используйте их в делах.");
            put(6, "Загадайте желание. Ваше желание может исполниться в ближайшее время.");
            put(7, "Сегодня подойдет для саморазвития. Изучайте что-то новое и интересное.");
            put(8, "Будьте внимательны к здоровью. Занимайтесь спортом и правильным питанием.");
            put(9, "Планы на будущее начнут принимать форму. Действуйте на них.");
        }};

        Button button1 = findViewById(R.id.confirmButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedDay = daySpinner.getSelectedItemPosition() + 1;
                int selectedMonth = monthSpinner.getSelectedItemPosition() + 1;
                String selectedYear = ((EditText) findViewById(R.id.editTextDate)).getText().toString();

                CharSequence name = nameTextView.getText();

                resultField.setText(name);

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Заполните поле с именем",Toast.LENGTH_LONG).show();
                    return;
                }



                if(selectedYear.isEmpty()){
                    resultField.setText("Заполните поле с годом");
                    resultField.setTextColor(Color.RED);
                    return;
                }

                try {
                    int year = Integer.parseInt(selectedYear);
                    if (year >= 1900 && year <= 2024) {
                    } else {
                        resultField.setText("Введите реальный возраст");
                        resultField.setTextColor(Color.RED);
                        return;
                    }
                } catch (Exception e) {

                }

                int selectedHour = hourSpinner.getSelectedItemPosition();
                int selectedMinute = minuteSpinner.getSelectedItemPosition();
                boolean unknownTimeChecked = unknownTimeCheckbox.isChecked();
                String selectedCountry = (String)countrySpinner.getSelectedItem();

                int daySum;
                int monthSum;
                int yearSum = 0;
                int yearFinalSum;
                int hourSum;
                int minuteSum;
                int finalResult = 0;

                if (selectedDay >= 10) {
                    daySum = selectedDay / 10 + selectedDay % 10;
                } else {
                    daySum = selectedDay;
                }

                if (selectedMonth >= 10) {
                    monthSum = selectedMonth / 10 + selectedMonth % 10;
                } else {
                    monthSum = selectedMonth;
                }

                for (int i = 0; i < selectedYear.length(); i++) {
                    yearSum += Character.getNumericValue(selectedYear.charAt(i));
                }

                if (yearSum >= 10) {
                    yearFinalSum = yearSum / 10 + yearSum % 10;
                } else {
                    yearFinalSum = yearSum;
                }

                if (selectedHour >= 10) {
                    hourSum = selectedHour / 10 + selectedHour % 10;
                } else {
                    hourSum = selectedHour;
                }

                if (selectedMinute >= 10) {
                    minuteSum = selectedMinute / 10 + selectedMinute % 10;
                } else {
                    minuteSum = selectedMinute;
                }

                if(unknownTimeChecked == true){
                    hourSum = 1;
                    minuteSum = 1;
                }

                int result = daySum + monthSum + yearFinalSum + hourSum + minuteSum;

                if(selectedCountry == "Беларусь"){
                    result+=1;
                }else if(selectedCountry == "Украина"){
                    result+=2;
                }

                if (result >= 10) {
                    finalResult = result / 10 + result % 10;
                } else {
                    finalResult = result;
                }

                String valueResult = horoscopes.get(finalResult);

                resultField.setText(valueResult);
                resultField.setTextColor(Color.BLACK);
            }
        });
    }
}