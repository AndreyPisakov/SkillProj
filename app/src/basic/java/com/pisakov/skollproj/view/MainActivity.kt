package com.pisakov.skollproj.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.pisakov.skillproj.App
import com.pisakov.skillproj.R
import com.pisakov.skillproj.databinding.ActivityMainBinding
import com.pisakov.skillproj.receivers.ConnectionChecker
import com.pisakov.skillproj.view.fragments.HomeFragment
import com.pisakov.skillproj.view.fragments.LaterFragment
import com.pisakov.skillproj.view.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiver = ConnectionChecker()
        val filters = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_BATTERY_LOW)
        }
        registerReceiver(receiver, filters)

        initNavigation()
        promo()
    }

    private fun promo() {
        if (!App.instance.isPromoShown) {
            val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build()
            firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            firebaseRemoteConfig.fetch()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        firebaseRemoteConfig.activate()
                        val filmLink = firebaseRemoteConfig.getString("film_link")
                        if (filmLink.isNotBlank()) {
                            App.instance.isPromoShown = true
                            binding.promoViewGroup.apply {
                                visibility = View.VISIBLE
                                animate()
                                    .setDuration(1500)
                                    .alpha(1f)
                                    .start()
                                setLinkForPoster(filmLink)
                                watchButton.setOnClickListener {
                                    visibility = View.GONE
                                }
                            }
                        }
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun initNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    val tag = "home"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: HomeFragment(), tag)
                    true
                }
                R.id.favoritesFragment -> {
                    Toast.makeText(this, "Доступно в Pro версии", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.searchFragment -> {
                    val tag = "search"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: SearchFragment(), tag)
                    true
                }
                R.id.selectionsFragment -> {
                    Toast.makeText(this, "Доступно в Pro версии", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.laterFragment -> {
                    val tag = "watch_later"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment( fragment?: LaterFragment(), tag)
                    true
                }
                else -> false
            }
        }
    }

    private fun checkFragmentExistence(tag: String): Fragment? = supportFragmentManager.findFragmentByTag(tag)

    @SuppressLint("CommitTransaction")
    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.myNavHostFragment, fragment, tag)
            .addToBackStack(null)
            .commit()
    }
}