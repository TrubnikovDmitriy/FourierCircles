package dv.trubnikov.fourier.circles.di

import android.app.Application
import android.content.Context
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    companion object {
        private var internalComponent: AppComponent? = null

        val component: AppComponent get() = requireNotNull(internalComponent) {
            "Method build() must be called first"
        }

        fun build(application: Application) {
            internalComponent = DaggerAppComponent.factory().create(AppModule(application))
        }
    }

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }

    @ApplicationContext
    fun context(): Context
}