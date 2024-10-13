package com.example.lab3;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class afterResultList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_after_result_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton back = (ImageButton)findViewById(R.id.backButton);

        back.setOnClickListener(view -> {
            onBackPressed();
        });



        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Car> carData = loadCarData();
        carAdapter = new Adapter(carData);
        recyclerView.setAdapter(carAdapter);
    }

    private List<Car> loadCarData() {
        List<Car> carList = new ArrayList<>();
        StringBuilder json = new StringBuilder();
        try {
            FileInputStream fis = openFileInput("car_data.json");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                json.append(line);
            }

            Log.d("LoadCarData", "data: " + json.toString());

            if (json.length() == 0) {
                Log.e("LoadCarData", "empty");
                return carList;
            }
            JSONArray jsonArray = new JSONArray(json.toString());
            Log.d("LoadCarData", "length: " + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Car car = new Car(
                        jsonObject.getString("carMark"),
                        jsonObject.getString("carModel"),
                        jsonObject.getString("carYear"),
                        jsonObject.getString("carRun"),
                        jsonObject.getString("dtpCharacter"),
                        jsonObject.getString("dtpCity"),
                        jsonObject.getString("engineMark"),
                        jsonObject.getString("horsePower"),
                        jsonObject.getString("numOfEngine"),
                        jsonObject.getString("transmissionType"),
                        jsonObject.getString("phoneNumber"),
                        jsonObject.getString("email"),
                        jsonObject.getString("instLink"),
                        jsonObject.getString("imagePath")
                        );
                carList.add(car);
            }
        } catch (Exception e) {
            Log.e("LoadCarData", "Error loading car data: " + e.getMessage());
            e.printStackTrace();
        }
        return carList;
    }
}