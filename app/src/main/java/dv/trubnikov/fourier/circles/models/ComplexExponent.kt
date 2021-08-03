package dv.trubnikov.fourier.circles.models

import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin

/**
 * exp(α + jβ) = exp(α)exp(jβ)
 */
data class ComplexExponent(
    val alpha: Float,
    val beta: Float,
) {
    val amplitude = exp(alpha)
}

operator fun ComplexExponent.times(other: ComplexExponent): ComplexExponent {
    val alpha = alpha + other.alpha
    val beta = beta + other.beta
    return ComplexExponent(alpha = alpha, beta = beta)
}

fun ComplexExponent.toComplex(): Complex {
    val real = amplitude * cos(beta)
    val image = amplitude * sin(beta)
    return Complex(real = real, image = image)
}