package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.withScale
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.FourierCoefficient
import dv.trubnikov.fourier.circles.models.Tick
import dv.trubnikov.fourier.circles.models.plus
import dv.trubnikov.fourier.circles.tools.withMathCoordinates

class TraceIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : IconView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private fun drawFunction(t: Tick): Complex {
            val coefficients = listOf(
                FourierCoefficient(-1, 151f, +2.6318593f),
                FourierCoefficient(+1, 65f, -0.7033255f),
                FourierCoefficient(-2, 245f, +0.18047173f),
                FourierCoefficient(+2, 134f, -2.1442108f),
            )
            var sum = Complex(0f, 0f)
            for (coef in coefficients) {
                sum += coef.toComplex(coef.angle(t))
            }
            return sum
        }
    }

    private val tracePath = Path().apply {
        var t = 0f
        val step = 0.01f
        while (t <= 1f) {
            val (x, y) = drawFunction(Tick(t))
            if (t == 0f) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
            t += step
        }
    }
    private val tracePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = 25f
    }
    private val traceRect = RectF().apply {
        tracePath.computeBounds(this, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scaleX = width / (traceRect.width() + 5 * tracePaint.strokeWidth)
        val scaleY = height / (traceRect.height() + 5 * tracePaint.strokeWidth)
        canvas.withMathCoordinates(width, height) {
            withScale(scaleX, scaleY) {
                canvas.drawPath(tracePath, tracePaint)
            }
        }
    }

    override fun onDisableIcon(tunePaint: Paint.() -> Unit) {
        tracePaint.tunePaint()
    }
}