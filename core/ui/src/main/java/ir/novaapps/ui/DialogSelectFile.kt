package ir.novaapps.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import ir.novaapps.callsafebackup.utils.events.EventBus
import ir.novaapps.callsafebackup.utils.events.Events
import kotlinx.coroutines.launch


class DialogSelectFile : DialogFragment() {

    private lateinit var txtTitleDialog: TextView
    private lateinit var buttonSelectPath: ImageView
    private lateinit var editTextPath: EditText
    private lateinit var editTextName: EditText
    private lateinit var spinnerSelectForamt: Spinner
    private lateinit var btnOk: Button
    private lateinit var btnCancel: Button

    private var selectedFormatFile : Int =0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.design_dialog_1, null)

        txtTitleDialog = view.findViewById(R.id.txtTitleDialog)
        editTextPath = view.findViewById(R.id.edit_choose_path)
        editTextName = view.findViewById(R.id.edit_name_file)
        buttonSelectPath = view.findViewById(R.id.img_open_folder)
        spinnerSelectForamt = view.findViewById(R.id.snip_select_format)
        btnOk = view.findViewById(R.id.btn_ok)
        btnCancel = view.findViewById(R.id.btn_cancel)


        val items = listOf("انتخاب فرمت فایل","JSON", "XML")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSelectForamt.adapter = adapter
        spinnerSelectForamt.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position!=0){
                        selectedFormatFile = position
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }


        buttonSelectPath.setOnClickListener {
            val nameFile = editTextName.text.trim().toString()

            if (nameFile.isNotEmpty()){
                if (selectedFormatFile!=0){
                    openFileSelector(nameFile,selectedFormatFile)
                }else{
                    Toast.makeText(requireContext(), "فرمت فایل ", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireContext(), "لطفا نام فایل را وارد کنید !", Toast.LENGTH_SHORT).show()
            }

        }

        btnOk.setOnClickListener {
            val selectedPath = editTextPath.text.toString()
            if (selectedPath.isNotEmpty()) {
                if (selectedFormatFile!=0){
                    sendUri(selectedPath,selectedFormatFile)
                }else{
                    Toast.makeText(requireContext(), "فرمت فایل را انتخاب کنید", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "محل ذخیره سازی فایل را انتخاب کنید", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }

        // اضافه کردن Listener به دکمه لغو
        btnCancel.setOnClickListener {
            dismiss() // بستن دیالوگ
        }

        // تنظیم شفافیت پس‌زمینه
        val dialog = builder.setView(view).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

    private fun openFileSelector(nameFile:String,typeFormat:Int) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            if (typeFormat==1){
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, "$nameFile.json")
            }else{
                type = "text/xml"
                putExtra(Intent.EXTRA_TITLE, "$nameFile.xml")
            }
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_PATH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_PATH && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            uri?.let {
                val path = it.toString() // URI انتخاب‌شده
                editTextPath.setText(path) // نمایش مسیر در EditText
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_PATH = 1
    }

    fun sendUri(uri:String,typeFormat:Int){
        lifecycleScope.launch {
            EventBus.publish(Events.IsResult(uri,typeFormat))
        }
    }

}