package ir.novaapps.callsafebackup.view.calllog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.databinding.CallLogFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import ir.novaapps.callsafebackup.viewmodel.MainViewModel
import ir.novaapps.ui.Dialog
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CallLogFragment : BaseFragment<CallLogFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> CallLogFragmentBinding
        get() = CallLogFragmentBinding::inflate

    private val mainViewModel: MainViewModel by viewModels()

    private var callLogAdapter: CallLogAdapter = CallLogAdapter()

    private var FAB_Status = false

    //Animations
    var show_fab_1: Animation? = null
    var hide_fab_1: Animation? = null
    var show_fab_2: Animation? = null
    var hide_fab_2: Animation? = null
    var show_fab_3: Animation? = null
    var hide_fab_3: Animation? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainViewModel.loadCallLogs(requireContext(), 0)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Animations
        show_fab_1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab1_show)
        hide_fab_1 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab1_hide)
        show_fab_2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab2_show)
        hide_fab_2 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab2_hide)
        show_fab_3 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab3_show)
        hide_fab_3 = AnimationUtils.loadAnimation(requireContext(), R.anim.fab3_hide)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.callLogs.collect { list ->
                    callLogAdapter.setUsers(list)

                    if (list.isEmpty()) {
                        updateUi(true)
                    } else {
                        updateUi(false)
                    }
                }
            }
        }

        binding.apply {
            fab.setOnClickListener {
                if (FAB_Status == false) {
                    expandFAB()
                } else {
                    hideFAB()
                }
            }

            layoutInclude.fab1.setOnClickListener {
                Toast.makeText(requireContext(), "Click on FAB 1", Toast.LENGTH_SHORT).show()
                hideFAB()
            }
            layoutInclude.fab2.setOnClickListener {
                Toast.makeText(requireContext(), "Click on FAB 2", Toast.LENGTH_SHORT).show()
                hideFAB()
            }
            layoutInclude.fab3.setOnClickListener {
                hideFAB()
                Dialog.showAlertDialogInsertInfo(
                    requireContext(),
                    "بک آپ گزارش تماس",
                    selectItemSniper = {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    },
                    onClickOk = {
                        Toast.makeText(requireContext(), "onClickOk", Toast.LENGTH_SHORT).show()
                    },
                    onClickOpenFile = {
                        Toast.makeText(requireContext(), "onClickOpenFile", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
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
            adapter = callLogAdapter
            callLogAdapter.onItemClick { userEntity ->
            }
        }
    }

    private fun expandFAB() {
        binding.apply {
            fab.setImageResource(R.drawable.icon_close)
            FAB_Status = true
            val layoutParams1 = layoutInclude.fab1.layoutParams as FrameLayout.LayoutParams
            layoutParams1.rightMargin += (layoutInclude.fab1.width * 1.7).toInt()
            layoutParams1.bottomMargin += (layoutInclude.fab1.height * 0.25).toInt()
            layoutInclude.fab1.layoutParams = layoutParams1
            layoutInclude.fab1.startAnimation(show_fab_1)
            layoutInclude.fab1.isClickable = true

            // Floating Action Button 2
            val layoutParams2 = layoutInclude.fab2.layoutParams as FrameLayout.LayoutParams
            layoutParams2.rightMargin += (layoutInclude.fab2.width * 1.5).toInt()
            layoutParams2.bottomMargin += (layoutInclude.fab2.height * 1.5).toInt()
            layoutInclude.fab2.layoutParams = layoutParams2
            layoutInclude.fab2.startAnimation(show_fab_2)
            layoutInclude.fab2.isClickable = true

            // Floating Action Button 3
            val layoutParams3 = layoutInclude.fab3.layoutParams as FrameLayout.LayoutParams
            layoutParams3.rightMargin += (layoutInclude.fab3.width * 0.25).toInt()
            layoutParams3.bottomMargin += (layoutInclude.fab3.height * 1.7).toInt()
            layoutInclude.fab3.layoutParams = layoutParams3
            layoutInclude.fab3.startAnimation(show_fab_3)
            layoutInclude.fab3.isClickable = true
        }
    }

    private fun hideFAB() {
        binding.apply {
            fab.setImageResource(R.drawable.icon_fab_export)
            FAB_Status = false;
            val layoutParams1 = layoutInclude.fab1.layoutParams as FrameLayout.LayoutParams
            layoutParams1.rightMargin -= (layoutInclude.fab1.width * 1.7).toInt()
            layoutParams1.bottomMargin -= (layoutInclude.fab1.height * 0.25).toInt()
            layoutInclude.fab1.layoutParams = layoutParams1
            layoutInclude.fab1.startAnimation(hide_fab_1)
            layoutInclude.fab1.isClickable = false

            // Floating Action Button 2
            val layoutParams2 = layoutInclude.fab2.layoutParams as FrameLayout.LayoutParams
            layoutParams2.rightMargin -= (layoutInclude.fab2.width * 1.5).toInt()
            layoutParams2.bottomMargin -= (layoutInclude.fab2.height * 1.5).toInt()
            layoutInclude.fab2.layoutParams = layoutParams2
            layoutInclude.fab2.startAnimation(hide_fab_2)
            layoutInclude.fab2.isClickable = false

            // Floating Action Button 3
            val layoutParams3 = layoutInclude.fab3.layoutParams as FrameLayout.LayoutParams
            layoutParams3.rightMargin -= (layoutInclude.fab3.width * 0.25).toInt()
            layoutParams3.bottomMargin -= (layoutInclude.fab3.height * 1.7).toInt()
            layoutInclude.fab3.layoutParams = layoutParams3
            layoutInclude.fab3.startAnimation(hide_fab_3)
            layoutInclude.fab3.isClickable = false
        }
    }

}