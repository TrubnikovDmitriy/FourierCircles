package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import kotlin.math.min

class RadiusIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : IconView(context, attrs, defStyleAttr, defStyleRes) {

    private val circlePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = min(width, height) / 2f * 0.65f
        canvas.withMathCoordinates(width, height) {
            canvas.drawCircle(+size / 5f, +size / 5f, size, circlePaint)
            canvas.drawCircle(-size / 5f, -size / 5f, size, circlePaint)
        }
    }

    override fun onDisableIcon(tunePaint: Paint.() -> Unit) {
        circlePaint.tunePaint()
    }
}