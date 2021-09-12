package dv.trubnikov.fourier.circles.views.vector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.graphics.withClip
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex

class VectorScaleWindow(context: Context) {

    private val scaleWindowWidth = context.resources.getDimension(R.dimen.scale_window_width)
    private val scaleWindowHeight = context.resources.getDimension(R.dimen.scale_window_height)
    private val scaleWindowPadding = context.resources.getDimension(R.dimen.scale_window_padding)
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

    private var scaleFactor = 2f
    private var height = 0
    private var width = 0

    fun setScale(scaleFactor: Float) {
        this.scaleFactor = scaleFactor
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun withScaleWindow(canvas: Canvas, center: Complex, block: Canvas.() -> Unit) {
        canvas.withClip(
            -width / 2f + scaleWindowPadding,
            height / 2f,
            -width / 2f + scaleWindowPadding + scaleWindowWidth,
            height / 2f - scaleWindowHeight,
        ) {
            withTranslation(
                -width / 2f - scaleFactor * center.real + scaleWindowWidth / 2,
                +height / 2f - scaleFactor * center.image - scaleWindowHeight / 2,
            ) {
                withScale(scaleFactor, scaleFactor, 0f, 0f) {
                    block()
                }
            }
        }
        drawBorder(canvas)
        drawZoomNumber(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRect(
            scaleWindowPadding - width / 2f,
            height / 2f,
            scaleWindowPadding - width / 2f + scaleWindowWidth,
            height / 2f - scaleWindowHeight,
            scaleWindowBorderPaint
        )
    }

    private fun drawZoomNumber(canvas: Canvas) {
        val zoomNumber = if (scaleFactor > 1.5f) {
            "x%.0f".format(scaleFactor)
        } else {
            "x%.1f".format(scaleFactor)
        }
        canvas.withScale(1f, -1f, 0f, 0f) {
            drawText(
                zoomNumber,
                1.5f * scaleWindowPadding - width / 2f,
                1.5f * scaleWindowPadding - height / 2f + scaleWindowHeight,
                scaleWindowTextPaint
            )
        }
    }
}
