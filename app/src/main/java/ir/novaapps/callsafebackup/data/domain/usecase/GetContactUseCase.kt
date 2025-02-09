package ir.novaapps.callsafebackup.data.domain.usecase

import android.content.Context
import android.util.Log
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.repository.ContactRepository
import javax.inject.Inject

class GetContactUseCase @Inject constructor(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(context: Context):List<ContactModel>{
        val contactsListAsync = contactRepository.getPhoneContacts(context)
        val contactNumbersAsync = contactRepository.getContactNumbers(context)
        val contactEmailAsync = contactRepository.getContactEmails(context)

        contactsListAsync.forEach {
            contactNumbersAsync[it.id]?.let { numbers ->
                it.numbers = numbers
            }
            contactEmailAsync[it.id]?.let { emails ->
                it.emails = emails
            }
        }
        return contactsListAsync
    }
}