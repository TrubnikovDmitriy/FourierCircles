package dv.trubnikov.fourier.circles.presentation.vector.sidebars

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import dv.trubnikov.fourier.circles.presentation.vector.IconType
import dv.trubnikov.fourier.circles.presentation.vector.IconType.*
import dv.trubnikov.fourier.circles.views.icons.*

class LeftSidebar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val icons = mapOf(
        TRACE to TraceIcon(context),
        ARROW to ArrowIcon(context),
        RADIUS to RadiusIcon(context),
        SCALE_WINDOW to ScaleIcon(context),
        USER_PICTURE to UserPictureIcon(context),
    )

    init {
        orientation = VERTICAL
        icons.toSortedMap(compareBy { it.position }).forEach {
            addView(it.value)
        }
    }

    fun setOnIconClickListener(listener: (type: IconType, isActive: Boolean) -> Unit) {
        for ((type, icon) in icons) {
            icon.setOnClickListener { listener(type, icon.isActive) }
        }
    }
}
