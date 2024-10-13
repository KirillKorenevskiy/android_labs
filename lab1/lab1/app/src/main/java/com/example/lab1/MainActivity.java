package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayText();

        for(int i = 0; i < 10; i++){
            Log.d("MainActivity", "counter =" + i);
        }
    }

    private void displayText() {
        TextView nt= findViewById(R.id.newtest);
        nt.setText(new test.TextFunction().getValue());
    }
}