package ir.novaapps.callsafebackup.data.domain.model

data class CallLogModel(
    val id : String,
    val displayName: String,
    val number: String,
    val date: String,
    val duration: String,
    val callType: String,
    val simInfo: String, // Added for SIM information
    val isForwarded: Boolean, // Added for call forwarding status
    val callStatus: String // Added for call status
)

