package dv.trubnikov.fourier.circles.calculates.searchers

import dv.trubnikov.fourier.circles.models.PictureFrame
import dv.trubnikov.fourier.circles.models.Tick

class PictureFrameSearcher(pictureFrames: List<PictureFrame>) : ValueSearcher<Tick, PictureFrame> {

    init {
        if (pictureFrames.isEmpty()) {
            error("Picture points must not be empty")
        }
    }

    private val pictureSearcher = BinarySearcher.tickSearcher(pictureFrames)

    override fun valueFor(time: Tick): PictureFrame {
        return pictureSearcher.valueFor(time)
    }
}
