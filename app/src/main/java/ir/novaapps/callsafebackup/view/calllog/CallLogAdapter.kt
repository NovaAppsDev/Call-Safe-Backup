package ir.novaapps.callsafebackup.view.calllog

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.novaapps.callsafebackup.R
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.databinding.CallLogItemListBinding
import javax.inject.Inject

class CallLogAdapter @Inject constructor() : ListAdapter<CallLogModel, CallLogAdapter.CallLogViewHolder>(
    object : DiffUtil.ItemCallback<CallLogModel>() {
        override fun areItemsTheSame(
            oldItem: CallLogModel,
            newItem: CallLogModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CallLogModel,
            newItem: CallLogModel
        ): Boolean {
            return oldItem == newItem
        }
    }
){

    private val selectedItems = mutableSetOf<CallLogModel>()
    private var isSelectionModeEnabled = false
    private var fullList: List<CallLogModel> = emptyList() // لیست کامل
    var filteredList: List<CallLogModel> = emptyList() // لیست فیلتر شده


    init {
        filteredList = currentList // در ابتدا لیست فیلترشده همان لیست اصلی است
    }

    inner class CallLogViewHolder(private val binding : CallLogItemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:CallLogModel){
            binding.apply {
                txtNameCallLog.text = item.displayName
                txtNumberCallLog.text = item.number
                txtTimeCall.text = item.date
                if (item.callStatus.equals("Incoming")){
                    imgStatusCall.setImageResource(R.drawable.icon_call_incomming)
                }
                if (item.callStatus.equals("Outgoing")){
                    imgStatusCall.setImageResource(R.drawable.icon_call_outgoing)
                }
                if (item.callStatus.equals("Missed")){
                    imgStatusCall.setImageResource(R.drawable.icon_call_missed)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val binding = CallLogItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CallLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        if (position < filteredList.size) {
            holder.bind(filteredList[position])
        } else {
            Log.e("AdapterError", "Invalid position: $position, List size: ${filteredList.size}")
        }
    }

    private var setOnItemClickListener : ((String)->Unit)?=null
    fun onItemClick(listener:(String)->Unit){
        setOnItemClickListener = listener
    }

    // تغییر وضعیت انتخاب
    private fun toggleSelection(item: CallLogModel) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        notifyDataSetChanged() // بروزرسانی لیست
    }

    fun getSelectedItems(): List<CallLogModel> = selectedItems.toList()

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
    fun setUsers(users: List<CallLogModel>) {
        fullList = users
        filteredList = users
        submitList(users) // لیست اصلی را تنظیم می‌کنیم
    }
}