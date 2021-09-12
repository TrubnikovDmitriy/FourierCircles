package dv.trubnikov.fourier.circles.views.vector

import android.graphics.Canvas
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture

interface VectorDrawer {

    fun onAnimationRepeat() = Unit

    fun onDraw(canvas: Canvas, vectors: List<FourierVector>)

    fun onPictureUpdate(picture: VectorPicture) = Unit

    fun onSizeChanged(width: Int, height: Int) = Unit

    fun onVectorCountChanged(vectorCount: Int) = Unit
}