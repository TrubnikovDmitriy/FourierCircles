package dv.trubnikov.fourier.circles.models

import dv.trubnikov.fourier.circles.calculates.PI_2
import kotlin.math.cos
import kotlin.math.sin

data class FourierCoefficient(
    val amplitude: Float,
    val argument: Float,
    val n: Int,
) {
    fun angle(time: Float): Float {
        return PI_2 * n * time + argument
    }

    fun toComplex(time: Float): Complex {
        val angle = angle(time)
        val real = amplitude * cos(angle)
        val image = amplitude * sin(angle)
        return Complex(real = real, image = image)
    }
}
