package dv.trubnikov.fourier.circles.presentation.vector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dv.trubnikov.fourier.circles.calculates.FourierCalculator
import dv.trubnikov.fourier.circles.calculates.PictureCalculator
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.presentation.vector.di.VectorComponent
import dv.trubnikov.fourier.circles.tools.StateSharedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.roundToInt

class VectorViewModel : ViewModel() {

    companion object {
        private const val MAX_NUMBER_OF_VECTORS = 50
        private const val DEFAULT_NUMBER_OF_VECTORS = 5
        private const val USER_INPUT_SAMPLING_MS = 50L
    }

    init {
        VectorComponent.build()
    }

    private val userInputVector = MutableSharedFlow<Int>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val pictureFlow = StateSharedFlow<VectorPicture>()
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
            // TODO logger
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val fourierCalculator = FourierCalculator { time ->
                val index = (userPath.lastIndex * time).roundToInt()
                userPath[index]
            }
            val pictureCalculator = PictureCalculator(fourierCalculator)
            val pictureFrames = pictureCalculator.calculatePicture(MAX_NUMBER_OF_VECTORS)
            val vectorPicture = VectorPicture(userPath, pictureFrames)
            pictureFlow.emit(vectorPicture)
        }
    }

    fun onVectorCountChanged(number: Int) {
        userInputVector.tryEmit(number)
    }

    override fun onCleared() {
        VectorComponent.release()
    }

    private suspend fun changeNumberOfVectors(number: Int) {
        withContext(Dispatchers.Default) {
            val vectorsCount = (number * MAX_NUMBER_OF_VECTORS / 100f).roundToInt()
            vectorsNumberFlow.value = max(1, vectorsCount)
        }
    }
}
