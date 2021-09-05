package dv.trubnikov.fourier.circles.calculates.searchers

import dv.trubnikov.fourier.circles.models.Tick

class BinarySearcher<T, R : Comparable<R>>(
    data: Iterable<R>,
    private val diffExtractor: (R, T) -> Int
) : ValueSearcher<T, R> {

    companion object {
        fun <R : Tick> tickSearcher(data: Iterable<R>): BinarySearcher<Tick, R> {
            return BinarySearcher(data) { value: R, tick: Tick -> value.compareTo(tick) }
        }
    }

    private val sortedArray = ArrayList(data.sorted())

    override fun valueFor(time: T): R {
        val index = sortedArray.binarySearch { data: R ->
            val diff = diffExtractor(data, time)
            when {
                diff < 0f -> -1
                diff > 0f -> +1
                else -> 0
            }
        }
        if (index >= 0) {
            return sortedArray[index]
        } else {
            val correctIndex = (-index - 1).coerceAtMost(sortedArray.lastIndex)
            return sortedArray[correctIndex]
        }
    }
}
