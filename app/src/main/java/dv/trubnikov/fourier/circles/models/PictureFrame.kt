package dv.trubnikov.fourier.circles.models

data class PictureFrame(
    val tick: Tick,
    val vectors: List<FourierVector>
) : Tick by tick