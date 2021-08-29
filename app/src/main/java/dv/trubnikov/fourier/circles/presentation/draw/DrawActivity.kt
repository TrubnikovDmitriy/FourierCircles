package dv.trubnikov.fourier.circles.presentation.draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import dv.trubnikov.fourier.circles.databinding.ActivityDrawBinding
import dv.trubnikov.fourier.circles.presentation.vector.VectorActivity

class DrawActivity : ComponentActivity() {

    private lateinit var viewBinding: ActivityDrawBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityDrawBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setUpButtons()
    }

    private fun setUpButtons() {
        viewBinding.root.setOnDrawFinishListener { data, _ ->
            val intent = VectorActivity.getIntent(this, data)
            startActivity(intent)
            finish()
        }
    }
}
