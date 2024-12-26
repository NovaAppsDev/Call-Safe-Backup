package ir.novaapps.callsafebackup.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.data.domain.usecase.GetContactUseCase
import ir.novaapps.callsafebackup.databinding.HomeFragmentBinding
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.ContactViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate


    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        contactViewModel.loadContacts(requireContext())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                contactViewModel.contacts.collect{result->
                    Log.e("14145" , result.size.toString())
                }
            }
        }

    }


}