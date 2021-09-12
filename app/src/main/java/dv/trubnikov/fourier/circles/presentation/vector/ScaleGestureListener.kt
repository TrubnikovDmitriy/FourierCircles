package dv.trubnikov.fourier.circles.presentation.vector

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class ScaleGestureListener(
    context: Context,
    private val onScaleListener: (Float) -> Unit,
) : View.OnTouchListener, ScaleGestureDetector.SimpleOnScaleGestureListener() {

    companion object {
        private const val MAX_SCALE_FACTOR = 3_000f
        private const val DEFAULT_SCALE_FACTOR = 2f
        private const val MIN_SCALE_FACTOR = 0.2f
    }

    private var scaleRatio = DEFAULT_SCALE_FACTOR
    private val scaleGestureDetector = ScaleGestureDetector(context, this)

    init {
        onScaleListener(scaleRatio)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()
        return scaleGestureDetector.onTouchEvent(event)
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scale = (scaleRatio * detector.scaleFactor).adjustScale()
        onScaleListener(scale.adjustScale())
        return false
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        scaleRatio = (scaleRatio * detector.scaleFactor).adjustScale()
    }

    private fun Float.adjustScale(): Float = coerceIn(MIN_SCALE_FACTOR, MAX_SCALE_FACTOR)
}
