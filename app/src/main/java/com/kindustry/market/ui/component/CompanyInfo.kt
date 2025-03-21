package com.kindustry.market.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kindustry.market.db.entity.Stock
import com.kindustry.market.ui.screen.LocalPaddingValues

@Composable
fun CompanyInfo(
    stock: Stock?
) {
    Column(
        modifier = Modifier.padding(LocalPaddingValues.current)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "銘柄基本情報", style = MaterialTheme.typography.h5)
        InfoRow("社名", stock?.name ?: "")
        InfoRow("業種", stock?.sector ?: "")
        InfoRow("代表者", stock?.representative ?: "")
        InfoRow("資本金", stock?.capitalStock ?: "")
        InfoRow("本社住所", stock?.address ?: "")
        InfoRow("電話番号", stock?.tel ?: "")
        InfoRow("上場市場", stock?.exchange ?: "")
        InfoRow("上場年月日", stock?.ListingDate ?: "")
        InfoRow("単元株数", stock?.perUnit ?: "")
        InfoRow("事業内容", stock?.businessScope ?: "")
        InfoRow("取扱い商品", stock?.productRange ?: "")
        InfoRow("URL", stock?.url ?: "")
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "$label：", style = MaterialTheme.typography.body1)
        Text(text = value, style = MaterialTheme.typography.body1)
    }
}
