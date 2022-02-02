package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.Picture
import dv.trubnikov.fourier.circles.models.Tick

class TraceCalculator {
    fun calculateTrace(picture: Picture, vectorSize: Int, pointSize: Int): Array<Complex> {
        assert(pointSize > 2) { "Trace must have at least 2 points" }
        assert(vectorSize <= picture.size) { "Picture have only ${picture.size} vectors" }

        if (vectorSize <= 0) {
            return emptyArray()
        }

        val step = 1f / (pointSize - 1)
        val trace = Array<Complex>(pointSize) { i ->
            val tick = Tick(i * step)
            val frame = picture.valueFor(tick)
            val vector = frame.vectors[vectorSize - 1]
            vector.dst
        }

        return trace
    }
}
