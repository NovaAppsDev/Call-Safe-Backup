package ir.novaapps.callsafebackup.utils.events

import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlin.coroutines.coroutineContext

object EventBus {
    private val _event = MutableSharedFlow<Any>()
    val event: SharedFlow<Any> get() = _event.asSharedFlow()
    suspend fun publish(ev:Any){
        _event.emit(ev)
    }
    suspend inline fun <reified T> subscribe(crossinline onEvent:(T)->Unit){
        event.filterIsInstance<T>().collectLatest {
            coroutineContext.ensureActive()
            onEvent(it)
        }
    }

}