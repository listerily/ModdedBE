package net.listerily.moddedbe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onStartGameClicked(view: View) {
        startActivity(Intent(this, InitializingActivity::class.java))
        finish()
    }

    fun onMenuClicked(view: View) {
        startActivity(Intent(this, OptionsActivity::class.java))
    }
}