package ir.novaapps.callsafebackup.data.domain.usecase

import android.content.Context
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.repository.ContactRepository

class GetContactUseCase(
    private val contactRepository: ContactRepository
) {
    suspend operator fun invoke(context: Context):List<ContactModel>{
        // دریافت اطلاعات پایه
        val contacts = contactRepository.getPhoneContacts(context)

        // تجمیع اطلاعات
        return contacts.map { contact ->
            contact.copy(
                numbers = contactRepository.getContactNumbers(context)[contact.id] ?: emptyList(),
                emails = contactRepository.getContactEmails(context)[contact.id] ?: emptyList(),
                events = contactRepository.getContactEvents(context)[contact.id] ?: emptyList(),
                groups = contactRepository.getContactGroups(context)[contact.id] ?: emptyList(),
                notes = contactRepository.getContactNotes(context)[contact.id]?.let { listOf(it) } ?: emptyList(),
                organization = contactRepository.getContactOrganization(context)[contact.id]?.let { listOf(it) } ?: emptyList()
            )
        }
    }
}