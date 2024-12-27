package ir.novaapps.callsafebackup.view.contact

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
import ir.novaapps.callsafebackup.databinding.ContactFragmentBinding
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : BaseFragment<ContactFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> ContactFragmentBinding
        get() = ContactFragmentBinding::inflate


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