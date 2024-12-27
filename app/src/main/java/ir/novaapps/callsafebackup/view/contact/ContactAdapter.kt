package ir.novaapps.callsafebackup.view.contact

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.databinding.ContactItemListBinding
import javax.inject.Inject

class ContactAdapter @Inject constructor() : ListAdapter<ContactModel, ContactAdapter.ContactViewHolder>(
    object : DiffUtil.ItemCallback<ContactModel>() {
        override fun areItemsTheSame(
            oldItem: ContactModel,
            newItem: ContactModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactModel,
            newItem: ContactModel
        ): Boolean {
            return oldItem == newItem
        }
    }
){

    private val selectedItems = mutableSetOf<ContactModel>()
    private var isSelectionModeEnabled = false
    private var fullList: List<ContactModel> = emptyList() // لیست کامل
    var filteredList: List<ContactModel> = emptyList() // لیست فیلتر شده


    init {
        filteredList = currentList // در ابتدا لیست فیلترشده همان لیست اصلی است
    }


    inner class ContactViewHolder(private val binding : ContactItemListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:ContactModel){
            binding.apply {
                txtNameContact.text = item.name
                if (item.numbers.isNotEmpty()){
                    txtNumberContact.text = item.numbers.get(0).toString()
                }else{
                    txtNumberContact.text = "شماره ای ثبت نشده است."
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ContactItemListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
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
    private fun toggleSelection(item: ContactModel) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
        notifyDataSetChanged() // بروزرسانی لیست
    }

    fun getSelectedItems(): List<ContactModel> = selectedItems.toList()

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

    // جستجو در لیست
//    fun filter(query: String) {
//        Log.d("AdapterTest", "Filter Query: $query")
//        filteredList = if (query.isEmpty()) {
//            fullList
//        } else {
//            fullList.filter {
//                it.fullName!!.contains(query, ignoreCase = true) ||
//                        it.documentNumber!!.contains(query, ignoreCase = true)
//            }
//        }
//        Log.d("AdapterTest", "Filtered List Size: ${filteredList.size}")
//        notifyDataSetChanged()
//    }

    // تنظیم لیست کامل
    fun setUsers(users: List<ContactModel>) {
        fullList = users
        filteredList = users
        submitList(users) // لیست اصلی را تنظیم می‌کنیم
    }

}