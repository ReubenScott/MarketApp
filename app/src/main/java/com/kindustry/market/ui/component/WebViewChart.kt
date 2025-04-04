package com.kindustry.market.ui.component

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewChart(symbol: String) {

    // https://www.nikkei.com/nkd/company/chart/?type=10year&scode=6619&ba=1
    // https://minkabu.jp/stock/6619/chart
    // https://kabutan.jp/stock/chart?code=6619
    // https://kabuyoho.jp/reportChart?bcode=6619
    // https://finance.yahoo.co.jp/quote/6619.T/chart
    // https://jp.tradingview.com/chart/?symbol=TSE:6619

    val url = "https://www.nikkei.com/nkd/company/chart/?type=10year&scode=${symbol}&ba=1"

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true // 启用 JavaScript (如果需要)
                webViewClient = WebViewClient() // 处理页面导航
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url) // 如果 URL 发生变化，重新加载
        },
        modifier = Modifier.fillMaxSize()
    )
}