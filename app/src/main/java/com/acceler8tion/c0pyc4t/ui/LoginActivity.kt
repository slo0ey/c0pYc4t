package com.acceler8tion.c0pyc4t.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.acceler8tion.c0pyc4t.R
import com.acceler8tion.c0pyc4t.data.Utils
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext


class LoginActivity() : AppCompatActivity(), CoroutineScope {

    private lateinit var accountid: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var notice: TextView
    private lateinit var appVersion: TextView
    private lateinit var pref: SharedPreferences
    private lateinit var job: Job
    private lateinit var moveToMainIntent: Intent

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()

        job = SupervisorJob()
        moveToMainIntent = Intent(this, MainActivity::class.java)
        pref = PreferenceManager.getDefaultSharedPreferences(this)

        if(!pref.contains("udid")) pref.edit()
                .putString("udid", UUID.randomUUID().toString())
                .apply()
        if(pref.getBoolean("login", false)) {
            startActivity(moveToMainIntent)
            finish()
        }

        login.setOnClickListener(View.OnClickListener {
            launch {
                doLogin()
            }
        })

        val text = getString(R.string.notice)
        val string = SpannableString(text)
        val index = text.indexOf("here")
        string.setSpan(URLSpan("https://gdbrowser.com/"), index, index+4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        notice.text = string
        notice.movementMethod = LinkMovementMethod.getInstance()

        val text2 = getString(R.string.app_version)
        val string2 = SpannableString(text2)
        string2.setSpan(URLSpan("https://www.github.com/acceler8tion/c0pYc4t"), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        appVersion.text = string2
        appVersion.movementMethod = LinkMovementMethod.getInstance()
    }

    private suspend fun doLogin() {
        val ai = accountid.text.toString()
        val un = username.text.toString()
        val pw = password.text.toString()
        if(ai.isEmpty() || un.isEmpty() || pw.isEmpty()) {
            showToast("Empty AccountID, Username or Password!", Toast.LENGTH_SHORT)
        } else {
            showToast("Wait a second...", Toast.LENGTH_LONG)
            pref.edit()
                    .putBoolean("login", true)
                    .putString("accountID", ai)
                    .putString("userName", un)
                    .putString("gjp", pw)
                    .apply()
            delay(2000L)
            startActivity(moveToMainIntent)
            finish()
        }
    }

    private fun initView() {
        accountid = findViewById(R.id.accountid)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        notice = findViewById(R.id.notice)
        appVersion = findViewById(R.id.app_version)
    }

    private fun showToast(text: String, length: Int) {
        Toast.makeText(this, text, length).show()
    }
}