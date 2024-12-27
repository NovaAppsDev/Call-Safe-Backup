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
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.MainViewModel
import kotlinx.coroutines.launch

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
        mainViewModel.loadCallLogs(requireContext(),0)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.contacts.collect{result->
                    binding.totalAllContact.text = result.size.toString()
                    Log.d("HomeFragment" ,"Total Contact" +   result.size.toString())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.callLogs.collect{result->
                    binding.totalAllCallLog.text = result.size.toString()
                    Log.d("HomeFragment" ,"Total Call Log" +   result.size.toString())
                }
            }
        }

    }

}