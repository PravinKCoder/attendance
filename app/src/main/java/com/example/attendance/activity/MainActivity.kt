package com.example.attendance.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.attendance.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //supportActionBar?.title = "My Action Bar Title"
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        /*  supportFragmentManager.beginTransaction().apply {
              add(R.id.fragmentContainerView,HomeFragment())
              addToBackStack(null)
              commit()
          }*/
        /* val fragmentTransaction = supportFragmentManager.beginTransaction()
          fragmentTransaction.add(R.id.fragmentContainerView, HomeFragment())
          fragmentTransaction.addToBackStack(null)
          fragmentTransaction.commit()*/
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}