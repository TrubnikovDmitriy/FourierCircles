package dv.trubnikov.fourier.circles.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

abstract class SquareView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    final override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val size = min(width, height)
        setMeasuredDimension(size, size)
    }
}
