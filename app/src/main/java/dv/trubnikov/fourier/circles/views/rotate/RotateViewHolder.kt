package dv.trubnikov.fourier.circles.views.rotate

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.presentation.vector.di.VectorComponent

class RotateViewHolder(
    private val rotateView: RotateView,
    private val vectorPicture: VectorPicture,
    private val clickListener: RotateClickListener,
) : ViewHolder(rotateView) {

    private var isActive = true
    private var vectorPosition: Int = 0

    init {
        val tickAnimator = VectorComponent.instance.timeController
        tickAnimator.addTickUpdateListener { tick ->
            val pictureFrame = vectorPicture.valueFor(tick)
            val vector = pictureFrame.vectors[vectorPosition]
            rotateView.setVector(vector.angle, vector.length)
        }
        rotateView.setOnClickListener {
            clickListener.onVectorClick(vectorPosition, isActive)
        }
    }

    fun setVectorNumber(position: Int, isActive: Boolean) {
        this.isActive = isActive
        this.vectorPosition = position
        rotateView.setActive(isActive)
    }
}
