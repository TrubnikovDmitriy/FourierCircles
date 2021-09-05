package dv.trubnikov.fourier.circles.tools

import android.graphics.Canvas
import androidx.core.graphics.withScale
import androidx.core.graphics.withTranslation

/**
 * Transforms android views' coordinate system into the usual mathematical one.
 */
fun Canvas.withMathCoordinates(width: Int, height: Int, block: Canvas.() -> Unit) {
    withTranslation(width / 2f, -height / 2f) {
        withScale(1f, -1f, width / 2f, height / 2f) {
            block()
        }
    }
}