package com.zbyte.nftsdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView

class ZBytePlatform : WebView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        loadZByte()
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun loadZByte() {
        val view = inflate(context, R.layout.zbyte_layout, this)
        val progress = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorAnim = view.findViewById<LottieAnimationView>(R.id.errorAnim)

        this.apply {
            loadUrl(WEB_URL)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.userAgentString = APP_NAME
            webViewClient = Client(context, progress, errorAnim)
        }
    }

    private inner class Client(
        private val context: Context,
        private val progress: ProgressBar,
        private val errorAnim: LottieAnimationView
    ) : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            progress.visibility = View.GONE
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url != null) {
                if (request.url.toString().contains("intent://")) {
                    val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            if (error?.errorCode == ERROR_HOST_LOOKUP || error?.errorCode == ERROR_UNKNOWN) {
                errorAnim.visibility = View.VISIBLE
                progress.visibility = View.GONE
                view?.loadUrl("about:blank")
            } else {
                errorAnim.visibility = View.GONE
            }

            super.onReceivedError(view, request, error)
        }
    }
}