package dv.trubnikov.fourier.circles.presentation.vector.di

import dagger.Component
import dv.trubnikov.fourier.circles.di.AppComponent
import dv.trubnikov.fourier.circles.presentation.vector.TimeController

@VectorScope
@Component(
    modules = [VectorModule::class],
    dependencies = [AppComponent::class]
)
interface VectorComponent {

    companion object {
        private var internalInstance: VectorComponent? = null

        val instance: VectorComponent get() = requireNotNull(internalInstance) {
            "Method build() must be called in VectorViewModel first"
        }

        fun build() {
            assert(internalInstance == null) { "VectorComponent is already build" }
            internalInstance = DaggerVectorComponent.factory().create(AppComponent.component)
        }

        fun release() {
            internalInstance = null
        }
    }

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): VectorComponent
    }

    @VectorScope
    val timeController: TimeController
}