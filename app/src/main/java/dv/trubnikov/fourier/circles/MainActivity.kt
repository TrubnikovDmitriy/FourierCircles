package dv.trubnikov.fourier.circles

import android.os.Bundle
import androidx.activity.ComponentActivity
import dv.trubnikov.fourier.circles.calculates.FourierCalculator
import dv.trubnikov.fourier.circles.calculates.PI_2
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.FourierCoefficient
import dv.trubnikov.fourier.circles.views.VectorView
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class MainActivity : ComponentActivity() {

    private val vectorView by lazy { findViewById<VectorView>(R.id.vector_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onStart() {
        super.onStart()
//        val vectors = arrayOf(
//            FourierCoefficient(150f, 2 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(110f, 4 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(100f, -4 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(50f, 9 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(75f, -12 * PI.toFloat() / 10000f, 0f),
//        )
//        val vectors = arrayOf(
//            FourierCoefficient(300f, 2 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(75f, 8 * PI.toFloat() / 10000f, 0f),
//            FourierCoefficient(-75f, 9 * PI.toFloat() / 10000f, 0f),
//        )
        val fourierCalculator = FourierCalculator { t ->
            val y = 13f * cos(PI_2 * t) - 5 * cos(PI_2 * 2 * t) - 2 * cos(PI_2 * 3 * t) - 4 * cos(PI_2 * 4 * t)
            val x = 16f * sin(PI_2 * t).pow(3)
            Complex(20*x, 15*y)
//            Complex(400*cos(PI_2 / 4 * 3 * t), 400*sin(PI_2 / 4 * 3 * t))
        }
        val termsCount = 6
        val terms = fourierCalculator.calculateCoefficient(termsCount)
        val coefficients = ArrayList<FourierCoefficient>(terms.size)
        for (i in 0..termsCount) {
            if (i == 0) {
                coefficients.add(terms.getValue(i))
            } else {
                coefficients.add(terms.getValue(-i))
                coefficients.add(terms.getValue(+i))
            }
        }
        vectorView.setVectors(coefficients)
    }
}
