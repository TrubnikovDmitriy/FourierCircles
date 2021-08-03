package dv.trubnikov.fourier.circles.calculates

import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.ComplexExponent
import dv.trubnikov.fourier.circles.models.module
import dv.trubnikov.fourier.circles.models.toComplex
import org.junit.Assert
import org.junit.Test
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class MathsTest {

    @Test
    fun `Integrate half circle`() {
        val circleR = 1f

        val circleArea = integrate(-1f, 1f, 0.001f) { x ->
            val y = sqrt(circleR.pow(2) - x.pow(2))
            Complex(y, 0f)
        }

        Assert.assertEquals(PI.toFloat() * circleR.pow(2), circleArea.real * 2f, 0.001f)
    }

    @Test
    fun `Integrate circle`() {
        val circleArea = integrate(0f, 1f, 0.001f) { t ->
            ComplexExponent(alpha = 0f, beta = 2 * PI.toFloat() * t).toComplex()
        }

        Assert.assertEquals(0f, circleArea.module(), 0.001f)
    }
}