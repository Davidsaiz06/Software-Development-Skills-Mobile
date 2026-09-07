package com.example.myfirstapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        EditText editNum1 = findViewById(R.id.editNum1);
        EditText editNum2 = findViewById(R.id.editNum2);
        Button btnAdd = findViewById(R.id.btnAdd);
        TextView tvResult = findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNum1 = editNum1.getText().toString();
                String strNum2 = editNum2.getText().toString();

                if (!strNum1.isEmpty() && !strNum2.isEmpty()) {
                    double num1 = Double.parseDouble(strNum1);
                    double num2 = Double.parseDouble(strNum2);
                    double sum = num1 + num2;
                    tvResult.setText(String.valueOf(sum));
                } else {
                    tvResult.setText("Numbers are missing");
                }
            }
        });
    }
}