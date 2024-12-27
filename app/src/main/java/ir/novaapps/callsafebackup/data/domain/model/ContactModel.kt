package ir.novaapps.callsafebackup.data.domain.model

//data class ContactModel(
//    val id: String,
//    val name: String,
//    val numbers: List<String> = emptyList(),
//    val emails: List<String> = emptyList(),
//    val events: List<String> = emptyList(),
//    val groups: List<String> = emptyList(),
//    val notes: List<String> = emptyList(),
//    val organization: List<String> = emptyList()
//)

data class ContactModel(val id: String, val name:String) {
    var numbers = ArrayList<String>()
    var emails = ArrayList<String>()
}