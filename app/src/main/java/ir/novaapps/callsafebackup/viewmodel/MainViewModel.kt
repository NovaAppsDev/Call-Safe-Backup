package ir.novaapps.callsafebackup.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.usecase.ExportCallLogToUriUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.ExportContactToUriUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.GetCallLogUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.GetContactUseCase
import ir.novaapps.callsafebackup.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val getAllContactsUseCase: GetContactUseCase,
    private val getAllCallLogUseCase: GetCallLogUseCase,
    private val exportContactToUriUseCase: ExportContactToUriUseCase,
    private val exportCallLogToUriUseCase: ExportCallLogToUriUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _contacts = MutableLiveData<List<ContactModel>>()
    val contacts: LiveData<List<ContactModel>> = _contacts

    fun loadContacts(context: Context) {
        viewModelScope.launch {
            _contacts.value = getAllContactsUseCase(context)
        }
    }


    private val _callLogs = MutableLiveData<List<CallLogModel>>()
    val callLogs: LiveData<List<CallLogModel>> = _callLogs

    fun loadCallLogs(context: Context , startId:Long){
        viewModelScope.launch {
            _callLogs.value = getAllCallLogUseCase(context,startId)
        }
    }

    private val _exportContactResult = MutableLiveData<String>()
    val exportContactResult: LiveData<String> = _exportContactResult

    fun exportContact(listContact:List<ContactModel>,uri: Uri,typeFormat:Int){
        viewModelScope.launch {
            _isLoading.value = true
            val result = exportContactToUriUseCase(listContact,uri,typeFormat)
            _exportContactResult.value = result
            _isLoading.value = false
        }
    }

    private val _exportCallLogResult = MutableLiveData<String>()
    val exportCallLogResult: LiveData<String> = _exportCallLogResult

    fun exportCallLog(listContact:List<CallLogModel>,uri: Uri,typeFormat:Int){
        viewModelScope.launch {
            _isLoading.value = true
            val result = exportCallLogToUriUseCase(listContact,uri,typeFormat)
            _exportCallLogResult.value = result
            _isLoading.value = false
        }
    }


    private val _exportAllBackupResult = MutableLiveData<String>()
    val exportAllBackupResult: LiveData<String> = _exportAllBackupResult

    fun exportAllBackup(){
        try {
            _isLoading.value = true
            val dirFolder = context.filesDir.absolutePath+"/BackupFiles"
            FileUtils.clearFolder(File(dirFolder))
            runBlocking {
                // Get Backup
                val contacts = getAllContactsUseCase(context)
                val callLog = getAllCallLogUseCase(context,0)
                Log.i("MainViewModel", "Get All Lists")

                exportContactToUriUseCase(contacts,null,1)
                exportCallLogToUriUseCase(callLog,null,1)
                Log.i("MainViewModel", "Export All Lists")

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(Date(System.currentTimeMillis()))

                // Zip Folder
                FileUtils.zipSingleFolder(
                    context,
                    dirFolder,
                    context.filesDir.absolutePath,
                    "backup_${formattedDate}"
                )
                Log.i("MainViewModel", "Zip All file")

                FileUtils.clearFolder(File(dirFolder))
                _exportAllBackupResult.value = "بک آپ با موفقیت انجام شد."
                _isLoading.value = false
            }
        }catch (e : Exception){
            _exportAllBackupResult.value = "Error Backup -> ${e.message}"
            _isLoading.value = false
        }

    }

}