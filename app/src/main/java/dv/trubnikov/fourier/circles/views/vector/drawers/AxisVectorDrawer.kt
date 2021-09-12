package dv.trubnikov.fourier.circles.views.vector.drawers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.views.vector.VectorDrawer

class AxisVectorDrawer(context: Context) : VectorDrawer {

    private val backgroundColor = context.getColor(R.color.vector_background_color)
    private val axisPaint = Paint().apply {
        color = Color.GRAY
    }

    private var halfWidth = 0f
    private var halfHeight = 0f

    override fun onSizeChanged(width: Int, height: Int) {
        halfWidth = width / 2f
        halfHeight = height / 2f
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        canvas.drawColor(backgroundColor)
        canvas.drawLine(-halfWidth, 0f, halfWidth, 0f, axisPaint)
        canvas.drawLine(0f, -halfHeight, 0f, halfHeight, axisPaint)
    }
}
