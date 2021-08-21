package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.*
import kotlinx.coroutines.*

class FourierCalculator(
    private val userFunction: (time: Float) -> Complex
) {
    // TODO: Builder
    private val cachedValues = mutableMapOf<Int, FourierCoefficient>()

    suspend fun calculateCoefficients(number: Int): List<FourierCoefficient> {
        return coroutineScope {
            val terms = ArrayList<Deferred<FourierCoefficient>>(1 + 2 * number)
            for (n in -number..number) {
                val cachedValue = cachedValues[n]
                val job = if (cachedValue == null) {
                    async(Dispatchers.Default) {
                        val cn = calculateCoefficient(n)
                        cachedValues[n] = cn
                        cn
                    }
                } else {
                    async { cachedValue }
                }
                terms.add(job)
            }
            terms.awaitAll()
        }
    }

    private fun calculateCoefficient(n: Int): FourierCoefficient {
        val cn = integrate(0f, 1f, 0.001f) { t ->
            val exp1 = ComplexExponent(0f, -PI_2 * n * t)
            val exp2 = userFunction(t).toExponent()
            (exp1 * exp2).toComplex()
        }.toExponent()
        return FourierCoefficient(cn.amplitude, cn.beta, n)
    }
}
