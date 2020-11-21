package com.acceler8tion.c0pyc4t.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.acceler8tion.c0pyc4t.R
import com.acceler8tion.c0pyc4t.data.Utils
import com.acceler8tion.c0pyc4t.data.viewmodel.ViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val repository = ViewModel()
    private lateinit var mainLayout: RelativeLayout
    private lateinit var appTitle: TextView
    private lateinit var idInput: EditText
    private lateinit var c0py: Button
    private lateinit var mlg: ImageButton
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    companion object {
        @JvmStatic
        private var isDownload = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        job = SupervisorJob()

        Glide.with(this)
            .load(R.drawable.mlg)
            .asGif()
            .error(R.drawable.ic_mlg_load_fail)
            .into(mlg)

        c0py.setOnClickListener(View.OnClickListener {
            /*val id = idInput.text.toString()
            if (id.isEmpty() || id.toIntOrNull() == null) {
                Toast.makeText(this, "$id must be number!", Toast.LENGTH_SHORT).show()
            } else {
                isDownload = true;
                launch {

                }
            }*/
            val string = idInput.text.toString()
            idInput.setText(Utils.chk(Utils.seed(string)))
        })

        mlg.setOnClickListener(View.OnClickListener {
            launch(Dispatchers.Main) {
                repeat(30) {
                    val r2 = String.format(
                        "%02X",
                        (Math.random() * 255).toInt()
                    )
                    val g2 = String.format(
                        "%02X",
                        (Math.random() * 255).toInt()
                    )
                    val b2 = String.format(
                        "%02X",
                        (Math.random() * 255).toInt()
                    )
                    mainLayout.setBackgroundColor(Color.parseColor("#$r2$g2$b2"))
                }
            }
        })
    }

    private fun initView() {
        mainLayout = findViewById(R.id.app_layout)
        appTitle = findViewById(R.id.appTitle)
        idInput = findViewById(R.id.idInput)
        c0py = findViewById(R.id.c0py)
        mlg = findViewById(R.id.mlg)
    }
}