package ir.novaapps.callsafebackup.utils.events

class Events {
    data class IsLoading(val isLoading:Boolean)
    data class IsResult(val isResult:String , val typeFormat:Int,val consumed: Boolean = false)
}
