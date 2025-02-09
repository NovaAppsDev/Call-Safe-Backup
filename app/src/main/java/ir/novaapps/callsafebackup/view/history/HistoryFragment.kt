package ir.novaapps.callsafebackup.view.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
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

    private var listFiles: MutableList<HistoryModel> = mutableListOf()

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
        initRecyclerView()
        updateUi()
    }


    private fun updateUi() {
        if (listFiles.isNotEmpty()) {
            historyBackupAdapter.submitList(listFiles)
            binding.historyListBackup.isVisible = true
            binding.emptyList.isVisible = false
        } else {
            binding.historyListBackup.isVisible = false
            binding.emptyList.isVisible = true
        }
    }

    private fun initRecyclerView() {
        binding.historyListBackup.apply {
            // استفاده از GridLayoutManager با دو ستون
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = historyBackupAdapter

            historyBackupAdapter.onItemDelete { historyModel, position ->
                val file = File("${historyModel.dirFile}/${historyModel.nameFile}")
                if (file.exists()) {
                    if (file.delete()) {
                        // حذف موفق
                        listFiles.remove(historyModel)
                        historyBackupAdapter.submitList(listFiles)
                        historyBackupAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            binding.root.context,
                            "${historyModel.nameFile} حذف شد",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // خطا در حذف فایل
                        Log.e("FileDeleteError", "فایل ${historyModel.nameFile} حذف نشد.")
                        Toast.makeText(
                            binding.root.context,
                            "خطا در حذف فایل ${historyModel.nameFile}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // فایل وجود ندارد
                    Log.e("FileDeleteError", "فایل ${historyModel.nameFile} وجود ندارد.")
                    Toast.makeText(
                        binding.root.context,
                        "فایل ${historyModel.nameFile} پیدا نشد",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            historyBackupAdapter.onItemShare { historyModel, position ->
                val file = File("${historyModel.dirFile}/${historyModel.nameFile}")
                if (file.exists()) {
                    // ساخت Uri برای اشتراک‌گذاری
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.provider", // تغییر دهید به نام پکیج خودتان
                        file
                    )
                    // ساخت Intent برای اشتراک‌گذاری
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "application/octet-stream" // تنظیم نوع فایل
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    // ارسال Intent
                    requireContext().startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "اشتراک‌گذاری با"
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "فایل ${historyModel.nameFile} وجود ندارد", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getFileZip(folder: File): MutableList<HistoryModel> {
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