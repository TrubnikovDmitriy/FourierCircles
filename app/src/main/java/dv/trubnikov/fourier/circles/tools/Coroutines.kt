package dv.trubnikov.fourier.circles.tools

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

inline fun CoroutineScope.launchWithLock(mutex: Mutex, crossinline action: suspend () -> Unit) {
    launch {
        mutex.withLock {
            action.invoke()
        }
    }
}

fun <T> StateSharedFlow(): MutableSharedFlow<T> {
    return MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
}
