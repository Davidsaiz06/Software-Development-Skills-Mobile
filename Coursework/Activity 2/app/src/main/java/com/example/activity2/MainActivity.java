package com.example.activity2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Botón para abrir la segunda Activity enviando información
        Button secondActivityBTN = findViewById(R.id.secondActivityBTN);
        secondActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startIntent.putExtra("SOMETHING", "HELLO WORLD!");
                startActivity(startIntent);
            }
        });

        // 2. Botón para abrir el navegador web externo
        Button googleBtn = findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String google = "https://www.google.com";
                Uri webaddress = Uri.parse(google);
                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webaddress);

                try {
                    startActivity(gotoGoogle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}