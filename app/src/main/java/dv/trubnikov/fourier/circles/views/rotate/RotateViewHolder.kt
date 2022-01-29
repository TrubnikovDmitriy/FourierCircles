package dv.trubnikov.fourier.circles.views.rotate

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dv.trubnikov.fourier.circles.models.Tick
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.presentation.vector.di.VectorComponent

class RotateViewHolder(
    private val rotateView: RotateView,
    private val vectorPicture: VectorPicture,
    private val clickListener: RotateClickListener,
) : ViewHolder(rotateView) {

    private val timeController = VectorComponent.instance.timeController
    private var vectorIndex: Int = 0

    init {
        timeController.addTickUpdateListener { tick ->
            updateVector(tick, vectorIndex)
        }
    }

    fun bindVectorIndex(index: Int, isActive: Boolean) {
        vectorIndex = index
        updateVector(timeController.currentTick, index)
        rotateView.setActive(isActive)
        rotateView.setOnClickListener {
            clickListener.onVectorClick(index, isActive)
        }
    }

    private fun updateVector(tick: Tick, index: Int) {
        val pictureFrame = vectorPicture.valueFor(tick)
        val vector = pictureFrame.vectors[index]
        rotateView.setVector(vector.angle, vector.length)
    }
}
