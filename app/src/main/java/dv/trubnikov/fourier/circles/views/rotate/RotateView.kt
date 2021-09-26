package dv.trubnikov.fourier.circles.views.rotate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.core.graphics.withRotation
import androidx.core.graphics.withScale
import dv.trubnikov.fourier.circles.models.toDegree
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import dv.trubnikov.fourier.circles.views.SquareView
import kotlin.math.PI
import kotlin.math.cos

class RotateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : SquareView(context, attrs, defStyleAttr, defStyleRes) {

    private val arrowPath = Path()
    private val arrowPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
        strokeWidth = 3f
    }
    private val radiusPaint = Paint().apply {
        color = Color.LTGRAY
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private var angle: Float = 0f
    private var length: Float = 0f

    fun setVector(angle: Float, length: Float) {
        this.angle = angle
        this.length = length
        invalidate()
    }

    fun setActive(isActive: Boolean) {
        if (isActive) {
            arrowPaint.color = Color.YELLOW
            radiusPaint.color = Color.LTGRAY
            arrowPaint.alpha = 255
            radiusPaint.alpha = 255
        } else {
            arrowPaint.color = Color.GRAY
            radiusPaint.color = Color.GRAY
            arrowPaint.alpha = 120
            radiusPaint.alpha = 120
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withMathCoordinates(width, height) {
            withRotation(angle.toDegree()) {
                // TODO move out to Canvas extension?
                withScale(0.5f, 0.5f) {
                    val lineWidth = width / 20f
                    val vectorLength = width.toFloat()
                    val arrowLength = vectorLength / 4f
                    val arrowHeight = arrowLength * cos(PI.toFloat() / 6)
                    arrowPaint.strokeWidth = lineWidth
                    arrowPath.apply {
                        reset()
                        moveTo(vectorLength, 0f)
                        lineTo(vectorLength - arrowHeight, +arrowLength / 2)
                        lineTo(vectorLength - arrowHeight, -arrowLength / 2)
                        close()
                    }
                    drawPath(arrowPath, arrowPaint)
                    drawLine(0f, 0f, vectorLength - arrowHeight, 0f, arrowPaint)
                    drawCircle(0f, 0f, vectorLength - radiusPaint.strokeWidth, radiusPaint)
                }
            }
        }
    }
}