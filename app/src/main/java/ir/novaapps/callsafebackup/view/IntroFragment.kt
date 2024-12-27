package ir.novaapps.callsafebackup.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroFragment : BaseFragment<IntroFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> IntroFragmentBinding
        get() = IntroFragmentBinding::inflate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(800)
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.introFragment, inclusive = true)
                    .build()
                findNavController().navigate(R.id.homeFragment, null, navOptions)

            }
        }

    }

}