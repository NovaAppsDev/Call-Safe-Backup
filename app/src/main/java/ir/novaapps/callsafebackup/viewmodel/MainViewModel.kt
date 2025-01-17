package ir.novaapps.callsafebackup.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.usecase.ExportCallLogToUriUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.ExportContactToUriUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.GetCallLogUseCase
import ir.novaapps.callsafebackup.data.domain.usecase.GetContactUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Thread.State
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAllContactsUseCase: GetContactUseCase,
    private val getAllCallLogUseCase: GetCallLogUseCase,
    private val exportContactToUriUseCase: ExportContactToUriUseCase,
    private val exportCallLogToUriUseCase: ExportCallLogToUriUseCase,
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _contacts = MutableStateFlow<List<ContactModel>>(emptyList())
    val contacts: StateFlow<List<ContactModel>> = _contacts

    fun loadContacts(context: Context) {
        viewModelScope.launch {
            _contacts.value = getAllContactsUseCase(context)
        }
    }

    private val _callLogs = MutableStateFlow<List<CallLogModel>>(emptyList())
    val callLogs: StateFlow<List<CallLogModel>> = _callLogs

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

}