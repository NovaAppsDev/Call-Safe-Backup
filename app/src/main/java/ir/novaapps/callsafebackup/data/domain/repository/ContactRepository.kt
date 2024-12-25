package ir.novaapps.callsafebackup.data.domain.repository

import android.content.Context
import ir.novaapps.callsafebackup.data.domain.model.ContactModel

interface ContactRepository {

    suspend fun getPhoneContacts(context:Context):List<ContactModel>
    suspend fun getContactNumbers(context: Context):HashMap<String, ArrayList<String>>
    suspend fun getContactEmails(context: Context) : HashMap<String, ArrayList<String>>
    suspend fun getContactEvents(context: Context):HashMap<String, ArrayList<String>>
    suspend fun getContactGroups(context: Context): HashMap<String, ArrayList<String>>
    suspend fun getContactNotes(context: Context): HashMap<String, String>
    suspend fun getContactOrganization(context: Context): HashMap<String, String>
}