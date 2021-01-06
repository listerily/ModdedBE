package net.listerily.moddedbe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onStartGameClicked() {
        startActivity(Intent(this, InitializingActivity::class.java))
        finish()
    }

    fun onMenuClicked() {
        startActivity(Intent(this, OptionsActivity::class.java))
    }
}