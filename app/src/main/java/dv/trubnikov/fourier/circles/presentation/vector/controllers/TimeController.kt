package dv.trubnikov.fourier.circles.presentation.vector.controllers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TimeController(private val scope: CoroutineScope) {

    companion object {
        const val TIME_STEP_MS = 16L

        const val DEFAULT_PERIOD_MS = 20_000L
    }

    fun interface TimeListener {
        fun onTick(tickNumber: Long, tickSize: Long)
    }

    private val mutex = Mutex()

    private var timeListener: TimeListener? = null
    private var startTime = System.currentTimeMillis()
    private var period = DEFAULT_PERIOD_MS

    init {
        scope.launch(Dispatchers.IO) {
            while (true) {
                mutex.withLock {
                    val totalTicks = period / TIME_STEP_MS
                    val elapsedTime = System.currentTimeMillis() - startTime
                    if (elapsedTime < period) {
                        val tickNumber = elapsedTime / TIME_STEP_MS
                        timeListener?.onTick(tickNumber, totalTicks)
                    } else {
                        // Reset timer
                        startTime = System.currentTimeMillis()
                        timeListener?.onTick(0, totalTicks)
                    }
                }

                delay(TIME_STEP_MS)
            }
        }
    }

    fun restart() {
        scope.launch {
            mutex.withLock {
                startTime = System.currentTimeMillis()
            }
        }
    }

    fun setTimeListener(listener: TimeListener?) {
        scope.launch {
            mutex.withLock {
                timeListener = listener
            }
        }
    }
}