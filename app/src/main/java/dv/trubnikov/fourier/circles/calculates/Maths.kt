package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.div
import dv.trubnikov.fourier.circles.models.plus
import dv.trubnikov.fourier.circles.models.times
import kotlin.math.PI

const val PI_2: Float = 2 * PI.toFloat()

fun integrate(t0: Float, t1: Float, dt: Float, function: (parameter: Float) -> Complex): Complex {
    var sum = Complex(0f, 0f)
    var t = t0
    while (t + dt <= t1) {
        val y0 = function(t)
        val y1 = function(t + dt)
        sum += dt * (y1 + y0) / 2f
        t += dt
    }
    return sum
}
