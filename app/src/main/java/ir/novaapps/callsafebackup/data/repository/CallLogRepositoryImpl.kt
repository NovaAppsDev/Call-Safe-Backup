package ir.novaapps.callsafebackup.data.repository

import android.content.Context
import android.provider.CallLog
import android.util.Log
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.repository.CallLogRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CallLogRepositoryImpl @Inject constructor() : CallLogRepository {

    override suspend fun getAllCallLog(context: Context, startId: Long): List<CallLogModel>{

        val callLogs = mutableListOf<CallLogModel>()
        // Query call logs with a starting ID
        val selection = "${CallLog.Calls._ID} > ?"
        val selectionArgs = arrayOf(startId.toString())
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            CallLog.Calls.DATE + " DESC"
        )

        cursor?.use { cursor ->
            val id = cursor.getColumnIndex(CallLog.Calls._ID)
            val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
            val durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)

            while (cursor.moveToNext()) {
                val id = cursor.getString(id)
                val name = cursor.getString(nameIndex)
                val displayName = if (name.isNullOrEmpty()) "Unknown" else name
                val number = cursor.getString(numberIndex) ?: "Unknown"
                val date = cursor.getLong(dateIndex)
                val formattedDate =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                        Date(date)
                    )
                val duration = cursor.getString(durationIndex) ?: "Unknown"
                val type = cursor.getInt(typeIndex)
                val callType = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }

                // Placeholder for SIM info and call forwarding status
                val simInfo = "SIM Info Unavailable"
                val isForwarded = false
                val callStatus = when (type) {
                    CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    CallLog.Calls.MISSED_TYPE -> "Missed"
                    else -> "Unknown"
                }

                callLogs.add(
                    CallLogModel(
                        id,
                        displayName,
                        number,
                        formattedDate,
                        duration,
                        callType,
                        simInfo,
                        isForwarded,
                        callStatus
                    )
                )
            }
        }
        return callLogs
    }

}