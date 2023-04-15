package com.example.testapp


import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.testapp.Game.SaveTheButterfly
import com.example.testapp.ViewModel.MainViewModel
import com.example.testapp.WebView.WebViewActivity
import android.os.Bundle as Bundle1


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle1?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.fetchRemoteConfig({ gamePass, webLink ->
            if (gamePass) {
                openGame()
            } else {
                openWebView(webLink)
            }
        }, {
            openWebView("https://www.google.com")
        })
    }

    private fun openGame() {
        val intent = Intent(this, SaveTheButterfly::class.java)
        startActivity(intent)
    }

    private fun openWebView(webLink: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Close app?")
            .setMessage("Are you sure you want to close the app?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
    }
}














