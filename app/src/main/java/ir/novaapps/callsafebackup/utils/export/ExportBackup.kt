package ir.novaapps.callsafebackup.utils.export

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import java.io.File
import java.time.LocalTime
import java.util.Locale

class ExportBackup {
    companion object{

        fun exportContactToJson(context: Context, contentResolver: ContentResolver, list: List<ContactModel>, uri: Uri?) : String {
            return try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(list)

                if (uri != null) {
                    // باز کردن OutputStream برای نوشتن در URI
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(jsonString.toByteArray())
                        outputStream.flush()
                    }
                } else {
                    val time = System.currentTimeMillis()
                    val folderName = "BackupFiles"
                    val folder = File(context.filesDir, folderName)
                    if (!folder.exists()) {
                        folder.mkdirs() // ایجاد پوشه در صورت عدم وجود
                    }
                    val fileName = "contacts_$time.json"
                    val file = File(folder, fileName)
                    file.writeText(jsonString)
                }

                "Ok Export"
            } catch (e: Exception) {
                e.message.toString()
            }
        }
        fun exportContactToXml(context: Context,contentResolver: ContentResolver, list: List<ContactModel>, uri: Uri?): String {
            return try {
                // ایجاد یک StringBuilder برای ساخت XML
                val xmlBuilder = StringBuilder()
                xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                xmlBuilder.append("<Contacts>\n")

                // افزودن هر مخاطب به XML
                list.forEach { contact ->
                    xmlBuilder.append("\t<Contact>\n")
                    xmlBuilder.append("\t\t<Id>${contact.id}</Id>\n")
                    xmlBuilder.append("\t\t<Name>${contact.name}</Name>\n")
                    xmlBuilder.append("\t\t<Phone>${contact.numbers}</Phone>\n")
                    xmlBuilder.append("\t</Contact>\n")
                }

                xmlBuilder.append("</Contacts>")

                if (uri != null) {
                    // باز کردن OutputStream برای نوشتن در URI
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(xmlBuilder.toString().toByteArray())
                        outputStream.flush()
                    }
                } else {
                    val time = System.currentTimeMillis()
                    val folderName = "BackupFiles"
                    val folder = File(context.filesDir, folderName)
                    if (!folder.exists()) {
                        folder.mkdirs() // ایجاد پوشه در صورت عدم وجود
                    }
                    val fileName = "contacts_$time.xml"
                    val file = File(folder, fileName)
                    file.writeText(xmlBuilder.toString())
                }

                "Ok Export"
            } catch (e: Exception) {
                e.message.toString()
            }
        }

        fun exportCallLogToJson(context: Context,contentResolver: ContentResolver, list: List<CallLogModel>, uri: Uri?) : String {
            return try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(list)

                if (uri != null) {
                    // باز کردن OutputStream برای نوشتن در URI
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(jsonString.toByteArray())
                        outputStream.flush()
                    }
                }else{
                    val time = System.currentTimeMillis()
                    val folderName = "BackupFiles"
                    val folder = File(context.filesDir, folderName)
                    if (!folder.exists()) {
                        folder.mkdirs() // ایجاد پوشه در صورت عدم وجود
                    }
                    val fileName = "callLogs_$time.json"
                    val file = File(folder, fileName)
                    file.writeText(jsonString)
                }

                return "Ok Export"
            }catch (e:Exception){
                return e.message.toString()
            }
        }
        fun exportCallLogToXml(context: Context,contentResolver: ContentResolver, list: List<CallLogModel>, uri: Uri?): String {
            return try {
                // ایجاد یک StringBuilder برای ساخت XML
                val xmlBuilder = StringBuilder()
                xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                xmlBuilder.append("<CallLog>\n")

                // افزودن هر مخاطب به XML
                list.forEach { callLog ->
                    xmlBuilder.append("\t<CallLog>\n")
                    xmlBuilder.append("\t\t<Id>${callLog.id}</Id>\n")
                    xmlBuilder.append("\t\t<Name>${callLog.displayName}</Name>\n")
                    xmlBuilder.append("\t\t<Phone>${callLog.number}</Phone>\n")
                    xmlBuilder.append("\t\t<Date>${callLog.date}</Date>\n")
                    xmlBuilder.append("\t\t<Duration>${callLog.duration}</Duration>\n")
                    xmlBuilder.append("\t\t<CallType>${callLog.callType}</CallType>\n")
                    xmlBuilder.append("\t\t<SimInfo>${callLog.simInfo}</SimInfo>\n")
                    xmlBuilder.append("\t\t<IsForwarded>${callLog.isForwarded}</IsForwarded>\n")
                    xmlBuilder.append("\t\t<CallStatus>${callLog.callStatus}</CallStatus>\n")
                    xmlBuilder.append("\t</CallLog>\n")
                }
                xmlBuilder.append("</CallLog>")

                if (uri != null) {
                    // باز کردن OutputStream برای نوشتن در URI
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(xmlBuilder.toString().toByteArray())
                        outputStream.flush()
                    }
                } else {
                    val time = System.currentTimeMillis()
                    val folderName = "BackupFiles"
                    val folder = File(context.filesDir, folderName)
                    if (!folder.exists()) {
                        folder.mkdirs() // ایجاد پوشه در صورت عدم وجود
                    }
                    val fileName = "callLogs_$time.xml"
                    val file = File(folder, fileName)
                    file.writeText(xmlBuilder.toString())
                }

                return "Ok Export"
            } catch (e: Exception) {
                return e.message.toString()
            }
        }

    }
}