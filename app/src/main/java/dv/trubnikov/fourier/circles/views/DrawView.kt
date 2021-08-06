package dv.trubnikov.fourier.circles.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.models.Complex

class DrawView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : AxisView(context, attrs, defStyleAttr, defStyleRes) {

    fun interface OnDrawFinishListener {
        fun onDrawFinish(points: List<Complex>, duration: Long)
    }

    companion object {
        private val VECTOR_TRACE_PAINT = Paint().apply {
            color = Color.RED
            isAntiAlias = true
            strokeWidth = 5f
            style = Paint.Style.STROKE
        }
    }

    init {
        setBackgroundColor(context.getColor(R.color.vector_background_color))
    }

    private val trace = Path()
    private val points = ArrayList<Complex>()
    private var startDrawingTime: Long? = null
    private var listener: OnDrawFinishListener? = null

    fun setOnDrawFinishListener(listener: OnDrawFinishListener) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startDrawing()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                draw(event)
                return true
            }
            MotionEvent.ACTION_UP -> {
                finishDrawing()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(trace, VECTOR_TRACE_PAINT)
    }

    private fun startDrawing() {
        startDrawingTime = System.currentTimeMillis()
    }

    private fun draw(event: MotionEvent) {
        if (trace.isEmpty) {
            trace.moveTo(event.x, event.y)
        } else {
            trace.lineTo(event.x, event.y)
        }
        points.add(Complex(event.x - width / 2, height / 2 - event.y))
        invalidate()
    }

    private fun finishDrawing() {
        val finishTime = System.currentTimeMillis()
        val startTime = startDrawingTime ?: return
        val listener = listener ?: return
        listener.onDrawFinish(ArrayList(points), finishTime - startTime)
        startDrawingTime = null
        points.clear()
        trace.reset()
    }
}
