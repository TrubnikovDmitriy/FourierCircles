package dv.trubnikov.fourier.circles.views.vector.drawers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.views.vector.VectorDrawer

class TraceVectorDrawer : VectorDrawer {

    companion object {
        private const val MIN_TRACE_ALPHA = 150
    }

    private var isRepeating = false
    private var picture: VectorPicture? = null

    private val trace = ArrayList<FourierVector>()
    private val tracePaint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        strokeCap = Paint.Cap.BUTT
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    override fun onPictureUpdate(picture: VectorPicture) {
        this.picture = picture
        isRepeating = false
        trace.clear()
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        trace.add(vectors.last())

        if (isRepeating && trace.isNotEmpty()) {
            trace.removeFirst()
        }

        drawTrace(canvas, trace)
    }

    override fun onVectorCountChanged(vectorCount: Int) {
        val picture = picture ?: return
        val newTrace = ArrayList<FourierVector>(trace.size)
        for (vector in trace) {
            val newVector = picture.valueFor(vector.tick)
            newTrace.add(newVector.vectors[vectorCount - 1])
        }
        trace.clear()
        trace.addAll(newTrace)
    }

    override fun onAnimationRepeat() {
        isRepeating = true
    }

    private fun drawTrace(canvas: Canvas, trace: List<FourierVector>) {
        if (trace.size < 2) {
            return
        }
        var x0 = trace.first().dst.real
        var y0 = trace.first().dst.image
        for (i in 1..trace.lastIndex) {
            val alpha = 255 * i / trace.lastIndex
            tracePaint.alpha = alpha.coerceIn(MIN_TRACE_ALPHA, 255)
            val point = trace[i]
            canvas.drawLine(x0, y0, point.dst.real, point.dst.image, tracePaint)
            x0 = point.dst.real
            y0 = point.dst.image
        }
    }
}
