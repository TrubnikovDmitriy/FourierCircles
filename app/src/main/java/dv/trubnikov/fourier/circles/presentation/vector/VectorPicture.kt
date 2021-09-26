package dv.trubnikov.fourier.circles.presentation.vector

import dv.trubnikov.fourier.circles.calculates.searchers.BinarySearcher
import dv.trubnikov.fourier.circles.calculates.searchers.ValueSearcher
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.PictureFrame
import dv.trubnikov.fourier.circles.models.Tick

class VectorPicture(
    val originalPath: List<Complex>,
    pictureFrames: List<PictureFrame>
) : ValueSearcher<Tick, PictureFrame> {

    init {
        if (originalPath.isEmpty()) {
            error("Original path must not be empty")
        }
        if (pictureFrames.isEmpty()) {
            error("Picture frames must not be empty")
        }
        val size = pictureFrames.first().vectors.size
        pictureFrames.forEach {
            assert(it.vectors.size == size) {
                "Picture frames must be the same size (tick [${it.value}] has [${it.vectors.size} != $size])"
            }
        }
    }

    private val pictureSearcher = BinarySearcher.tickSearcher(pictureFrames)

    val size = pictureFrames.first().vectors.size

    override fun valueFor(time: Tick): PictureFrame {
        return pictureSearcher.valueFor(time)
    }
}
