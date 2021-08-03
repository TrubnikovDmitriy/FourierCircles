package dv.trubnikov.fourier.circles.models

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
