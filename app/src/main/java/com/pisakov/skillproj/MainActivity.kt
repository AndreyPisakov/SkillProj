package com.pisakov.skillproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pisakov.skillproj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btnMenu.setOnClickListener { Toast.makeText(this@MainActivity, R.string.menu, Toast.LENGTH_SHORT).show() }
            btnFavor.setOnClickListener { Toast.makeText(this@MainActivity, R.string.favor, Toast.LENGTH_SHORT).show() }
            btnLater.setOnClickListener { Toast.makeText(this@MainActivity, R.string.later, Toast.LENGTH_SHORT).show() }
            btnSelections.setOnClickListener { Toast.makeText(this@MainActivity, R.string.selections, Toast.LENGTH_SHORT).show() }
            btnSettings.setOnClickListener { Toast.makeText(this@MainActivity, R.string.settings, Toast.LENGTH_SHORT).show() }
        }
    }
}