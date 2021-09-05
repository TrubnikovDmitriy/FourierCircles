package dv.trubnikov.fourier.circles.views.vector

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnRepeat
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Tick
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
import dv.trubnikov.fourier.circles.views.AxisView
import dv.trubnikov.fourier.circles.views.vector.drawers.ArrowVectorDrawer
import dv.trubnikov.fourier.circles.views.vector.drawers.RadiusVectorDrawer
import dv.trubnikov.fourier.circles.views.vector.drawers.TraceVectorDrawer
import dv.trubnikov.fourier.circles.views.vector.drawers.UserPictureVectorDrawer

class VectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : AxisView(context, attrs, defStyleAttr, defStyleRes) {

    init {
        setBackgroundColor(context.getColor(R.color.vector_background_color))
    }

    /**
     * Drawers are arranged in the order in which they
     * will be rendered, so the order is important.
     */
    private val drawers: List<VectorDrawer> = listOf(
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

    val isPaused: Boolean
        get() = animator.isPaused

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

        canvas.withMathCoordinates(width, height) {
            drawers.forEach { vectorDrawer ->
                vectorDrawer.onDraw(canvas, vectors)
            }
        }
    }
}