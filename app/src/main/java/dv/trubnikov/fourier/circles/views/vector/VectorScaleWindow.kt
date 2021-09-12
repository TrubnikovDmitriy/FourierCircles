package dv.trubnikov.fourier.circles.views.vector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import androidx.core.graphics.withClip
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex

class VectorScaleWindow(context: Context) {

    private val scaleWindowWidth = context.resources.getDimension(R.dimen.scale_window_width)
    private val scaleWindowHeight = context.resources.getDimension(R.dimen.scale_window_height)
    private val scaleWindowPadding = context.resources.getDimension(R.dimen.scale_window_padding)
    private val scaleWindowRect = RectF(
        scaleWindowPadding,
        -scaleWindowPadding,
        scaleWindowPadding + scaleWindowWidth,
        -scaleWindowPadding - scaleWindowHeight,
    )
    private val scaleWindowBorderPaint = Paint().apply {
        strokeWidth = 3f
        color = context.getColor(R.color.vector_color)
        style = Paint.Style.STROKE
    }
    private val scaleWindowTextPaint = Paint().apply {
        textSize = 50f
        isAntiAlias = true
        color = context.getColor(R.color.vector_color)
    }
    private val scaleTextBound = Rect()

    private var scaleFactor = 2f

    fun setScale(scaleFactor: Float) {
        this.scaleFactor = scaleFactor
    }

    fun withScaleWindow(canvas: Canvas, center: Complex, block: Canvas.() -> Unit) {
        canvas.withClip(
            -canvas.width / 2 + scaleWindowRect.left,
            canvas.height / 2 + scaleWindowRect.top,
            -canvas.width / 2 + scaleWindowRect.right,
            canvas.height / 2 + scaleWindowRect.bottom,
        ) {
            withTranslation(
                -width / 2f - scaleFactor * center.real + scaleWindowRect.width() / 2,
                +height / 2f - scaleFactor * center.image + scaleWindowRect.height() / 2,
            ) {
                withScale(scaleFactor, scaleFactor, 0f, 0f) {
                    block()
                }
            }
        }
        canvas.withTranslation(-canvas.width / 2f, canvas.height / 2f) {
            drawBorder(canvas)
            drawZoomNumber(canvas)
        }
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRect(scaleWindowRect, scaleWindowBorderPaint)
    }

    private fun drawZoomNumber(canvas: Canvas) {
        val zoomNumber = if (scaleFactor > 1.5f) {
            "x%.0f".format(scaleFactor)
        } else {
            "x%.1f".format(scaleFactor)
        }
        scaleWindowTextPaint.getTextBounds(zoomNumber, 0, zoomNumber.length, scaleTextBound)
        canvas.withScale(1f, -1f, 0f, 0f) {
            drawText(
                zoomNumber,
                scaleWindowPadding / 2f + scaleWindowRect.left,
                scaleWindowPadding / 2f + scaleTextBound.height() - scaleWindowRect.bottom,
                scaleWindowTextPaint
            )
        }
    }
}
