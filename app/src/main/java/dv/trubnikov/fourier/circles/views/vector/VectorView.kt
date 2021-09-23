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
import dv.trubnikov.fourier.circles.presentation.vector.IconType
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.tools.withMathCoordinates
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
    private val drawers: VectorDrawers = VectorDrawers.Builder()
        .register(AxisDrawer(context))
        .register(TraceDrawer())
        .register(UserPictureDrawer(context))
        .register(RadiusDrawer())
        .register(ArrowDrawer())
        .build()

    private val animator = ValueAnimator.ofFloat(Tick.MIN_VALUE, Tick.MAX_VALUE).apply {
        interpolator = LinearInterpolator()
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 10_000
        addUpdateListener { invalidate() }
        doOnRepeat { drawers.onAnimationRepeat() }
    }

    private val scaleWindow = VectorScaleWindow(context)

    private var picture: VectorPicture? = null
    private var vectorCount: Int = 0

    fun resume() {
        animator.resume()
    }

    fun pause() {
        animator.pause()
    }

    fun toggleDrawer(type: IconType, isActive: Boolean) {
        val drawerClass = when (type) {
            IconType.TRACE -> TraceDrawer::class
            IconType.USER_PICTURE -> UserPictureDrawer::class
            IconType.RADIUS -> RadiusDrawer::class
            IconType.ARROW -> ArrowDrawer::class
            IconType.SCALE_WINDOW -> {
                scaleWindow.toggleVisibility(isActive)
                postInvalidate()
                return
            }
        }
        drawers.toggleDrawer(drawerClass, isActive)
        postInvalidate()
    }

    fun setPicture(picture: VectorPicture) {
        post {
            this.picture = picture
            drawers.onPictureUpdate(picture)
            animator.start()
        }
    }

    fun setVectorCount(vectorCount: Int) {
        this.vectorCount = vectorCount
        drawers.onVectorCountChanged(vectorCount)
        postInvalidate()
    }

    fun setScaleFactor(scaleFactor: Float) {
        scaleWindow.setScale(scaleFactor)
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val picture = picture ?: return
        val tick = Tick(animator.animatedValue as Float)
        val pictureFrame = picture.valueFor(tick)
        val vectors = pictureFrame.vectors.subList(0, vectorCount)

        val widthScale = (width - paddingLeft - paddingRight).toFloat() / width
        val heightScale = (height - paddingTop - paddingBottom).toFloat() / height
        val scale = min(widthScale, heightScale)
        canvas.withScale(x = scale, y = scale, pivotX = width / 2f, pivotY = 0f) {
            canvas.withMathCoordinates(width, height) {
                drawers.onDraw(canvas, vectors)
                scaleWindow.withScaleWindow(canvas, vectors.last().dst) {
                    drawers.onDraw(canvas, vectors)
                }
            }
        }
    }
}
