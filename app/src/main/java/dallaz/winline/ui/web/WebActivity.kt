package dallaz.winline.ui.web

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebActivity : AppCompatActivity() {
    private val webView by lazy { WebView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView.webViewClient = WebViewClient()
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            useWideViewPort = true
            allowContentAccess = true
            loadWithOverviewMode = true
            allowFileAccess = true
            databaseEnabled = true
            useWideViewPort = true
            setSupportZoom(false)
        }


        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        setContentView(webView)

        if (savedInstanceState != null) webView.restoreState(savedInstanceState)
        else webView.loadUrl(intent.getStringExtra(URL) ?: "")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        if (webView.canGoBack()) webView.goBack()
    }

    companion object {
        private const val URL = "url"

        fun startWebActivity(context: Context, url: String) {
            context.startActivity(Intent(context, WebActivity::class.java).apply {
                putExtra(URL, url)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        }
    }
}