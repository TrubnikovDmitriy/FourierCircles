package dv.trubnikov.fourier.circles.views.rotate

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture

class RotateRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RecyclerView(context, attrs, defStyleAttr) {

    var adapter: RotateAdapter? = null
        private set

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setVectors(picture: VectorPicture, listener: RotateClickListener) {
        adapter = RotateAdapter(picture, listener)
        setAdapter(adapter)
    }
}
