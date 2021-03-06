package dv.trubnikov.fourier.circles.tools

import dv.trubnikov.fourier.circles.calculates.PI_2
import dv.trubnikov.fourier.circles.models.Complex
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

object PictureSamples {

    private val HEART = { t: Float ->
        val y = 13f * cos(PI_2 * t) -
                5 * cos(PI_2 * 2 * t) -
                2 * cos(PI_2 * 3 * t) -
                4 * cos(PI_2 * 4 * t)
        val x = 16f * sin(PI_2 * t).pow(3)
        Complex(15 * x, 15 * y)
    }

    private val HELIX = { t: Float ->
        val x = t * sin(3 * PI_2 * t)
        val y = t * cos(3 * PI_2 * t)
        Complex(300 * x, 300 * y)
    }

    private val DELTOID = { t: Float ->
        val x = 2 * cos(PI_2 * t) + cos(2 * PI_2 * t)
        val y = 2 * sin(PI_2 * t) - sin(2 * PI_2 * t)
        Complex(200 * x, 200 * y)
    }

    private val HYPOCYCLOID_1 = { t: Float ->
        val y = 20 * (sin(PI_2 * t) - sin(5 * PI_2 * t) / 5)
        val x = 20 * (cos(PI_2 * t) + cos(5 * PI_2 * t) / 5)
        Complex(50 * x, 50 * y)
    }

    private val HYPOCYCLOID_2 = { t: Float ->
        val x = 4.4f * (cos(10 * PI_2 * t) + cos(1.1f * 10 * PI_2 * t) / 1.1f)
        val y = 4.4f * (sin(10 * PI_2 * t) - sin(1.1f * 10 * PI_2 * t) / 1.1f)
        Complex(50 * x, 50 * y)
    }

    private val HYPOCYCLOID_3 = { t: Float ->
        val x = 6.2f * (cos(10 * PI_2 * t) - cos(3.1f * 10 * PI_2 * t) / 3.1f)
        val y = 6.2f * (sin(10 * PI_2 * t) - sin(3.1f * 10 * PI_2 * t) / 3.1f)
        Complex(50 * x, 50 * y)
    }

    private val LISSAJOUS = { t: Float ->
        val y = sin(5 * PI_2 * t + PI_2 / 4)
        val x = cos(6 * PI_2 * t)
        Complex(200 * x, 200 * y)
    }

    private val TEST = { t: Float ->
        val R = 100f
        when {
            t < 0.3f -> {
                val y = R * sin(10f / 3f * -PI_2 * t + PI_2 / 4) - R
                val x = R * cos(10f / 3f * -PI_2 * t + PI_2 / 4) + R
                Complex(x, y)
            }
            t < 0.6f -> {
                val y = R * sin(10f / 3f * -PI_2 * t + PI_2 / 4) - R
                val x = R * cos(10f / 3f * -PI_2 * t + PI_2 / 4) - R
                Complex(x, y)
            }
            t < 0.75f -> {
                Complex(-R, R * (t - 0.60f) * 20)
            }
            t < 0.85f -> {
                val y = R * sin(10f * PI_2 * t) + 3 * R
                val x = R * cos(10f * PI_2 * t + PI_2 / 2)
                Complex(x, y)
            }
            else -> {
                Complex(R, R * (1 - t) * 20)
            }
        }
    }
}
