package dv.trubnikov.fourier.circles.views.vector

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.withScale
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Tick
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import dv.trubnikov.fourier.circles.views.vector.drawers.*
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
    private val drawers: List<VectorDrawer> = listOf(
        AxisVectorDrawer(context),
        TraceVectorDrawer(),
        UserPictureVectorDrawer(context),
        RadiusVectorDrawer(),
        ArrowVectorDrawer(),
    )

    private val animator = ValueAnimator.ofFloat(Tick.MIN_VALUE, Tick.MAX_VALUE).apply {
        interpolator = LinearInterpolator()
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 10_000
        addUpdateListener { invalidate() }
        doOnRepeat { drawers.forEach { it.onAnimationRepeat() } }
    }

    private var picture: VectorPicture? = null
    private var vectorCount: Int = 0

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
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            drawers.forEach { it.onSizeChanged(width = width, height = height) }
        }
    }
}