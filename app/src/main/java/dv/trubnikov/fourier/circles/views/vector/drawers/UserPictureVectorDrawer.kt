package dv.trubnikov.fourier.circles.views.vector.drawers

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.views.vector.VectorDrawer

class UserPictureVectorDrawer(context: Context) : VectorDrawer {

    private val userPicture = Path()
    private val userPicturePaint = Paint().apply {
        color = context.getColor(R.color.vector_color)
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
    }

    override fun onPictureUpdate(picture: VectorPicture) {
        userPicture.reset()
        for (index in picture.originalPath.indices) {
            val point = picture.originalPath[index]
            if (index == 0) {
                userPicture.moveTo(point.real, point.image)
            } else {
                userPicture.lineTo(point.real, point.image)
            }
        }
        userPicture.close()
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        canvas.drawPath(userPicture, userPicturePaint)
    }
}