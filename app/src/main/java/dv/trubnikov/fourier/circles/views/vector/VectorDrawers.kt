package dv.trubnikov.fourier.circles.views.vector

import android.graphics.Canvas
import dv.trubnikov.fourier.circles.models.FourierVector
import dv.trubnikov.fourier.circles.presentation.vector.VectorPicture
import dv.trubnikov.fourier.circles.views.drawers.CanvasDrawer
import kotlin.reflect.KClass

class VectorDrawers(
    private val drawerMap: Map<KClass<out CanvasDrawer>, CanvasDrawer>
) : CanvasDrawer {

    private val enabledMap = mutableMapOf(*drawerMap.keys.map { it to true }.toTypedArray())

    class Builder {
        private val map = mutableMapOf<KClass<out CanvasDrawer>, CanvasDrawer>()

        fun register(drawer: CanvasDrawer): Builder {
            val klass = drawer::class
            if (map.containsKey(klass)) {
                throw IllegalArgumentException("$klass уже зарегистрирован")
            }
            map[klass] = drawer
            return this
        }

        fun build(): VectorDrawers = VectorDrawers(map)
    }

    override fun onDraw(canvas: Canvas, vectors: List<FourierVector>) {
        for ((key, drawer) in drawerMap) {
            if (enabledMap[key] == true) {
                drawer.onDraw(canvas, vectors)
            }
        }
    }

    override fun onAnimationRepeat() {
        drawerMap.onEach { it.value.onAnimationRepeat() }
    }

    override fun onPictureUpdate(picture: VectorPicture) {
        drawerMap.onEach { it.value.onPictureUpdate(picture) }
    }

    override fun onVectorCountChanged(vectorCount: Int) {
        drawerMap.onEach { it.value.onVectorCountChanged(vectorCount) }
    }

    inline fun <reified D : CanvasDrawer> getDrawer(): D? {
        return getDrawer(D::class)
    }

    fun <D : CanvasDrawer> toggleDrawer(klass: KClass<D>, isEnabled: Boolean) {
        enabledMap[klass] = isEnabled
    }

    fun <D : CanvasDrawer> getDrawer(klass: KClass<D>): D? {
        @Suppress("UNCHECKED_CAST")
        return drawerMap[klass] as D?
    }
}