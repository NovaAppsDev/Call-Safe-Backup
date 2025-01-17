package ir.novaapps.callsafebackup.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.databinding.HistoryFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment

@AndroidEntryPoint
class HistoryFragment : BaseFragment<HistoryFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> HistoryFragmentBinding
        get() = HistoryFragmentBinding::inflate

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