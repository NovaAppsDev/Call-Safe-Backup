package ir.novaapps.callsafebackup.data.domain.usecase

import android.content.Context
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.repository.CallLogRepository
import javax.inject.Inject

class GetCallLogUseCase @Inject constructor(
    private val callLogRepository: CallLogRepository
) {
    suspend operator fun invoke(context: Context , startId:Long): List<CallLogModel>{
        return callLogRepository.getAllCallLog(context,startId)
    }
}