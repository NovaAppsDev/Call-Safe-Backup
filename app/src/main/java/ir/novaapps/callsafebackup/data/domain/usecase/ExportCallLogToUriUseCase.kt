package ir.novaapps.callsafebackup.data.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.novaapps.callsafebackup.data.domain.model.CallLogModel
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.utils.export.ExportBackup
import javax.inject.Inject

class ExportCallLogToUriUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {
    suspend operator fun invoke(listCallLog:List<CallLogModel> ,uri:Uri?,typeFormat:Int) : String{
        if (typeFormat==1){
            return ExportBackup.exportCallLogToJson(
                context,
                context.contentResolver,
                listCallLog,
                uri
            )
        }else{
            return ExportBackup.exportCallLogToXml(
                context,
                context.contentResolver,
                listCallLog,
                uri
            )
        }

    }
}