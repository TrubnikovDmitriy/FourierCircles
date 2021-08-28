package dv.trubnikov.fourier.circles.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper

abstract class AxisView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val AXIS_PAINT = Paint().apply {
            color = Color.GRAY
        }
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        drawAxis(canvas)
    }

    private fun drawAxis(canvas: Canvas) {
        // Real axis
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), AXIS_PAINT)
        // Image axis
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, AXIS_PAINT)
    }
}
