package net.listerily.moddedpepro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import net.listerily.moddedpepro.ui.BitmapRepeater;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_background);
        bitmap = BitmapRepeater.repeat(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), bitmap);
        getWindow().getDecorView().setBackground(new BitmapDrawable(bitmap));
    }
}