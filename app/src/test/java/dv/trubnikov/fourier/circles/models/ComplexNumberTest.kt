package dv.trubnikov.fourier.circles.models

import dv.trubnikov.fourier.circles.asserts.assertComplexEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ComplexNumberTest(private val complexNumber: Complex) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun categoryList(): List<Complex> {
            return listOf(
                0 + 0.j,
                1 + 0.j,
                0 + 1.j,
                1 + 1.j,
                0.707 + 0.707.j,
                0.100500 + 0.42.j,
            )
        }
    }

    @Test
    fun `Convert from complex to exponent and back`() {
        val vector = complexNumber.toExponent()

        val actualComplex = vector.toComplex()

        assertComplexEquals(complexNumber, actualComplex)
    }
}
