package ir.novaapps.callsafebackup.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.HomeFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.MainViewModel
import ir.novaapps.ui.DialogProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate


    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel.loadContacts(requireContext())
        mainViewModel.loadCallLogs(requireContext(), 0)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnFastBackup.setOnClickListener {
                mainViewModel.exportAllBackup()
            }
        }

        mainViewModel.contacts.observe(viewLifecycleOwner) { result ->
            binding.totalAllContact.text = result.size.toString()
            Log.d("HomeFragment", "Total Contact" + result.size.toString())
        }

        mainViewModel.callLogs.observe(viewLifecycleOwner) { result ->
            binding.totalAllCallLog.text = result.size.toString()
            Log.d("HomeFragment", "Total Call Log" + result.size.toString())
        }

        mainViewModel.exportAllBackupResult.observe(viewLifecycleOwner){ result ->
            Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_SHORT).show()
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner){isLoading->
            if(!isLoading){
//                val dialog = Dialog(requireContext())
//                DialogProgress.resultDialog(dialog)
            }
        }

    }


}