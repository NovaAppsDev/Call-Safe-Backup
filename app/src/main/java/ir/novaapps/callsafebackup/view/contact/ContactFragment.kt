package ir.novaapps.callsafebackup.view.contact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.ContactFragmentBinding
import ir.novaapps.callsafebackup.databinding.IntroFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : BaseFragment<ContactFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> ContactFragmentBinding
        get() = ContactFragmentBinding::inflate

    private val mainViewModel: MainViewModel by viewModels()

    private var contactAdapter: ContactAdapter = ContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel.loadContacts(requireContext())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.contacts.collect { list ->
                    contactAdapter.setUsers(list)
                    if (list.isEmpty()) {
                        updateUi(true)
                    } else {
                        updateUi(false)
                    }
                }
            }
        }
    }

    private fun updateUi(emptyLists: Boolean) {
        binding.apply {
            if (emptyLists) {
//                emptyList.isVisible = true
//                recyclerView.isVisible = false
            } else {
//                emptyList.isVisible = false
//                recyclerView.isVisible = true
                initRecyclerView()
            }
        }
    }

    private fun initRecyclerView() {
        binding.listContact.apply {
            // استفاده از GridLayoutManager با دو ستون
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contactAdapter
            contactAdapter.onItemClick { userEntity ->
            }
        }
    }


}