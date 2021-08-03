package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.*

class FourierCalculator(
    private val userFunction: (time: Float) -> Complex
) {
    fun calculateCoefficient(number: Int): Map<Int, FourierCoefficient> {
        val terms = HashMap<Int, FourierCoefficient>(1 + 2 * number)
        for (n in -number..number) {
            val cn = integrate(0f, 1f, 0.001f) { t ->
//                val t = time / period
                val exp1 = ComplexExponent(0f, -PI_2 * n * t)
                val exp2 = userFunction(t).toExponent()
                (exp1 * exp2).toComplex()
            }.toExponent()
            val term = FourierCoefficient(cn.amplitude, cn.beta, n)
            terms[n] = term
        }
        return terms
    }
}