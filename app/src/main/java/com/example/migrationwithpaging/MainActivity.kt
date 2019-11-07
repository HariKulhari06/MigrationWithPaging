package com.example.migrationwithpaging

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.migrationwithpaging.ui.withDB.WithDBActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        withDB.setOnClickListener {
            startActivity(Intent(this,WithDBActivity::class.java))
        }
    }

}
