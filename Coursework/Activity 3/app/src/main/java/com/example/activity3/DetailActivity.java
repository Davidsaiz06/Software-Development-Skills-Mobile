package com.example.activity3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        int index = intent.getIntExtra("com.example.activity3.ITEM_INDEX", -1);

        if (index > -1) {
            int imageResId = getImage(index);
            if (imageResId != -1) {
                ImageView imageView = findViewById(R.id.imageView);
                scaleImage(imageView, imageResId);
            }
        }
    }

    private int getImage(int index) {
        switch (index) {
            case 0: return R.drawable.peach;
            case 1: return R.drawable.tomato;
            case 2: return R.drawable.squash;
            default: return -1;
        }
    }

    private void scaleImage(ImageView imageView, int imageResId) {
        Display display = getWindowManager().getDefaultDisplay();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), imageResId, options);

        int imageWidth = options.outWidth;
        int screenWidth = display.getWidth();

        if (imageWidth > screenWidth) {
            int ratio = Math.round((float) imageWidth / (float) screenWidth);
            options.inSampleSize = ratio;
        }

        options.inJustDecodeBounds = false;
        Bitmap scaledImg = BitmapFactory.decodeResource(getResources(), imageResId, options);
        imageView.setImageBitmap(scaledImg);
    }
}