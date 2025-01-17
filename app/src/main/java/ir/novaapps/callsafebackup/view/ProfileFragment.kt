package ir.novaapps.callsafebackup.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.databinding.HomeFragmentBinding
import ir.novaapps.callsafebackup.databinding.ProfileFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> ProfileFragmentBinding
        get() = ProfileFragmentBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}