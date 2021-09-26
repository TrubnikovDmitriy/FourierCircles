package dv.trubnikov.fourier.circles.presentation.vector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.databinding.ActivityVectorBinding
import dv.trubnikov.fourier.circles.models.Complex
import dv.trubnikov.fourier.circles.models.getComplex
import dv.trubnikov.fourier.circles.models.putComplex
import dv.trubnikov.fourier.circles.presentation.draw.DrawActivity
import dv.trubnikov.fourier.circles.presentation.vector.di.VectorComponent
import dv.trubnikov.fourier.circles.tools.updateMargins
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
    private val timeController by lazy { VectorComponent.instance.timeController }

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

    @Suppress("ClickableViewAccessibility")
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
        binding.controls.deleteButton.setOnClickListener {
            val behaviour = BottomSheetBehavior.from(binding.bottomSheet.root)
            behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
            val drawIntent = DrawActivity.getIntent(this)
            startActivity(drawIntent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        binding.controls.playButton.setOnClickListener {
            timeController.resume()
            binding.controls.playButton.isVisible = false
            binding.controls.pauseButton.isVisible = true
        }
        binding.controls.pauseButton.setOnClickListener {
            timeController.pause()
            binding.controls.playButton.isVisible = true
            binding.controls.pauseButton.isVisible = false
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
        binding.sidebarRecyclerLeft.setOnIconClickListener { icon, isActive ->
            binding.vectorView.toggleDrawer(icon, isActive)
        }
        binding.sidebarRecyclerRight.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            lifecycleScope.launchWhenCreated {
                viewModel.pictureFlow.collect { picture ->
                    setVectors(picture) { position, isActive ->
                        adapter?.apply { setVectorState(position, !isActive) }
                    }
                    adapter?.setVectorCount(viewModel.vectorsNumberFlow.value)
                }
            }
        }
        val scaleListener = ScaleGestureListener(this) {
            binding.vectorView.setScaleFactor(it)
        }
        binding.vectorView.setOnTouchListener(scaleListener)
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
                binding.sidebarRecyclerRight.adapter?.setVectorCount(it)
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
            binding.controls.root.apply {
                translationX = -binding.sidebarRecyclerRight.width * slideOffset
            }
            val bottomSheetHeight = binding.root.height - binding.bottomSheet.root.top
            binding.vectorView.updatePadding(
                left = (binding.sidebarRecyclerLeft.width * slideOffset).toInt(),
                right = (binding.sidebarRecyclerRight.width * slideOffset).toInt(),
                bottom = bottomSheetHeight
            )
            binding.sidebarRecyclerRight.updateMargins(
                bottom = bottomSheetHeight
            )
            binding.sidebarRecyclerLeft.updateMargins(
                bottom = bottomSheetHeight
            )
        }
    }
}
