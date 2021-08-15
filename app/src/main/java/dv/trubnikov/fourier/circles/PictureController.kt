package dv.trubnikov.fourier.circles

import dv.trubnikov.fourier.circles.calculates.PictureCalculator.Picture
import dv.trubnikov.fourier.circles.calculates.Tick
import dv.trubnikov.fourier.circles.models.Complex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*
import kotlin.collections.ArrayList

class PictureController(
    private val scope: CoroutineScope,
    timeController: TimeController,
) {

    data class VectorPicture(
        val originalPath: List<Complex>,
        val drawingPath: List<Complex>,
        val vectors: List<Complex>
    )

    private val mutex = Mutex()
    private val picturePointsTrace = LinkedList<Picture.PicturePoint>()
    private var picture: Picture? = null
    private var originalPath: List<Complex> = emptyList()

    val pictureFlow = MutableSharedFlow<VectorPicture>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    init {
        timeController.setTimeListener(this::onTick)
    }

    suspend fun setPicture(userPicture: List<Complex>, fourierPicture: Picture) {
        mutex.withLock {
            if (originalPath == userPicture) {
                val newTrace = recalculateTrace(fourierPicture, picturePointsTrace)
                picturePointsTrace.clear()
                picturePointsTrace.addAll(newTrace)
                picture = fourierPicture
            } else {
                picturePointsTrace.clear()
                originalPath = userPicture
                picture = fourierPicture
            }
        }
    }

    private fun onTick(tickNumber: Long, tickSize: Long) {
        scope.launch {
            mutex.withLock {
                val picture = picture ?: return@launch
                val timeFactor = Tick(tickNumber.toFloat() / tickSize)
                val value = picture.valueAt(timeFactor)
                picturePointsTrace.add(value)
                if (picturePointsTrace.size > tickSize) {
                    picturePointsTrace.removeFirst()
                }
                val picturePoint = VectorPicture(
                    originalPath = originalPath,
                    drawingPath = picturePointsTrace.map { it.point },
                    vectors = value.vectors,
                )
                pictureFlow.emit(picturePoint)
            }
        }
    }

    private fun recalculateTrace(
        picture: Picture,
        oldTrace: List<Picture.PicturePoint>
    ): List<Picture.PicturePoint> {
        val newTrace = ArrayList<Picture.PicturePoint>(picturePointsTrace.size)
        for (oldPoint in oldTrace) {
            val newPoint = picture.valueAt(oldPoint.time)
            newTrace.add(newPoint)
        }
        return newTrace
    }
}
