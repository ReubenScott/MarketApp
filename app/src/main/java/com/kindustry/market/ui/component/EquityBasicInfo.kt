package com.kindustry.market.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kindustry.market.db.entity.Equity
import com.kindustry.market.ui.screen.LocalPaddingValues

@Composable
fun EquityBasicInfo(
    equity: Equity?
) {
    Column(
        modifier = Modifier.padding(LocalPaddingValues.current)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "銘柄基本情報", style = MaterialTheme.typography.h5)
        InfoRow("社名", equity?.name ?: "")
        InfoRow("業種", equity?.sector ?: "")
        InfoRow("代表者", equity?.representative ?: "")
        InfoRow("資本金", equity?.capitalStock ?: "")
        InfoRow("本社住所", equity?.address ?: "")
        InfoRow("電話番号", equity?.tel ?: "")
        InfoRow("上場市場", equity?.exchange ?: "")
        InfoRow("上場年月日", equity?.listingDate ?: "")
        InfoRow("単元株数", equity?.perUnit ?: "")
        InfoRow("事業内容", equity?.businessScope ?: "")
        InfoRow("取扱い商品", equity?.productRange ?: "")
        InfoRowLink("URL", equity?.url ?: "")
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label：", style = MaterialTheme.typography.body1)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}

@Composable
fun InfoRowLink(label: String, value: String) {
    val uriHandler = LocalUriHandler.current

    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label：", style = MaterialTheme.typography.body1)
        Text(text = value,
            style = MaterialTheme.typography.body1,
            color = Color.Blue,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                uriHandler.openUri(value)
            }
        )
    }
}
