package dv.trubnikov.fourier.circles.calculates

@JvmInline
value class Tick(val value: Float) {

    companion object {
        const val MAX_TICK_VALUE = 1.0f
        const val MIN_TICK_VALUE = 0.0f
    }

    init {
        val lazyMessage = {
            "Value must be in [$MIN_TICK_VALUE; $MAX_TICK_VALUE], but is {$value}"
        }
        if (value < MIN_TICK_VALUE) {
            throw IllegalArgumentException(lazyMessage())
        }
        if (value > MAX_TICK_VALUE) {
            throw IllegalArgumentException(lazyMessage())
        }
    }

    operator fun plus(other: Tick): Float {
        return value + other.value
    }

    operator fun minus(other: Tick): Float {
        return value - other.value
    }

    operator fun compareTo(other: Tick): Int {
        return value.compareTo(other.value)
    }
}