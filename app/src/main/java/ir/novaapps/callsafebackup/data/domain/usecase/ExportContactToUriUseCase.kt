package ir.novaapps.callsafebackup.data.domain.usecase

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.novaapps.callsafebackup.data.domain.model.ContactModel
import ir.novaapps.callsafebackup.utils.export.ExportBackup
import javax.inject.Inject

class ExportContactToUriUseCase @Inject constructor(
    @ApplicationContext val context: Context
) {
    suspend operator fun invoke(listContact:List<ContactModel> ,uri:Uri,typeFormat:Int) : String{
        if (typeFormat==1){
            return ExportBackup.exportContactToJson(
                context.contentResolver,
                listContact,
                uri
            )
        }else{
            return ExportBackup.exportContactToXml(
                context.contentResolver,
                listContact,
                uri
            )
        }

    }
}