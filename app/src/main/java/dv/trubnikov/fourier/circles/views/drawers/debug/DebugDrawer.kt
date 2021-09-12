package dv.trubnikov.fourier.circles.views.drawers.debug

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.withScale
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.views.drawers.CanvasDrawer

@Suppress("Unused")
class DebugDrawer : CanvasDrawer {

    private val numberPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 35f
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        for (x in -5..5) {
            for (y in -9..9) {
                canvas.withScale(1f, -1f, 0f, 0f) {
                    drawText("[$x;$y]", x * 100f - 35f, y * 100f + 15f, numberPaint)
                }
            }
        }
    }
}
