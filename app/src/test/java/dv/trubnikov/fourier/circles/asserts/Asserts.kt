package dv.trubnikov.fourier.circles.asserts

import dv.trubnikov.fourier.circles.models.Complex
import org.junit.Assert.assertEquals

const val COMPLEX_DELTA = 1e-7f

fun assertComplexEquals(expected: Complex, actual: Complex) {
    val diff = "expected=$expected, actual=$actual"
    assertEquals(
        "Real parts are not equals: $diff",
        expected.real, actual.real, COMPLEX_DELTA)

    assertEquals(
        "Image parts are not equals: $diff",
        expected.image, actual.image, COMPLEX_DELTA
    )
}