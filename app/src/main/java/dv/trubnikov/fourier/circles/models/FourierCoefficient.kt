package dv.trubnikov.fourier.circles.models

import dv.trubnikov.fourier.circles.calculates.PI_2
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

data class FourierCoefficient(
    val number: Int,
    val amplitude: Float,
    val startAngle: Float,
    val frequency: Float,
) : Comparable<FourierCoefficient> {

    constructor(number: Int, amplitude: Float, startAngle: Float) : this(
        number = number,
        amplitude = amplitude,
        startAngle = startAngle,
        frequency = PI_2 * number
    )

    fun angle(time: Tick): Float {
        return startAngle + PI_2 * number * time.value
    }

    fun toComplex(angle: Float): Complex {
        val real = amplitude * cos(angle)
        val image = amplitude * sin(angle)
        return Complex(real = real, image = image)
    }

    override fun compareTo(other: FourierCoefficient): Int {
        // 0, -1, +1, -2, +2...
        val absDiff = number.absoluteValue - other.number.absoluteValue
        if (absDiff != 0) {
            return absDiff
        } else {
            return number - other.number
        }
    }
}
