package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton back = (ImageButton)findViewById(R.id.backButton);

        back.setOnClickListener(view -> {
            onBackPressed();
        });

        EditText horsePower = findViewById(R.id.horsePower);
        Spinner engineMarkSpinner = findViewById(R.id.engineMarkSpinner);
        EditText numOfEngine = findViewById(R.id.numOfEngine);
        RadioButton mehKpp = findViewById(R.id.mehKpp);
        RadioButton autoKpp = findViewById(R.id.autoKpp);

        Button next = (Button)findViewById(R.id.nextNextView);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String horsePowerText = horsePower.getText().toString();
                String engineMark = engineMarkSpinner.getSelectedItem().toString();
                String numOfEngineText = numOfEngine.getText().toString();

                String transmissionType;
                if (mehKpp.isChecked()) {
                    transmissionType = "Механика";
                } else if (autoKpp.isChecked()) {
                    transmissionType = "Автомат";
                } else {
                    transmissionType = "";
                }

                Intent intent = new Intent(SecondActivity.this, personInfo.class);

                intent.putExtra("horsePower", horsePowerText);
                intent.putExtra("engineMark", engineMark);
                intent.putExtra("numOfEngine", numOfEngineText);
                intent.putExtra("transmissionType", transmissionType);

                intent.putExtras(getIntent().getExtras());

                startActivity(intent);
            }
        });
    }
}