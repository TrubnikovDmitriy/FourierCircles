package dv.trubnikov.fourier.circles.views.rotate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture

class RotateAdapter(
    private val vectorPicture: VectorPicture,
    private val clickListener: RotateClickListener,
) : RecyclerView.Adapter<RotateViewHolder>() {

    private val vectorEnables = BooleanArray(vectorPicture.size) { true }
    private var count = 0

    fun setVectorState(position: Int, isActive: Boolean) {
        vectorEnables[position] = isActive
        notifyItemChanged(position)
    }

    fun setVectorCount(newCount: Int) {
        val oldCount = count
        count = newCount
        when {
            oldCount < newCount -> notifyItemRangeInserted(oldCount, newCount - oldCount)
            newCount < oldCount -> notifyItemRangeRemoved(newCount, oldCount - newCount)
            newCount == oldCount -> return
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RotateViewHolder {
        val rotateView = LayoutInflater.from(parent.context).inflate(R.layout.vh_rotate_vector, parent, false)
        return RotateViewHolder(rotateView as RotateView, vectorPicture, clickListener)
    }

    override fun onBindViewHolder(holder: RotateViewHolder, position: Int) {
        holder.setVectorNumber(position, vectorEnables[position])
    }

    override fun getItemCount(): Int {
        return count
    }
}
