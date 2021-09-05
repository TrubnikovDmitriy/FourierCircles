package dv.trubnikov.fourier.circles.models

interface Tick : Comparable<Tick> {

    val value: Float

    operator fun plus(other: Tick): Float
    operator fun minus(other: Tick): Float

    companion object {
        const val MAX_VALUE = 1.0f
        const val MIN_VALUE = 0.0f
    }
}
