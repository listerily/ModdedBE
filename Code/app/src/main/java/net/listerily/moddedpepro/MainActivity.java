package net.listerily.moddedpepro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.listerily.moddedpepro.ui.BitmapRepeater;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.activity_background);
        bitmap = BitmapRepeater.repeat(getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight(), bitmap);
        getWindow().getDecorView().setBackground(new BitmapDrawable(bitmap));
    }

    public void onStartGameClicked(View view) {
        startActivity(new Intent(this,com.mojang.minecraftpe.MainActivity.class));
    }

    public void onSettingsClicked(View view) {
        startActivity(new Intent(this,SettingsActivity.class));
    }

    public void onManageNModsClicked(View view) {
        startActivity(new Intent(this,ManageNModsActivity.class));
    }
}
