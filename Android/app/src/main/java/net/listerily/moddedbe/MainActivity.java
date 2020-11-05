package net.listerily.moddedbe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartGameClicked(View view) {
        startActivity(new Intent(this, InitializingActivity.class));
        finish();
    }

    public void onMenuClicked(View view) {
    }
}