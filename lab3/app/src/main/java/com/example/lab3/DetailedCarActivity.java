package com.example.lab3;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailedCarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton back = (ImageButton)findViewById(R.id.backButton);

        back.setOnClickListener(view -> {
            onBackPressed();
        });

        Intent intent = getIntent();
        String carMark = intent.getStringExtra("carMark");
        String carModel = intent.getStringExtra("carModel");
        String carYear = intent.getStringExtra("carYear");
        String carRun = intent.getStringExtra("carRun");
        String dtpCity = intent.getStringExtra("dtpCity");
        String dtpCharacter = intent.getStringExtra("dtpCharacter");
        String horsePower = intent.getStringExtra("horsePower");
        String engineMark = intent.getStringExtra("engineMark");
        String numOfEngine = intent.getStringExtra("numOfEngine");
        String transmissionType = intent.getStringExtra("transmissionType");
        String phoneNumber = intent.getStringExtra("email");
        String email = intent.getStringExtra("phoneNumber");
        String instLink = intent.getStringExtra("instLink");
        String imagePath = intent.getStringExtra("imagePath");

        TextView textCarMark = findViewById(R.id.textCarMark);
        TextView textCarModel = findViewById(R.id.textCarModel);
        TextView textCarYear = findViewById(R.id.textCarYear);
        TextView textCarRun = findViewById(R.id.textCarRun);
        TextView textDtpCity = findViewById(R.id.textDtpCity);
        TextView textDtpCharacter = findViewById(R.id.textDtpCharacter);
        TextView textHorsePower = findViewById(R.id.textHorsePower);
        TextView textEngineMark = findViewById(R.id.textEngineMark);
        TextView textNumOfEngine = findViewById(R.id.textNumOfEngine);
        TextView textTransmissionType = findViewById(R.id.textTransmissionType);
        TextView textPhoneNumber = findViewById(R.id.textPhoneNumber);
        TextView textEmail = findViewById(R.id.textEmail);
        TextView textInstLink = findViewById(R.id.textInstLink);
        ImageView image = findViewById(R.id.image);

        textCarMark.setText("Марка: " + carMark);
        textCarModel.setText("Модель: " + carModel);
        textCarYear.setText("Год выпуска: " + carYear);
        textCarRun.setText("Пробег: " + carRun);
        textDtpCity.setText("Город: " + dtpCity);
        textDtpCharacter.setText("Характер ДТП: " + dtpCharacter);
        textHorsePower.setText("Кол-во л.с.: " + horsePower);
        textEngineMark.setText("Марка двигателя: " + engineMark);
        textNumOfEngine.setText("Объём двигателя: " + numOfEngine);
        textTransmissionType.setText("Вид КПП: " + transmissionType);
        textPhoneNumber.setText("Телефон владельца: " + phoneNumber);
        textEmail.setText("Почта: " + email);
        textInstLink.setText("Ссылка на инст: " + instLink);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        image.setImageBitmap(bitmap);

        textPhoneNumber.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        textEmail.setOnClickListener(v -> {
            String message = "Марка: " + carMark + "\n" +
                    "Модель: " + carModel + "\n" +
                    "Год выпуска: " + carYear + "\n" +
                    "Пробег: " + carRun + "\n" +
                    "Город: " + dtpCity + "\n" +
                    "Характер ДТП: " + dtpCharacter + "\n" +
                    "Кол-во л.с.: " + horsePower + "\n" +
                    "Марка двигателя: " + engineMark + "\n" +
                    "Объём двигателя: " + numOfEngine + "\n" +
                    "Вид КПП: " + transmissionType + "\n" +
                    "Телефон владельца: " + phoneNumber + "\n" +
                    "Ссылка на инст: " + instLink;

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Информация об автомобиле");
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(emailIntent, "фваыфав"));
        });

        final String finalInstLink = instLink;
        textInstLink.setOnClickListener(v -> {
            String link = finalInstLink;
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "https://" + link;
            }
            Uri uri = Uri.parse(link);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(browserIntent);
        });
    }
}