package ir.novaapps.callsafebackup.data.repository

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.data.domain.repository.ContactRepository

class ContactRepositoryImpl : ContactRepository {

    override suspend fun getPhoneContacts(context: Context): List<ContactModel> {
        val contactsList = mutableListOf<ContactModel>()
        val contactsCursor = context.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        contactsCursor?.use { cursor ->
            if (cursor.count > 0) {
                val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
                val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getString(idIndex)
                    val name = cursor.getString(nameIndex) ?: "Unknown"
                    contactsList.add(ContactModel(id, name))
                }
            }
        }

        return contactsList
    }

    override suspend fun getContactNumbers(context: Context): HashMap<String, ArrayList<String>> {
        val contactsNumberMap = HashMap<String, ArrayList<String>>()
        val phoneCursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        phoneCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex =
                    phoneCursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val numberIndex =
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (cursor.moveToNext()) {
                    val contactId = phoneCursor.getString(contactIdIndex!!)
                    val number: String = phoneCursor.getString(numberIndex)
                    //check if the map contains key or not, if not then create a new array list with number
                    if (contactsNumberMap.containsKey(contactId)) {
                        contactsNumberMap[contactId]?.add(number)
                    } else {
                        contactsNumberMap[contactId] = arrayListOf(number)
                    }
                }
            }

        }

        return contactsNumberMap
    }

    override suspend fun getContactEmails(context: Context): HashMap<String, ArrayList<String>> {
        val contactsEmailMap = HashMap<String, ArrayList<String>>()
        val emailCursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        emailCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex =
                    emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID)
                val emailIndex =
                    emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                while (cursor.moveToNext()) {
                    val contactId = emailCursor.getString(contactIdIndex)
                    val email = emailCursor.getString(emailIndex)
                    //check if the map contains key or not, if not then create a new array list with email
                    if (contactsEmailMap.containsKey(contactId)) {
                        contactsEmailMap[contactId]?.add(email)
                    } else {
                        contactsEmailMap[contactId] = arrayListOf(email)
                    }
                }
            }

        }
        return contactsEmailMap
    }

    override suspend fun getContactEvents(context: Context): HashMap<String, ArrayList<String>> {
        val contactsEventMap = HashMap<String, ArrayList<String>>()
        val eventCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
            null
        )

        eventCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val eventIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
                val eventTypeIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)

                while (cursor.moveToNext()) {
                    val contactId = cursor.getString(contactIdIndex)
                    val event = cursor.getString(eventIndex)
                    val eventType = cursor.getInt(eventTypeIndex)
                    val eventTypeName = when (eventType) {
                        ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> "Birthday"
                        ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> "Anniversary"
                        else -> "Other"
                    }
                    val eventInfo = "$event ($eventTypeName)"
                    if (contactsEventMap.containsKey(contactId)) {
                        contactsEventMap[contactId]?.add(eventInfo)
                    } else {
                        contactsEventMap[contactId] = arrayListOf(eventInfo)
                    }
                }
            }


        }
        return contactsEventMap
    }

    override suspend fun getContactGroups(context: Context): HashMap<String, ArrayList<String>> {
        val contactGroupMap = HashMap<String, ArrayList<String>>()
        val groupCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE),
            null
        )

        groupCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val groupIdIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID)
                while (cursor.moveToNext()) {
                    val contactId = cursor.getString(contactIdIndex)
                    val groupId = cursor.getString(groupIdIndex)
                    if (contactGroupMap.containsKey(contactId)) {
                        contactGroupMap[contactId]?.add(groupId)
                    } else {
                        contactGroupMap[contactId] = arrayListOf(groupId)
                    }
                }
            }
        }
        return contactGroupMap
    }

    override suspend fun getContactNotes(context: Context): HashMap<String, String> {
        val contactNotesMap = HashMap<String, String>()
        val notesCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE),
            null
        )

        notesCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val noteIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)

                while (cursor.moveToNext()) {
                    val contactId = cursor.getString(contactIdIndex)
                    val note = cursor.getString(noteIndex)
                    if (!contactId.isNullOrEmpty() && !note.isNullOrEmpty()) {
                        contactNotesMap[contactId] = note
                    }
                }
            }
        }
        return contactNotesMap
    }

    override suspend fun getContactOrganization(context: Context): HashMap<String, String> {
        val contactOrgMap = HashMap<String, String>()
        val orgCursor = context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.MIMETYPE} = ?",
            arrayOf(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE),
            null
        )

        orgCursor?.use { cursor ->
            if (cursor.count > 0) {
                val contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val companyIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)
                val titleIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE)

                while (cursor.moveToNext()) {
                    val contactId = cursor.getString(contactIdIndex)
                    val company = cursor.getString(companyIndex) ?: "Unknown Company"
                    val title = cursor.getString(titleIndex) ?: "Unknown Title"
                    contactOrgMap[contactId] = "$company - $title"
                }
            }
        }
        return contactOrgMap
    }
}