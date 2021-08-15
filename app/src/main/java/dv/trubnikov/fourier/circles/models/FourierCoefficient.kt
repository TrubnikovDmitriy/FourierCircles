package dv.trubnikov.fourier.circles.models

import dv.trubnikov.fourier.circles.calculates.PI_2
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class FourierCoefficient(
    val amplitude: Float,
    val argument: Float,
    val n: Int,
) : Comparable<FourierCoefficient> {

    fun angle(time: Float): Float {
        return PI_2 * n * time + argument
    }

    fun toComplex(time: Float): Complex {
        val angle = angle(time)
        val real = amplitude * cos(angle)
        val image = amplitude * sin(angle)
        return Complex(real = real, image = image)
    }

    override fun compareTo(other: FourierCoefficient): Int {
        // 0, -1, +1, -2, +2...
        val absDiff = n.absoluteValue - other.n.absoluteValue
        if (absDiff != 0) {
            return absDiff
        } else {
            return n - other.n
        }
    }
}
