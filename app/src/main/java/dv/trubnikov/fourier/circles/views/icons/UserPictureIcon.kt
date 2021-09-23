package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import kotlin.math.min

class UserPictureIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : IconView(context, attrs, defStyleAttr, defStyleRes) {

    private val userPicturePaint = Paint().apply {
        color = context.getColor(R.color.vector_color)
        isAntiAlias = true
        strokeWidth = 10f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(15f, 30f), 0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val size = min(width, height) / 2f * 0.85f
        canvas.withMathCoordinates(width, height) {
            canvas.drawCircle(0f, 0f, size, userPicturePaint)
        }
    }

    override fun onDisableIcon(tunePaint: Paint.() -> Unit) {
        userPicturePaint.tunePaint()
    }
}