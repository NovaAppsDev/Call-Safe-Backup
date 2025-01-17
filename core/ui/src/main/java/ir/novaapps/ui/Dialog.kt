package ir.novaapps.ui

import android.R
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ir.novaapps.ui.databinding.DesignDialog1Binding

class Dialog() {
    companion object {
        fun showAlertDialogInsertInfo(
            context: Context,
            title: String,
            selectItemSniper: (item: String) -> Unit,
            onClickOk: () -> Unit,
            onClickOpenFile: (callback: (uri: String) -> Unit) -> Unit
        ) {
            val dialog = Dialog(context)
            val binding = DesignDialog1Binding.inflate(LayoutInflater.from(context))
            dialog.setContentView(binding.root)
            // تنظیم ظاهر دیالوگ
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val items = listOf("JSON", "XML")
            val adapter = ArrayAdapter(context, R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.apply {
                snipSelectFormat.adapter = adapter
                snipSelectFormat.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedItem = parent?.getItemAtPosition(position).toString()
                            selectItemSniper(selectedItem)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                btnOk.setOnClickListener {
                    onClickOk()
                }
                btnCancel.setOnClickListener {
                    dialog.dismiss()
                }
                imgOpenFolder.setOnClickListener {
                    onClickOpenFile { uri ->
                        // تنظیم مسیر انتخاب‌شده در EditText
                        editChoosePath.setText(uri)
                    }
                }
                txtTitleDialog.text = title
                btnCloseDialog.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.setCancelable(false)
            dialog.show()
        }
    }
}
