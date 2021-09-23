package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import dv.trubnikov.fourier.circles.R

class ScaleIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : IconView(context, attrs, defStyleAttr, defStyleRes) {

    private val scaleNumber = "x42"
    private val scaleBound = Rect()
    private val scalePaint = Paint().apply {
        textSize = 60f
        isAntiAlias = true
        color = context.getColor(R.color.vector_color)
    }

    init {
        scalePaint.getTextBounds(scaleNumber, 0, scaleNumber.length, scaleBound)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.apply {
            val freeHeight = (height + scaleBound.height()) / 2f
            val freeWidth = (width - scaleBound.width()) / 2f
            drawText(scaleNumber, freeWidth, freeHeight, scalePaint)
        }
    }

    override fun onDisableIcon(tunePaint: Paint.() -> Unit) {
        scalePaint.tunePaint()
    }
}