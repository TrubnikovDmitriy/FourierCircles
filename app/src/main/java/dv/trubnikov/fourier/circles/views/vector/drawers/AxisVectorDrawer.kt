package dv.trubnikov.fourier.circles.views.vector.drawers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.views.vector.VectorDrawer
import kotlin.math.max

class AxisVectorDrawer(context: Context) : VectorDrawer {

    private val backgroundColor = context.getColor(R.color.vector_background_color)
    private val axisPaint = Paint().apply {
        color = Color.GRAY
    }

    private var size = 0f

    override fun onSizeChanged(width: Int, height: Int) {
        size = max(width, height).toFloat()
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        canvas.drawColor(backgroundColor)
        canvas.drawLine(-size, 0f, size, 0f, axisPaint)
        canvas.drawLine(0f, -size, 0f, size, axisPaint)
    }
}
