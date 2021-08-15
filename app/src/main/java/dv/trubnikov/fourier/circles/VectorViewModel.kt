package dv.trubnikov.fourier.circles

import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dv.trubnikov.fourier.circles.PictureController.VectorPicture
import dv.trubnikov.fourier.circles.calculates.FourierCalculator
import dv.trubnikov.fourier.circles.calculates.PictureCalculator
import dv.trubnikov.fourier.circles.models.Complex
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.roundToInt

class VectorViewModel : ViewModel() {

    companion object {
        private const val DEFAULT_NUMBER_OF_VECTORS = 5
    }

    private var coefficientCount = DEFAULT_NUMBER_OF_VECTORS
    private var pictureCalculator: PictureCalculator? = null
    private var userPicture: List<Complex>? = null

    private val timeController = TimeController(viewModelScope)
    private val pictureController = PictureController(viewModelScope, timeController)

    val pictureFlow: SharedFlow<VectorPicture> = pictureController.pictureFlow
    val vectorNumberFlow = MutableStateFlow(5)

    suspend fun setUserPicture(userPath: List<Complex>) {
        if (userPath.isEmpty()) {
            userPicture = null
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val fourierCalculator = FourierCalculator { time ->
                val index = (userPath.lastIndex * time).roundToInt()
                userPath[index]
            }
            val pictureCalculator = PictureCalculator(fourierCalculator)
            val picture = pictureCalculator.calculatePicture(coefficientCount)
            timeController.restart()
            pictureController.setPicture(userPath, picture)


            this@VectorViewModel.pictureCalculator = pictureCalculator
            this@VectorViewModel.userPicture = userPath
        }
    }

    suspend fun changeNumberOfVectors(@IntRange(from = 1, to = 10_001) number: Int) {
        val vectorsNumber = number.coerceIn(1, 10_000)
        val pictureCalculator = pictureCalculator ?: return
        val userPicture = userPicture ?: return
        val fourierPicture = pictureCalculator.calculatePicture(vectorsNumber)
        pictureController.setPicture(userPicture, fourierPicture)
        vectorNumberFlow.value = vectorsNumber
    }
}
