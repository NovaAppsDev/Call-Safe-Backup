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

class HistoryBackupAdapter @Inject constructor() :
    ListAdapter<HistoryModel, HistoryBackupAdapter.HistoryBackupViewHolder>(
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
    ) {


    inner class HistoryBackupViewHolder(private val binding: HistoryItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryModel) {
            binding.apply {
                txtNameFile.text = item.nameFile
                btnDeleteBackup.setOnClickListener {
                    setOnItemDeleteListener?.let {
                        it(item, position)
                    }
                }

                btnShareBackup.setOnClickListener {
                    setOnItemShareListener?.let {
                        it(item, position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryBackupViewHolder {
        val binding =
            HistoryItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryBackupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryBackupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private var setOnItemDeleteListener: ((HistoryModel, Int) -> Unit)? = null
    fun onItemDelete(listener: (HistoryModel, Int) -> Unit) {
        setOnItemDeleteListener = listener
    }

    private var setOnItemShareListener: ((HistoryModel, Int) -> Unit)? = null
    fun onItemShare(listener: (HistoryModel, Int) -> Unit) {
        setOnItemShareListener = listener
    }

}