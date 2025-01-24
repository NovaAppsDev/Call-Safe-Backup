package ir.novaapps.callsafebackup.view.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.novaapps.callsafebackup.data.domain.model.HistoryModel
import ir.novaapps.callsafebackup.databinding.HistoryFragmentBinding
import ir.novaapps.callsafebackup.utils.BaseFragment
import java.io.File
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class HistoryFragment : BaseFragment<HistoryFragmentBinding>() {

    override val bindingInflater: (inflater: LayoutInflater) -> HistoryFragmentBinding
        get() = HistoryFragmentBinding::inflate

    private var listFiles : List<HistoryModel> = emptyList()

    @Inject
    lateinit var historyBackupAdapter: HistoryBackupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        listFiles = getFileZip(requireContext().filesDir)
        Log.i("12123", listFiles.toString())

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyBackupAdapter.setUsers(listFiles)
        initRecyclerView()
    }


    private fun initRecyclerView() {
        binding.historyListBackup.apply {
            // استفاده از GridLayoutManager با دو ستون
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = historyBackupAdapter
            historyBackupAdapter.onItemClick { historyModel ->

            }
        }
    }


   private fun getFileZip(folder: File): List<HistoryModel> {
        val historyList = mutableListOf<HistoryModel>()

        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    // بازگشت به زیرپوشه‌ها
                    historyList.addAll(getFileZip(file))
                } else if (file.isFile && file.extension.equals("zip", ignoreCase = true)) {
                    // اضافه کردن فایل‌های ZIP به لیست
                    historyList.add(HistoryModel(dirFile = file.parent ?: "", nameFile = file.name))
                }
            }
        }

        return historyList
    }

}