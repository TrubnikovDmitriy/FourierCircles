package dv.trubnikov.fourier.circles.presentation.vector

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import dv.trubnikov.fourier.circles.models.Tick

@Suppress("Recycle")
class TimeController : ValueAnimator() {

    init {
        setFloatValues(Tick.MIN_VALUE, Tick.MAX_VALUE)
        interpolator = LinearInterpolator()
        repeatMode = RESTART
        repeatCount = INFINITE
        duration = 10_000
    }

    val currentTick: Tick
        get() = Tick(animatedValue as Float)

    fun addTickUpdateListener(listener: (Tick) -> Unit) {
        addUpdateListener { listener(currentTick) }
    }
}
