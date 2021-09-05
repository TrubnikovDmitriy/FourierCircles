package dv.trubnikov.fourier.circles.models

class FourierVector(
    val tick: Tick,
    val src: Complex,
    val dst: Complex,
    val angle: Float,
    val coefficient: FourierCoefficient,
) {
    val length = coefficient.amplitude
}
