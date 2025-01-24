package ir.novaapps.callsafebackup.view.history

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.model.HistoryModel
import ir.novaapps.callsafebackup.databinding.ContactItemListBinding
import ir.novaapps.callsafebackup.databinding.HistoryItemListBinding
import java.io.File
import javax.inject.Inject

class HistoryBackupAdapter @Inject constructor() : ListAdapter<HistoryModel, HistoryBackupAdapter.HistoryBackupViewHolder>(
    object : DiffUtil.ItemCallback<HistoryModel>() {
        override fun areItemsTheSame(
            oldItem: HistoryModel,
            newItem: HistoryModel
        ): Boolean {
            return oldItem.dirFile == newItem.dirFile
        }

        override fun areContentsTheSame(
            oldItem: HistoryModel,
            newItem: HistoryModel
        ): Boolean {
            return oldItem == newItem
        }
    }
){

    private val selectedItems = mutableSetOf<HistoryModel>()
    private var isSelectionModeEnabled = false
    private var fullList: List<HistoryModel> = emptyList() // لیست کامل
    var filteredList: List<HistoryModel> = emptyList() // لیست فیلتر شده


    init {
        filteredList = currentList // در ابتدا لیست فیلترشده همان لیست اصلی است
    }


    inner class HistoryBackupViewHolder(private val binding : HistoryItemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:HistoryModel){
            binding.apply {
                txtNameFile.text = item.nameFile
                btnDeleteBackup.setOnClickListener {
                    item.dirFile?.let { filePath ->
                        val file = File("${filePath}/${item.nameFile}")
                        // بررسی وجود فایل
                        if (file.exists()) {
                            if (file.delete()) {
                                // حذف موفق
                                val updatedList = currentList.toMutableList()
                                updatedList.remove(item)
                                submitList(updatedList)
                                notifyDataSetChanged()
                                Toast.makeText(binding.root.context, "${item.nameFile} حذف شد", Toast.LENGTH_SHORT).show()
                            } else {
                                // خطا در حذف فایل
                                Log.e("FileDeleteError", "فایل ${item.nameFile} حذف نشد.")
                                Toast.makeText(binding.root.context, "خطا در حذف فایل ${item.nameFile}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // فایل وجود ندارد
                            Log.e("FileDeleteError", "فایل ${item.nameFile} وجود ندارد.")
                            Toast.makeText(binding.root.context, "فایل ${item.nameFile} پیدا نشد", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                btnShareBackup.setOnClickListener {
                    item.dirFile?.let { filePath ->
                        val context = binding.root.context
                        val file = File("${filePath}/${item.nameFile}")
                        if (file.exists()) {
                            // ساخت Uri برای اشتراک‌گذاری
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider", // تغییر دهید به نام پکیج خودتان
                                file
                            )

                            // ساخت Intent برای اشتراک‌گذاری
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/octet-stream" // تنظیم نوع فایل
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }

                            // ارسال Intent
                            context.startActivity(Intent.createChooser(shareIntent, "اشتراک‌گذاری با"))
                        } else {
                            Toast.makeText(context, "فایل ${item.nameFile} وجود ندارد", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryBackupViewHolder {
        val binding = HistoryItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryBackupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryBackupViewHolder, position: Int) {
        if (position < filteredList.size) {
            holder.bind(filteredList[position])
        } else {
            Log.e("AdapterError", "Invalid position: $position, List size: ${filteredList.size}")
        }
    }

    private var setOnItemClickListener : ((HistoryModel)->Unit)?=null
    fun onItemClick(listener:(HistoryModel)->Unit){
        setOnItemClickListener = listener
    }


    // تغییر وضعیت انتخاب
    private fun toggleSelection(item: HistoryModel) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        notifyDataSetChanged() // بروزرسانی لیست
    }

    fun getSelectedItems(): List<HistoryModel> = selectedItems.toList()

    fun clearSelection() {
        selectedItems.clear()
        isSelectionModeEnabled = false
        notifyDataSetChanged()
    }

    fun enableSelectionMode() {
        isSelectionModeEnabled = true
        notifyDataSetChanged()
    }

    fun selectAll() {
        if (currentList.isNotEmpty()) {
            selectedItems.clear()
            selectedItems.addAll(currentList)
            isSelectionModeEnabled = true
            notifyDataSetChanged()
        }
    }

    fun isSelectionMode(): Boolean = isSelectionModeEnabled

    // تنظیم لیست کامل
    fun setUsers(users: List<HistoryModel>) {
        fullList = users
        filteredList = users
        submitList(users) // لیست اصلی را تنظیم می‌کنیم
    }

}