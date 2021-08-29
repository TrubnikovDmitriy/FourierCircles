package dv.trubnikov.fourier.circles.models

import android.content.Intent
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.ln

/**
 * Представление комплексных чисел
 */
data class Complex(
    val real: Float,
    val image: Float,
)

val Float.j: Complex
    get() = Complex(0f, this)

val Double.j: Complex
    get() = Complex(0f, this.toFloat())

val Int.j: Complex
    get() = Complex(0f, this.toFloat())

operator fun Float.plus(other: Complex): Complex {
    return Complex(
        real = this + other.real,
        image = other.image
    )
}

operator fun Double.plus(other: Complex): Complex {
    return Complex(
        real = this.toFloat() + other.real,
        image = other.image
    )
}

operator fun Int.plus(other: Complex): Complex {
    return Complex(
        real = this + other.real,
        image = other.image
    )
}

operator fun Float.minus(other: Complex): Complex {
    return Complex(
        real = this - other.real,
        image = -other.image
    )
}

operator fun Complex.plus(other: Complex): Complex {
    return Complex(
        real = this.real + other.real,
        image = this.image + other.image
    )
}

operator fun Float.times(other: Complex): Complex {
    return Complex(
        real = this * other.real,
        image = this * other.image
    )
}

operator fun Complex.div(other: Float): Complex {
    return Complex(
        real = real / other,
        image = image / other
    )
}

operator fun Complex.minus(other: Complex): Complex {
    return Complex(
        real = this.real - other.real,
        image = this.image - other.image
    )
}

operator fun Complex.times(other: Float): Complex {
    return Complex(
        real = this.real * other,
        image = this.image * other,
    )
}

fun Complex.module(): Float {
    return hypot(real, image)
}

fun Complex.toExponent(): ComplexExponent {
    val amplitude = hypot(real, image)
    val alpha = ln(amplitude)
    val beta = atan2(image, real)
    return ComplexExponent(alpha = alpha, beta = beta)
}

fun Intent.putComplex(key: String, complexNumbers: List<Complex>) {
    val reals = complexNumbers.map { it.real.toDouble() }.toDoubleArray()
    val images = complexNumbers.map { it.image.toDouble() }.toDoubleArray()
    putExtra(key.toRealKey(), reals)
    putExtra(key.toImageKey(), images)
}

fun Intent.getComplex(key: String): Array<Complex>? {
    val reals = getDoubleArrayExtra(key.toRealKey()) ?: return null
    val images = getDoubleArrayExtra(key.toImageKey()) ?: return null
    if (reals.size != images.size) {
        error("Complex arrays' size must be the same [${reals.size} != ${images.size}]")
    }
    return Array(reals.size) {
        Complex(real = reals[it].toFloat(), image = images[it].toFloat())
    }
}

private fun String.toRealKey(): String = "$this-real"

private fun String.toImageKey(): String = "$this-image"
