package com.alroy214.autolocation

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.alroy214.autolocation.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            sneakCurrentLocation(view)
        }
    }


    private fun sneakRoot(view: View) {
        sneakMessage(view, "Root Permission: " + if (isRootGiven()) "Granted" else "Denied")
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                sneakRoot(binding.root)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun isRootAvailable(): Boolean {
        for (pathDir in System.getenv("PATH")?.split(":")?.toTypedArray()!!) {
            if (File(pathDir, "su").exists()) {
                return true
            }
        }
        return false
    }

    private fun isRootGiven(): Boolean {
        if (isRootAvailable()) {
            var process: Process? = null
            try {
                process = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
                val `in` = BufferedReader(InputStreamReader(process.inputStream))
                val output: String = `in`.readLine()
                if (output.lowercase(Locale.getDefault()).contains("uid=0")) {
                    return true
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                process?.destroy()
            }
        }
        return false
    }

    companion object {
        private fun sneakMessage(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

        fun sneakCurrentLocation(view: View) {
            sneakMessage(view, "Current Location: ${isLocationEnabled(view.context)}")
        }

        fun isLocationEnabled(context: Context?): Boolean {
            if(context == null) return false
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return isLocationEnabled(locationManager)
        }
    }
}