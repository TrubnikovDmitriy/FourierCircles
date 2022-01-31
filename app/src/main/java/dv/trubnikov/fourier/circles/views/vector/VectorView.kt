package dv.trubnikov.fourier.circles.views.vector

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.withScale
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.presentation.vector.IconType
import dv.trubnikov.fourier.circles.models.Picture
import dv.trubnikov.fourier.circles.presentation.vector.di.VectorComponent
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

    private val animator by lazy {
        VectorComponent.instance.timeController.apply {
            addUpdateListener { invalidate() }
            doOnRepeat { drawers.onAnimationRepeat() }
        }
    }

    private val scaleWindow = VectorScaleWindow(context)

    private var picture: Picture? = null
    private var vectorCount: Int = 0

    fun setVectorColor(vectorIndex: Int, @ColorInt color: Int) {
        drawers.getDrawer<ArrowDrawer>()?.setVectorColor(vectorIndex, color)
        postInvalidate()
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

    fun setPicture(picture: Picture) {
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
        val picture = picture ?: run {
            drawers.getDrawer<AxisDrawer>()?.onDraw(canvas, emptyList())
            return
        }
        val pictureFrame = picture.valueFor(animator.currentTick)
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
