package dv.trubnikov.fourier.circles.presentation.vector.di

import dagger.Module
import dagger.Provides
import dv.trubnikov.fourier.circles.presentation.vector.TimeController

@Module
object VectorModule {

    @Provides
    @VectorScope
    fun timeController(): TimeController = TimeController()
}
