package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import kotlin.math.PI
import kotlin.math.cos

class ArrowIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : IconView(context, attrs, defStyleAttr, defStyleRes) {


    private val arrowPath = Path()
    private val arrowPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
        strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.withMathCoordinates(width, height) {
            withRotation(45f) {
                withTranslation(-width / 2f) {
                    val lineWidth = width / 30f
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
                }
            }
        }
    }

    override fun onDisableIcon(tunePaint: Paint.() -> Unit) {
        arrowPaint.tunePaint()
    }
}