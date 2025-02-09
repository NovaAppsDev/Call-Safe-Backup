package ir.novaapps.callsafebackup

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.databinding.ActivityMainBinding
import ir.novaapps.callsafebackup.utils.SharedPreferences


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreferences = SharedPreferences(this)
        val darkTheme = sharedPreferences.getDetailBoolean("statusTheme", false)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navHostFragment.navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.introFragment->{
                    binding.navigationBottom.isVisible = false
                    binding.topAppBar.isVisible = false
                }

                R.id.permissionFragment->{
                    binding.navigationBottom.isVisible = false
                    binding.topAppBar.isVisible = false
                }

                else->{
                    binding.navigationBottom.isVisible = true
                    binding.topAppBar.isVisible = true
                }
            }
        }

        binding.navigationBottom.setupWithNavController(navHostFragment.navController)

        setContentView(binding.root)

        if (!darkTheme) {
            setThemeMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            setThemeMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.btnChangeTheme.setOnClickListener {
            toggleTheme()
        }

    }

    override fun onNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun AppCompatActivity.setThemeMode(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        delegate.applyDayNight()
    }

    private fun toggleTheme() {
        val isDarkTheme = sharedPreferences.getDetailBoolean("statusTheme", false)
        sharedPreferences.setDetailBoolean("statusTheme", !isDarkTheme)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        )
        recreate()
    }


}