package dv.trubnikov.fourier.circles.calculates.searchers

fun interface ValueSearcher<T, R> {
    fun valueFor(time: T): R
}