package dv.trubnikov.fourier.circles.views.drawers.vectors

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.views.drawers.CanvasDrawer
import kotlin.math.max

class AxisDrawer(context: Context) : CanvasDrawer {

    private val backgroundColor = context.getColor(R.color.vector_background_color)
    private val axisPaint = Paint().apply {
        color = Color.GRAY
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        val size = max(canvas.width, canvas.height).toFloat()
        canvas.drawColor(backgroundColor)
        canvas.drawLine(-size, 0f, size, 0f, axisPaint)
        canvas.drawLine(0f, -size, 0f, size, axisPaint)
    }
}
