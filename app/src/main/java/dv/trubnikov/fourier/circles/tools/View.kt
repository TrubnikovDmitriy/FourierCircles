package dv.trubnikov.fourier.circles.tools

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.updateMargins

fun View.updateMargins(
    @Px left: Int? = null,
    @Px top: Int? = null,
    @Px right: Int? = null,
    @Px bottom: Int? = null,
) {
    val marginParams = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    marginParams.updateMargins(
        top = top ?: marginParams.topMargin,
        left = left ?: marginParams.leftMargin,
        right = right ?: marginParams.rightMargin,
        bottom = bottom ?: marginParams.bottomMargin,
    )
}