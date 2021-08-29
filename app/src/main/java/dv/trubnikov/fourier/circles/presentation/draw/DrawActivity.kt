package dv.trubnikov.fourier.circles.presentation.draw

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import dv.trubnikov.fourier.circles.R
import dv.trubnikov.fourier.circles.databinding.ActivityDrawBinding
import dv.trubnikov.fourier.circles.presentation.vector.VectorActivity

class DrawActivity : ComponentActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, DrawActivity::class.java)
        }
    }

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
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
    }
}
