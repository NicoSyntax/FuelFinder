package de.syntax.androidabschluss

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import de.syntax.androidabschluss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHost.navController)

        navHost.navController.addOnDestinationChangedListener{_, destination, _, ->
            when(destination.id) {
                R.id.loginFragment -> binding.bottomNavigationView.visibility = View.GONE
                R.id.signupFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }
}
