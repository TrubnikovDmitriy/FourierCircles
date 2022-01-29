package dv.trubnikov.fourier.circles.views.drawers.vectors

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.SparseIntArray
import androidx.annotation.ColorInt
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

    private val vectorColors = SparseIntArray()
    private val arrowPath = Path()
    private val arrowPaint = Paint().apply {
        isAntiAlias = true
        strokeWidth = 3f
    }

    fun setVectorColor(vectorIndex: Int, @ColorInt color: Int) {
        vectorColors.put(vectorIndex, color)
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        vectors.forEachIndexed { index, vector ->
            canvas.withTranslation(vector.src.real, vector.src.image) {
                canvas.withRotation(vector.angle.toDegree()) {
                    arrowPaint.color = vectorColors.get(index, Color.YELLOW)
                    val lineWidth = min(vector.length / 30f, MAX_LINE_WIDTH)
                    val arrowLength = min(MIN_ARROW_WIDTH, vector.length / 4f)
                    val arrowHeight = arrowLength * cos(PI.toFloat() / 6)
                    val lineLength = vector.length - arrowHeight
                    arrowPaint.strokeWidth = lineWidth
                    canvas.drawLine(0f, 0f, lineLength, 0f, arrowPaint)
                    withTranslation(x = lineLength) {
                        drawArrowTip(canvas, arrowLength, arrowHeight)
                    }
                }
            }
        }
    }

    /**
     * Draw this shape ▶
     */
    private fun drawArrowTip(canvas: Canvas, tipLength: Float, tipHeight: Float) {
        arrowPath.reset()
        arrowPath.moveTo(tipHeight, 0f) // right corner
        arrowPath.lineTo(0f, +tipLength / 2) // top corner
        arrowPath.lineTo(0f, -tipLength / 2) // bottom corner
        arrowPath.close()
        canvas.drawPath(arrowPath, arrowPaint)
    }
}
