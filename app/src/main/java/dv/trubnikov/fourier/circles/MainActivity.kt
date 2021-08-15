package dv.trubnikov.fourier.circles

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.views.DrawView
import dv.trubnikov.fourier.circles.views.VectorView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<VectorViewModel>()

    private val vectorCountText by lazy { findViewById<TextView>(R.id.vector_count_text) }
    private val vectorSeekBar by lazy { findViewById<SeekBar>(R.id.vector_count_seekbar) }
    private val vectorView by lazy { findViewById<VectorView>(R.id.vector_view) }
    private val drawView by lazy { findViewById<DrawView>(R.id.draw_view) }
    private val restart by lazy { findViewById<Button>(R.id.restart_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setUpButtons()
        setupFlows()
    }

    private fun calculateFourier(data: List<Complex>, duration: Long) {
        lifecycleScope.launch {
            viewModel.setUserPicture(data)
            changeView(drawing = false)
        }
    }

    private fun setUpButtons() {
        drawView.setOnDrawFinishListener { data, duration ->
            calculateFourier(data, duration)
        }
        restart.setOnClickListener {
            changeView(drawing = true)
        }
        vectorSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) return
                lifecycleScope.launchWhenStarted {
                    viewModel.changeNumberOfVectors(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
        })
    }

    private fun setupFlows() {
        // TODO cancel instead of suspend in out of RESUMED-PAUSED
        lifecycleScope.launchWhenResumed {
            viewModel.pictureFlow.collect {
                vectorView.drawVectorPicture(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.vectorNumberFlow.collect {
                vectorCountText.text = it.toString()
            }
        }
    }

    private fun changeView(drawing: Boolean) {
        vectorView.isVisible = !drawing
        restart.isVisible = !drawing
        drawView.isVisible = drawing
    }
}
