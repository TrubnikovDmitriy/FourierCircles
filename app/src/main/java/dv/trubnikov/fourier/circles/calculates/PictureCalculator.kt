package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.*
import kotlinx.coroutines.*

class PictureCalculator(
    private val fourierCalculator: FourierCalculator
) {

    companion object {
        private const val TIME_RANGE_STEP = 0.0001f
    }

    /**
     * Calculates the whole picture using Fourier series
     * with a given number of terms [vectorsCount].
     */
    suspend fun calculatePicture(vectorsCount: Int): List<PictureFrame> = coroutineScope {
        val coefs = fourierCalculator.calculateCoefficients(vectorsCount / 2).sorted()
        val picturePointCount = (1f / TIME_RANGE_STEP).toInt()
        val pictureTicks = ArrayList<Deferred<PictureFrame>>()
        for (pointNumber in 0..picturePointCount) {
            val time = Tick(TIME_RANGE_STEP * pointNumber)
            val point = async(Dispatchers.Default) {
                calculatePoint(coefs, time)
            }
            pictureTicks.add(point)
        }
        pictureTicks.awaitAll()
    }


    private fun calculatePoint(coefficients: List<FourierCoefficient>, tick: Tick): PictureFrame {
        val vectors = ArrayList<FourierVector>(coefficients.size)

        coefficients.forEach { coef ->
            val angle = coef.angle(tick)
            val vector = coef.toComplex(angle)
            val prevVectorEnd = vectors.lastOrNull()?.dst ?: Complex(0f, 0f)
            val fourierVector = FourierVector(
                tick = tick,
                src = prevVectorEnd,
                dst = prevVectorEnd + vector,
                angle = angle,
                coefficient = coef
            )
            vectors.add(fourierVector)
        }

        return PictureFrame(tick = tick, vectors = vectors)
    }
}