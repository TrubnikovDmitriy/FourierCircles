package dv.trubnikov.fourier.circles.views.vector

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.withClip
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.Tick
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import dv.trubnikov.fourier.circles.views.drawers.CanvasDrawer
import dv.trubnikov.fourier.circles.views.drawers.vectors.*
import kotlin.math.min

class VectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    init {
        setBackgroundColor(context.getColor(R.color.vector_background_color))
    }

    /**
     * Drawers are arranged in the order in which they
     * will be rendered, so the order is important.
     */
    private val drawers: List<CanvasDrawer> = listOf(
        AxisDrawer(context),
        TraceDrawer(),
        UserPictureDrawer(context),
        RadiusDrawer(),
        ArrowDrawer(),
    )

    private val animator = ValueAnimator.ofFloat(Tick.MIN_VALUE, Tick.MAX_VALUE).apply {
        interpolator = LinearInterpolator()
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 10_000
        addUpdateListener { invalidate() }
        doOnRepeat { drawers.forEach { it.onAnimationRepeat() } }
    }

    private val scaleWindowWidth = resources.getDimension(R.dimen.scale_window_width)
    private val scaleWindowHeight = resources.getDimension(R.dimen.scale_window_height)
    private val scaleWindowBorderPaint = Paint().apply {
        strokeWidth = 3f
        color = context.getColor(R.color.vector_color)
        style = Paint.Style.STROKE
    }

    private var picture: VectorPicture? = null
    private var vectorCount: Int = 0
    private var scaleFactor = 2f

    fun resume() {
        animator.resume()
    }

    fun pause() {
        animator.pause()
    }

    fun setPicture(picture: VectorPicture) {
        post {
            this.picture = picture
            drawers.forEach { drawer -> drawer.onPictureUpdate(picture) }
            animator.start()
        }
    }

    fun setVectorCount(vectorCount: Int) {
        post {
            this.vectorCount = vectorCount
            drawers.forEach { drawer ->
                drawer.onVectorCountChanged(vectorCount)
            }
        }
    }

    fun setScaleFactor(scaleFactor: Float) {
        this.scaleFactor = scaleFactor
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val picture = picture ?: return
        val tick = Tick(animator.animatedValue as Float)
        val pictureFrame = picture.valueFor(tick)
        val vectors = pictureFrame.vectors.subList(0, vectorCount)

        val widthScale = (width - paddingLeft - paddingRight).toFloat() / width
        val heightScale = (height - paddingTop - paddingBottom).toFloat() / height
        val scale = min(widthScale, heightScale)
        canvas.withScale(x = scale, y = scale, pivotX = width / 2f, pivotY = height / 2f) {
            canvas.withMathCoordinates(width, height) {
                drawers.forEach { vectorDrawer ->
                    vectorDrawer.onDraw(canvas, vectors)
                }
                withScaleWindow(vectors.last().dst, scaleFactor) {
                    drawers.forEach { vectorDrawer ->
                        vectorDrawer.onDraw(canvas, vectors)
                    }
                }
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            drawers.forEach { it.onSizeChanged(width = width, height = height) }
        }
    }

    private fun Canvas.withScaleWindow(
        center: Complex,
        scaleFactor: Float,
        block: Canvas.() -> Unit
    ) {
        withClip(
            -width / 2f,
            height / 2f,
            -width / 2f + scaleWindowWidth,
            height / 2f - scaleWindowHeight
        ) {
            withTranslation(
                -width / 2f - scaleFactor * center.real + scaleWindowWidth / 2,
                +height / 2f - scaleFactor * center.image - scaleWindowHeight / 2,
            ) {
                withScale(scaleFactor, scaleFactor, 0f, 0f) {
                    // Draw content
                    block()
                }
            }
        }
        // Draw border
        drawRect(
            -width / 2f,
            height / 2f,
            -width / 2f + scaleWindowWidth,
            height / 2f - scaleWindowHeight,
            scaleWindowBorderPaint
        )
    }
}
