package ir.novaapps.callsafebackup

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var navHostFragment: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)

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
    }

    override fun onNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}