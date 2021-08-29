package dv.trubnikov.fourier.circles.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.presentation.vector.controllers.PictureController
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex
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
    }

    private val VECTOR_PAINT = Paint().apply {
        color = Color.YELLOW
        strokeWidth = 3f
    }
    private val USER_PATH_PAINT = Paint().apply {
        color = context.getColor(R.color.vector_color)
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }
    private val FOURIER_PATH_PAINT = Paint().apply {
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

    init {
        setBackgroundColor(context.getColor(R.color.vector_background_color))
    }

    private var lastState: PictureController.VectorPicture? = null

    private val originalPath = Path()
    private val arrowPath = Path()

    fun drawVectorPicture(picture: PictureController.VectorPicture) {
        lastState = picture
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // TODO: Строить точки не на мейн потоке, а в фоне перед вызовом postInvalidate()
        super.onDraw(canvas)
        val picture = lastState ?: return

        drawFourierPath(canvas, picture.drawingPath)

        originalPath.let { path ->
            picture.originalPath.toPath(path)
            path.close()
            canvas.drawPath(path, USER_PATH_PAINT)
        }

        drawVectors(canvas, picture.vectors)
    }

    private fun drawVectors(canvas: Canvas, vectors: List<Complex>) {
        var x = width / 2f
        var y = height / 2f
        var x0: Float
        var y0: Float
        for (vector in vectors) {
            x0 = x
            y0 = y
            x += vector.real
            y += vector.image
            drawVector(canvas, x0, height - y0, x, height - y)
        }
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

    private fun drawFourierPath(canvas: Canvas, points: List<Complex>) {
        if (points.size < 2) {
            return
        }
        var x1 = width / 2 + points.first().real
        var y1 = height / 2 - points.first().image
        var x0: Float
        var y0: Float
        for (i in 1..points.lastIndex) {
            val alpha = 255 * i / points.lastIndex
            FOURIER_PATH_PAINT.alpha = alpha.coerceIn(150, 255)
            x0 = x1
            y0 = y1
            x1 = width / 2 + points[i].real
            y1 = height / 2 - points[i].image
            canvas.drawLine(x0, y0, x1, y1, FOURIER_PATH_PAINT)
        }
    }

    private fun List<Complex>.toPath(path: Path) {
        path.rewind()
        val halfScreenX = width / 2
        val halfScreenY = height / 2
        for (index in this.indices) {
            val point = get(index)
            val x = halfScreenX + point.real
            val y = halfScreenY - point.image
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
    }
}