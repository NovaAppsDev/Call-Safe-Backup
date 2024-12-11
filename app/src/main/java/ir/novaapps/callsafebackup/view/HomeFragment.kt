package ir.novaapps.callsafebackup.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.HomeFragmentBinding
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}