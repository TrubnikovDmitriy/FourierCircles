package dv.trubnikov.fourier.circles.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.FourierCoefficient
import dv.trubnikov.fourier.circles.models.plus
import dv.trubnikov.fourier.circles.models.toDegree
import kotlin.math.*


class VectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : AxisView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val MIN_ARROW_WIDTH = 30f

        private val VECTOR_PAINT = Paint().apply {
            color = Color.YELLOW
            strokeWidth = 3f
        }
        private val VECTOR_TRACE_PAINT = Paint().apply {
            color = Color.RED
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }
        private val ARROW_CIRCLE_PAINT = Paint().apply {
            color = Color.GRAY
            isAntiAlias = true
            strokeWidth = 1f
            style = Paint.Style.STROKE
        }
    }

    init {
        setBackgroundColor(context.getColor(R.color.vector_background_color))
    }

    private var vectors = emptyList<FourierCoefficient>()
    private var animation: Animation? = null
    private val vectorTrace = Path()
    private val arrowPath = Path()

    fun setVectors(vectors: List<FourierCoefficient>) {
        post {
            animation = Animation()
            this.vectors = vectors
            vectorTrace.rewind()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        animation?.let {
            drawVectors(canvas, vectors, it.time)
            if (!it.isAnimating) it.reset()
            invalidate()
        }
    }

    private fun drawVectors(canvas: Canvas, vectors: List<FourierCoefficient>, time: Float) {
        var sourcePoint = Complex(width / 2f, height / 2f)
        canvas.drawPath(vectorTrace, VECTOR_TRACE_PAINT)
        for (vector in vectors) {
            val (x0, y0) = sourcePoint
            sourcePoint += vector.toComplex(time)
            val (x1, y1) = sourcePoint
            // In android Oy is inverted
            drawVector(canvas, x0, height - y0, x1, height - y1)
        }
        addTracePoint(vectorTrace, sourcePoint)
    }

    private fun drawVector(canvas: Canvas, x0: Float, y0: Float, x1: Float, y1: Float) {
        val length = hypot(y1 - y0, x1 - x0)
        val angle = atan2(y1 - y0, x1 - x0)
        canvas.withTranslation(x0, y0) {
            canvas.withRotation(angle.toDegree()) {
                drawCircle(canvas, length)
                canvas.drawLine(0f, 0f, length, 0f, VECTOR_PAINT)
                drawArrow(canvas, length)
            }
        }
    }

    private fun drawArrow(canvas: Canvas, length: Float) {
        val arrowLength = min(MIN_ARROW_WIDTH, length * 0.25f)
        val arrowHeight = arrowLength * cos(PI.toFloat() / 6)
        arrowPath.reset()
        arrowPath.moveTo(length, 0f)
        arrowPath.lineTo(length - arrowHeight, +arrowLength / 2)
        arrowPath.lineTo(length - arrowHeight, -arrowLength / 2)
        arrowPath.close()
        canvas.drawPath(arrowPath, VECTOR_PAINT)
    }

    private fun drawCircle(canvas: Canvas, radius: Float) {
        canvas.drawCircle(0f, 0f, radius, ARROW_CIRCLE_PAINT)
    }

    private fun addTracePoint(path: Path, nextPoint: Complex) {
        if (path.isEmpty) {
            val (startX, startY) = nextPoint
            path.moveTo(startX, height - startY)
        } else {
            val (nextX, nextY) = nextPoint
            path.lineTo(nextX, height - nextY)
        }
    }

    private class Animation(private val slowFactor: Int = 20) {

        private var startTime = System.currentTimeMillis()
        private var endTime = startTime + 1 * 1000 * slowFactor

        val isAnimating: Boolean
            get() = System.currentTimeMillis() < endTime

        val time: Float
            get() = (System.currentTimeMillis() - startTime).toFloat() / 1000 / slowFactor

        fun reset() {
            startTime = System.currentTimeMillis()
            endTime = startTime + 1 * 1000 * slowFactor
        }
    }
}