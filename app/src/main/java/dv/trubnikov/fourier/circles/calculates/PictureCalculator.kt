package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.calculates.PictureCalculator.Picture.PicturePoint
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.FourierCoefficient
import dv.trubnikov.fourier.circles.models.plus
import kotlinx.coroutines.*

class PictureCalculator(
    private val fourierCalculator: FourierCalculator
) {

    companion object {
        private const val TIME_RANGE_STEP = 0.0001f
    }

    /**
     * Object containing precalculated cached values of the picture.
     */
    fun interface Picture {

        class PicturePoint(
            val time: Tick,
            val point: Complex,
            val vectors: List<Complex>,
        )

        /**
         * Returns the point of picture at the specified [time].
         */
        fun valueAt(time: Tick): PicturePoint
    }

    /**
     * Calculates the whole picture using Fourier series
     * with a given number of terms [vectorNumber].
     */
    suspend fun calculatePicture(vectorNumber: Int): Picture = coroutineScope {
        val n = (vectorNumber - 1) / 2
        val coefs = fourierCalculator.calculateCoefficient(n).sorted()
        val picturePointCount = (1f / TIME_RANGE_STEP).toInt()
        val picturePoints = ArrayList<Deferred<PicturePoint>>()
        for (pointNumber in 0..picturePointCount) {
            val time = Tick(TIME_RANGE_STEP * pointNumber)
            val point = async(Dispatchers.Default) {
                calculatePoint(coefs, time)
            }
            picturePoints.add(point)
        }
        PictureWithoutInterpolation(picturePoints.awaitAll())
    }


    private fun calculatePoint(coefficients: List<FourierCoefficient>, time: Tick): PicturePoint {
        var sumVector = Complex(0f, 0f)
        val vectors = ArrayList<Complex>(coefficients.size)

        coefficients.forEach { coef ->
            val vector = coef.toComplex(time.value)
            sumVector += vector
            vectors.add(vector)
        }

        return PicturePoint(
            time = time,
            point = sumVector,
            vectors = vectors
        )
    }

    private class PictureWithoutInterpolation(
        private val sortedPoints: List<PicturePoint>
    ) : Picture {

        init {
            if (sortedPoints.isEmpty()) {
                throw IllegalArgumentException("Picture points must not be empty")
            }
        }

        override fun valueAt(time: Tick): PicturePoint {
            val index = sortedPoints.binarySearch {
                val diff = it.time - time
                when {
                    diff < 0f -> -1
                    diff > 0f -> +1
                    else -> 0
                }
            }
            if (index >= 0) {
                return sortedPoints[index]
            } else {
                val correctIndex = (-index - 1).coerceAtMost(sortedPoints.lastIndex)
                return sortedPoints[correctIndex]
            }
        }
    }
}