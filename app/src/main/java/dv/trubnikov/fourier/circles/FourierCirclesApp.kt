package dv.trubnikov.fourier.circles

import android.app.Application
import android.content.Context
import dv.trubnikov.fourier.circles.di.AppComponent

class FourierCirclesApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppComponent.build(this)
    }
}
