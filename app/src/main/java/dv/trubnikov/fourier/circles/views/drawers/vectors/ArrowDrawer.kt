package dv.trubnikov.fourier.circles.views.drawers.vectors

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.models.toDegree
import dv.trubnikov.fourier.circles.views.drawers.CanvasDrawer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min

class ArrowDrawer : CanvasDrawer {

    companion object {
        private const val MIN_ARROW_WIDTH = 30f
        private const val MAX_LINE_WIDTH = 3f
    }

    private val arrowPath = Path()
    private val arrowPaint = Paint().apply {
        color = Color.YELLOW
        isAntiAlias = true
        strokeWidth = 3f
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        for (vector in vectors) {
            canvas.withTranslation(vector.src.real, vector.src.image) {
                canvas.withRotation(vector.angle.toDegree()) {
                    val lineWidth = min(vector.length / 30f, MAX_LINE_WIDTH)
                    arrowPaint.strokeWidth = lineWidth
                    canvas.drawLine(0f, 0f, vector.length - lineWidth, 0f, arrowPaint)
                    drawArrow(canvas, vector.length)
                }
            }
        }
    }

    private fun drawArrow(canvas: Canvas, length: Float) {
        val arrowLength = min(MIN_ARROW_WIDTH, length / 4f)
        val arrowHeight = arrowLength * cos(PI.toFloat() / 6)
        arrowPath.reset()
        arrowPath.moveTo(length, 0f)
        arrowPath.lineTo(length - arrowHeight, +arrowLength / 2)
        arrowPath.lineTo(length - arrowHeight, -arrowLength / 2)
        arrowPath.close()
        canvas.drawPath(arrowPath, arrowPaint)
    }
}
