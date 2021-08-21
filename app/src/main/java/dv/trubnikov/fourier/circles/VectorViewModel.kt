package dv.trubnikov.fourier.circles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dv.trubnikov.fourier.circles.PictureController.VectorPicture
import dv.trubnikov.fourier.circles.calculates.FourierCalculator
import dv.trubnikov.fourier.circles.calculates.PictureCalculator
import dv.trubnikov.fourier.circles.models.Complex
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlin.math.log
import kotlin.math.pow
import kotlin.math.roundToInt

class VectorViewModel : ViewModel() {

    companion object {
        private const val DEFAULT_NUMBER_OF_VECTORS = 5
        private const val USER_INPUT_SAMPLING_MS = 50L
    }

    private var pictureCalculator: PictureCalculator? = null
    private var userPicture: List<Complex>? = null

    private val timeController = TimeController(viewModelScope)
    private val pictureController = PictureController(viewModelScope, timeController)
    private val userInputVector = MutableSharedFlow<Int>(
        replay = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val pictureFlow: SharedFlow<VectorPicture> = pictureController.pictureFlow
    val vectorsNumberFlow = MutableStateFlow(DEFAULT_NUMBER_OF_VECTORS)

    init {
        viewModelScope.launch(Dispatchers.Default) {
          userInputVector.sample(USER_INPUT_SAMPLING_MS).collect {
                changeNumberOfVectors(it)
            }
        }
    }

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
            val picture = pictureCalculator.calculatePicture(vectorsNumberFlow.value)
            timeController.restart()
            pictureController.setPicture(userPath, picture)


            this@VectorViewModel.pictureCalculator = pictureCalculator
            this@VectorViewModel.userPicture = userPath
        }
    }

    fun onVectorCountChanged(number: Int) {
        userInputVector.tryEmit(number)
    }

    private suspend fun changeNumberOfVectors(number: Int) {
        val userPicture = userPicture ?: return
        val pictureCalculator = pictureCalculator ?: return
        withContext(Dispatchers.Default) {
            // Converting the linear scale of seekbar to logarithmic
            // b = k * lg(a)
            // a = 10^(b / k)
            // k = b_max / lg(a_max)
            val logarithmBase = 10.0
            val maxCoefNumber = 100
            val maxSeekbarNumber = 100
            val k = maxSeekbarNumber / log(maxCoefNumber.toDouble(), logarithmBase)
            val coefNumber = logarithmBase.pow(number / k).toInt()
            val vectorsNumber = coefNumber.coerceIn(1, maxCoefNumber) * 2 + 1

            if (vectorsNumber == vectorsNumberFlow.value) {
                return@withContext
            }

            val fourierPicture = pictureCalculator.calculatePicture(vectorsNumber)
            pictureController.setPicture(userPicture, fourierPicture)
            vectorsNumberFlow.value = vectorsNumber
        }
    }
}
