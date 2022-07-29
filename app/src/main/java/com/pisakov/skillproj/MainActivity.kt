package com.pisakov.skillproj

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pisakov.skillproj.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()

        val navController = findNavController(R.id.myNavHostFragment)
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun initNavigation() {
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, R.string.settings, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.watch_later -> {
                    Toast.makeText(this@MainActivity, R.string.later, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.selections -> {
                    Toast.makeText(this@MainActivity, R.string.selections, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}