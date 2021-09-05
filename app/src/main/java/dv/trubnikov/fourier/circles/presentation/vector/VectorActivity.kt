package dv.trubnikov.fourier.circles.presentation.vector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dv.trubnikov.fourier.circles.databinding.ActivityVectorBinding
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.getComplex
import dv.trubnikov.fourier.circles.models.putComplex
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VectorActivity : ComponentActivity() {

    companion object {
        private const val COMPLEX_KEY = "complex_data_key"

        fun getIntent(context: Context, data: List<Complex>): Intent {
            return Intent(context, VectorActivity::class.java).apply {
                putComplex(COMPLEX_KEY, data)
            }
        }
    }

    private lateinit var binding: ActivityVectorBinding
    private val viewModel by viewModels<VectorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            intent?.getComplex(COMPLEX_KEY)?.let { data ->
                viewModel.setUserPicture(data.toList())
            }
        }

        setupCollects()
        setupListeners()
        setupControlPanel()
    }

    private fun setupListeners() {
        binding.bottomSheet.vectorCountSeekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
                override fun onStartTrackingTouch(seekBar: SeekBar) = Unit
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) viewModel.onVectorCountChanged(progress)
                }
            }
        )
        binding.deleteButton.setOnClickListener {
//            val drawIntent = DrawActivity.getIntent(this)
//            startActivity(drawIntent)
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            finish()
            with(binding.vectorView) {
                if (isPaused) {
                    binding.vectorView.resume()
                } else {
                    binding.vectorView.pause()
                }
            }
        }
        binding.bottomSheet.root.setOnClickListener {
            val behaviour = BottomSheetBehavior.from(it)
            val desiredState = when (behaviour.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> BottomSheetBehavior.STATE_EXPANDED
                BottomSheetBehavior.STATE_EXPANDED -> BottomSheetBehavior.STATE_COLLAPSED
                else -> return@setOnClickListener
            }
            behaviour.state = desiredState
        }
    }

    private fun setupCollects() {
        lifecycleScope.launchWhenResumed {
            viewModel.pictureFlow.collect {
                binding.vectorView.setPicture(it)
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.vectorsNumberFlow.collect {
                binding.bottomSheet.vectorCountText.text = it.toString()
                binding.vectorView.setVectorCount(it)
            }
        }
        binding.bottomSheet.vectorCountSeekbar.progress = viewModel.vectorsNumberFlow.value
    }

    private fun setupControlPanel() {
        binding.root.post {
            val callback = SidebarsControl(binding)
            val behaviour = BottomSheetBehavior.from(binding.bottomSheet.root)
            behaviour.addBottomSheetCallback(callback)
            callback.onStateChanged(binding.bottomSheet.root, behaviour.state)
        }
    }

    private class SidebarsControl(
        private val binding: ActivityVectorBinding
    ) : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_COLLAPSED -> onSlide(bottomSheet, 0f)
                BottomSheetBehavior.STATE_EXPANDED -> onSlide(bottomSheet, 1f)
                else -> { /* no-op */ }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            binding.sidebarRecyclerLeft.apply {
                translationX = -width * (1f - slideOffset)
            }
            binding.sidebarRecyclerRight.apply {
                translationX = +width * (1f - slideOffset)
            }
            binding.deleteButton.apply {
                translationX = -width * slideOffset
                alpha = 1f - slideOffset
            }
            binding.vectorView.updatePadding(
                left = (binding.sidebarRecyclerLeft.width * slideOffset).toInt(),
                right = (binding.sidebarRecyclerRight.width * slideOffset).toInt(),
                bottom = (binding.root.height - binding.bottomSheet.root.top)
            )
        }
    }
}
