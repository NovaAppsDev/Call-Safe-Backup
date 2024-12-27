package ir.novaapps.callsafebackup.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
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
    private val getAllCallLogUseCase: GetCallLogUseCase
) : ViewModel() {

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
}