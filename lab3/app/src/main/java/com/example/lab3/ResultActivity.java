package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private String carMark, carModel, carYear, carRun, dtpCity, dtpCharacter, horsePower, engineMark, numOfEngine, transmissionType, email, phone, instLink, imagePath;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultText = findViewById(R.id.car_info);
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> onBackPressed());
        Button nextButton = findViewById(R.id.nextView);


        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                restoreFromExtras(extras);
            }
        }

        updateResultText();

        Button save = findViewById(R.id.save);
        save.setOnClickListener(view -> {
            Car car = new Car(carMark, carModel, carYear, carRun, dtpCity, dtpCharacter, horsePower, engineMark, numOfEngine, transmissionType, email, phone, instLink, imagePath);
            saveCarToJsonFile(car);
            Intent intent = new Intent(ResultActivity.this, afterResultList.class);
            startActivity(intent);
        });

        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(ResultActivity.this, afterResultList.class);
            startActivity(intent);
        });
    }

    private void restoreFromExtras(Bundle extras) {
        carMark = extras.getString("carMark");
        carModel = extras.getString("carModel");
        carYear = extras.getString("carYear");
        carRun = extras.getString("carRun");
        dtpCity = extras.getString("dtpCity");
        dtpCharacter = extras.getString("dtpCharacter");
        horsePower = extras.getString("horsePower");
        engineMark = extras.getString("engineMark");
        numOfEngine = extras.getString("numOfEngine");
        transmissionType = extras.getString("transmissionType");
        phone = extras.getString("phone");
        email = extras.getString("email");
        instLink = extras.getString("inst");
        imagePath = extras.getString("imagePath");
    }

    private void restoreState(Bundle savedInstanceState) {
        carMark = savedInstanceState.getString("carMark");
        carModel = savedInstanceState.getString("carModel");
        carYear = savedInstanceState.getString("carYear");
        carRun = savedInstanceState.getString("carRun");
        dtpCity = savedInstanceState.getString("dtpCity");
        dtpCharacter = savedInstanceState.getString("dtpCharacter");
        horsePower = savedInstanceState.getString("horsePower");
        engineMark = savedInstanceState.getString("engineMark");
        numOfEngine = savedInstanceState.getString("numOfEngine");
        transmissionType = savedInstanceState.getString("transmissionType");
        phone = savedInstanceState.getString("phone");
        email = savedInstanceState.getString("email");
        instLink = savedInstanceState.getString("inst");
    }

    private void updateResultText() {
        String result = "Марка: " + carMark + "\n" +
                "Модель: " + carModel + "\n" +
                "Год выпуска: " + carYear + "\n" +
                "Пробег: " + carRun + "\n\n" +
                "Информация о ДТП:\n" +
                "Город: " + dtpCity + "\n" +
                "Характер ДТП: " + dtpCharacter + "\n\n" +
                "Кол-во л.с.: " + horsePower + "\n" +
                "Марка двигателя: " + engineMark + "\n" +
                "Объём двигателя: " + numOfEngine + "\n" +
                "Вид КПП: " + transmissionType + "\n\n" +
                "Телефон владельца: " + phone + "\n" +
                "Почта: " + email + "\n" +
                "Ссылка на инст: " + instLink + "\n" +
                "Путь к изображению" + imagePath + "\n";

        resultText.setText(result);
    }

    private void saveCarToJsonFile(Car car) {
        Gson gson = new Gson();
        File file = new File(getFilesDir(), "car_data.json");

        List<Car> carList = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Type carListType = new TypeToken<ArrayList<Car>>() {}.getType();
                carList = gson.fromJson(reader, carListType);
                if (carList == null) {
                    carList = new ArrayList<>();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error reading existing data", Toast.LENGTH_SHORT).show();
            }
        }

        carList.add(car);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String json = gson.toJson(carList);
            writer.write(json);
            Toast.makeText(this, "Car data saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("carMark", carMark);
        outState.putString("carModel", carModel);
        outState.putString("carYear", carYear);
        outState.putString("carRun", carRun);
        outState.putString("dtpCity", dtpCity);
        outState.putString("dtpCharacter", dtpCharacter);
        outState.putString("horsePower", horsePower);
        outState.putString("engineMark", engineMark);
        outState.putString("numOfEngine", numOfEngine);
        outState.putString("transmissionType", transmissionType);
    }
}