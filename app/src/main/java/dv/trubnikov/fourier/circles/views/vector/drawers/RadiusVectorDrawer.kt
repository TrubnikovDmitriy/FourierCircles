package dv.trubnikov.fourier.circles.views.vector.drawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.views.vector.VectorDrawer

class RadiusVectorDrawer : VectorDrawer {

    private val circlePaint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        for (vector in vectors) {
            canvas.drawCircle(vector.src.real, vector.src.image, vector.length, circlePaint)
        }
    }
}
