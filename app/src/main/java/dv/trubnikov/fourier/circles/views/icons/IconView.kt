package dv.trubnikov.fourier.circles.views.icons

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View.OnClickListener
import androidx.annotation.CallSuper
import androidx.core.view.setPadding
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.views.SquareView

abstract class IconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : SquareView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private val disableColorMatrix = ColorMatrixColorFilter(
            floatArrayOf(
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0.3f, 0.59f, 0.11f, 0f, 0f,
                0f, 0f, 0f, 0.75f, 0f,
            )
        )
    }

    init {
        val padding = context.resources.getDimensionPixelSize(R.dimen.icon_view_padding)
        setPadding(padding)
    }

    var isActive = true
        private set

    final override fun setOnClickListener(userListener: OnClickListener?) {
        val listener = OnClickListener {
            isActive = !isActive
            invalidate()
            onDisableIcon {
                colorFilter = disableColorMatrix.takeIf { !isActive }
            }
            userListener?.onClick(it)
        }
        super.setOnClickListener(listener)
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        val widthScale = (width - paddingLeft - paddingRight).toFloat() / width
        val heightScale = (height - paddingTop - paddingBottom).toFloat() / height
        canvas.scale(widthScale, heightScale, width / 2f, height / 2f)
    }

    protected abstract fun onDisableIcon(tunePaint: Paint.() -> Unit)
}
