package com.example.testapp.WebView

import android.content.ClipData
import android.content.ComponentName
import android.content.Intent
import android.content.Intent.EXTRA_ALLOW_MULTIPLE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.webkit.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testapp.R
import java.io.ByteArrayOutputStream


public class WebViewActivity : AppCompatActivity()  {
    private var cam_file_data : String? = null
    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val webView = findViewById<WebView>(R.id.webView)
        val fileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it?.let {activityResult ->
                if (activityResult.resultCode==-1){
                    val intent = activityResult.data!!
                    var results : Array<Uri?>?
                    var clipData: ClipData?
                    var stringData: String?
                    try {
                        clipData = intent.getClipData()
                        stringData = intent.getDataString()
                    } catch (e: Exception) {
                        clipData = null
                        stringData = null
                    }
                    if (clipData == null && stringData == null && cam_file_data != null) {
                        results = arrayOf(Uri.parse(cam_file_data))
                    } else {
                        if (clipData != null) {
                            val numSelectedFiles = clipData.itemCount
                            results = arrayOfNulls(numSelectedFiles)
                            for (i in 0 until clipData.itemCount) {
                                results[i] = clipData.getItemAt(i).uri
                            }
                        } else {
                            try {
                                val cam_photo =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        intent.getParcelableExtra("data", Bitmap::class.java)
                                    } else {
                                        intent.extras?.get("data") as Bitmap
                                    }

                                val bytes = ByteArrayOutputStream()
                                cam_photo?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                stringData = MediaStore.Images.Media.insertImage(
                                    contentResolver,
                                    cam_photo,
                                    null,
                                    null
                                )
                            } catch (ignored: Exception) { }
                            results = arrayOf(Uri.parse(stringData))
                        }
                    }

                    filePathCallback?.onReceiveValue(results as Array<Uri>?)

                }else{
                    filePathCallback?.onReceiveValue(null)
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (filePathCallback != null && fileChooserParams != null) {
                    if (ContextCompat.checkSelfPermission(
                            this@WebViewActivity,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            this@WebViewActivity,
                            android.Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        this@WebViewActivity.filePathCallback = filePathCallback
                        val intent = fileChooserParams.createIntent()
                        intent.putExtra(EXTRA_ALLOW_MULTIPLE, true)
                        val intentList = ArrayList<Intent>()
                        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        val packageManager = packageManager
                        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
                        for (res in listCam) {
                            val packageName = res.activityInfo.packageName
                            val intent = Intent(captureIntent)
                            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                            intent.setPackage(packageName)
                            intentList.add(intent)
                        }
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        intentList.add(galleryIntent)
                        val chooserIntent = Intent.createChooser(intentList.removeAt(intentList.size - 1), "Select source")
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray())
                        fileLauncher.launch(chooserIntent)
                        return true
                    } else {
                        ActivityCompat.requestPermissions(
                            this@WebViewActivity, arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA
                            ), 100
                        )
                    }
                }
                return false
            }
        }


        webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        })


        webView.apply {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://tinypng.com/")
        }
    }
    }


