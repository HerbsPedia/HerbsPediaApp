package id.zamzam.herbspedia.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomAppBar = findViewById(R.id.nav)
        setSupportActionBar(bottomAppBar)


        // Set up BottomNavigationView with NavController
        bottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        // Set up FloatingActionButton to navigate to ScanFragment
        binding.fab.setOnClickListener {
            navController.navigate(R.id.scanFragment)
        }

        // Set up item selection listener for BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.nav_blank -> {
                    navController.navigate(R.id.scanFragment)
                    true
                }
                R.id.nav_chat -> {
                    navController.navigate(R.id.chatFragment)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
        // Hide back button in bottomNavigationView when on startDestination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isMainDestination =
                    destination.id == R.id.listPlantFragment ||
                            destination.id == R.id.detailPlantFragment ||
                    destination.id == R.id.homeFragment ||
                    destination.id == R.id.searchFragment ||
                    destination.id == R.id.chatFragment ||
                    destination.id == R.id.scanFragment ||
                            destination.id == R.id.detailScanFragment ||
                    destination.id == R.id.profileFragment
            if (isMainDestination) {
                bottomNavigationView.visibility = View.VISIBLE
                bottomAppBar.visibility = View.VISIBLE
            } else {
                bottomNavigationView.visibility = View.GONE
                bottomAppBar.visibility = View.GONE
            }
        }
    }
}