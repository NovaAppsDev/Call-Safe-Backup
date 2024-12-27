package ir.novaapps.callsafebackup.data.domain.repository

import android.content.Context
import android.util.Log
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel

interface CallLogRepository {
    suspend fun getAllCallLog(context: Context,startId : Long = 0):List<CallLogModel>
}