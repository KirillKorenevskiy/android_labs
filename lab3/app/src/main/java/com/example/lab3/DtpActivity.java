package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dtp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageButton back = (ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            onBackPressed();
        });

        TextView dtpCity = findViewById(R.id.dtpCity);
        Spinner dtpCharacterSpinner = findViewById(R.id.dtpCharacterSpinner);

        Button next = (Button)findViewById(R.id.nextNextView);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dtpCityText = dtpCity.getText().toString();
                String dtpCharacter = dtpCharacterSpinner.getSelectedItem().toString();

                Intent intent = new Intent(DtpActivity.this, SecondActivity.class);

                intent.putExtra("dtpCity", dtpCityText);
                intent.putExtra("dtpCharacter", dtpCharacter);

                intent.putExtras(getIntent().getExtras());

                startActivity(intent);
            }
        });
    }
}