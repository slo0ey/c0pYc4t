package com.acceler8tion.c0pyc4t.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.acceler8tion.c0pyc4t.R
import com.acceler8tion.c0pyc4t.data.Utils
import com.acceler8tion.c0pyc4t.data.viewmodel.APIViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var mainLayout: RelativeLayout
    private lateinit var appTitle: TextView
    private lateinit var idInput: EditText
    private lateinit var c0py: Button
    private lateinit var notice: TextView
    private lateinit var mlg: ImageButton
    private lateinit var appVersion: TextView
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private lateinit var pref: SharedPreferences
    private lateinit var viewModel: APIViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        viewModel = ViewModelProvider(this)
                        .get(APIViewModel::class.java)
        job = SupervisorJob()
        pref = PreferenceManager.getDefaultSharedPreferences(this)

        launch {
            Glide.with(this@MainActivity)
                .load(R.drawable.mlg)
                .asGif()
                .error(R.drawable.ic_mlg_load_fail)
                .into(mlg)
        }

        c0py.setOnClickListener(View.OnClickListener {
            launch {
                doCopy()
            }
        })

        mlg.setOnClickListener(View.OnClickListener {
            launch {
                doMlg()
            }
        })

        val text = "If you want to relogin, here"
        val string = SpannableString(text)
        val index = text.indexOf("here")
        string.setSpan(CustomClickableSpan(), index, index+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        notice.text = string
        notice.movementMethod = LinkMovementMethod.getInstance()

        val text2 = getString(R.string.app_version)
        val string2 = SpannableString(text2)
        string2.setSpan(URLSpan("https://www.github.com/acceler8tion/c0pYc4t"), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        appVersion.text = string2
        appVersion.movementMethod = LinkMovementMethod.getInstance()
    }

    private suspend fun doCopy() {
        val id = idInput.text.toString()
        if (id.isEmpty() || id.toIntOrNull() == null) {
            showToast("$id must be number!", Toast.LENGTH_SHORT)
        } else {
            showToast("Downloading $id...", Toast.LENGTH_LONG)
            viewModel.getLevel(id).collect {
                if (it == null) {
                    showToast("Failed to download level", Toast.LENGTH_SHORT)
                } else {
                    showToast("Analyzing param...", Toast.LENGTH_LONG)
                    val level = Utils.parseToMap(it, ":")
                    val gjp = Base64.encodeToString(Utils.xor(pref.getString("gjp", "")!!, "37526").toByteArray(), Base64.DEFAULT)
                    viewModel.uploadLevel(
                            id,
                            pref.getString("accountID", "")!!,
                            gjp,
                            pref.getString("userName", "")!!,
                            level
                    ).collect {it2 -> run {
                            if(it2 == null) {
                                showToast("Failed to upload level", Toast.LENGTH_SHORT)
                            } else {
                                showToast("Upload successfully! Check your level list", Toast.LENGTH_LONG)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun doMlg() {
        repeat(100) {
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
            delay(50L)
        }
        mainLayout.setBackgroundColor(Color.WHITE)
    }

    private fun initView() {
        mainLayout = findViewById(R.id.app_layout)
        appTitle = findViewById(R.id.appTitle)
        idInput = findViewById(R.id.idInput)
        c0py = findViewById(R.id.c0py)
        notice = findViewById(R.id.notice)
        mlg = findViewById(R.id.mlg)
        appVersion = findViewById(R.id.app_version)
    }

    private fun showToast(text: String, length: Int) {
        Toast.makeText(this, text, length).show()
    }

    inner class CustomClickableSpan : ClickableSpan() {
        /**
         * Performs the click action associated with this span.
         */
        override fun onClick(widget: View) {
            pref.edit()
                    .putBoolean("login", false)
                    .apply()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}