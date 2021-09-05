package dv.trubnikov.fourier.circles.models

import dv.trubnikov.fourier.circles.models.Tick.Companion.MAX_VALUE
import dv.trubnikov.fourier.circles.models.Tick.Companion.MIN_VALUE

@Suppress("FunctionName")
fun Tick(value: Float): Tick = TickImpl(value)

@JvmInline
private value class TickImpl(override val value: Float) : Tick {

    init {
        val lazyMessage = {
            "Value must be in [$MIN_VALUE; $MAX_VALUE], but is {$value}"
        }
        if (value < MIN_VALUE) {
            throw IllegalArgumentException(lazyMessage())
        }
        if (value > MAX_VALUE) {
            throw IllegalArgumentException(lazyMessage())
        }
    }

    override operator fun plus(other: Tick): Float {
        return value + other.value
    }

    override operator fun minus(other: Tick): Float {
        return value - other.value
    }

    override operator fun compareTo(other: Tick): Int {
        return value.compareTo(other.value)
    }
}
