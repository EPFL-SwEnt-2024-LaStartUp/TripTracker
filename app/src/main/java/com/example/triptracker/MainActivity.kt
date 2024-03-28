package com.example.triptracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.triptracker.view.LoginScreen
import com.example.triptracker.view.Navigation
import com.example.triptracker.view.Route
import com.example.triptracker.view.theme.TripTrackerTheme

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : ComponentActivity() {
  /*override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      // Set the trip tracker theme
      TripTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          // Instance of NavController
          val navController = rememberNavController()
          val navigation = remember(navController) { Navigation(navController) }

          // List of destinations for in app navigation
          NavHost(navController = navController, startDestination = Route.LOGIN) {
            composable(Route.LOGIN) {
              LoginScreen(navigation) // TODO change this once more screens are added
            }
          }
        }
      }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      val firstFragment=ProfileFragment()
      val secondFragment=MapsFragment()
      val thirdFragment=RecordFragment()
      val fourthFragment=ProfileFragment()

      setCurrentFragment(firstFragment)

      bottomNavigationView.setOnNavigationItemSelectedListener {
        when(it.itemId){
          R.id.home->setCurrentFragment(firstFragment)
          R.id.person->setCurrentFragment(secondFragment)
          R.id.settings->setCurrentFragment(thirdFragment)

        }
        true
      }

    }

    private fun setCurrentFragment(fragment:Fragment)=
      supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
        commit()
      }
  }
}
