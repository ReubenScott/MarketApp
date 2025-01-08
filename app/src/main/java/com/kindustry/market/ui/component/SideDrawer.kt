package com.kindustry.market.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun SideDrawer(
//    onDismissRequest: () -> Unit,
//    onSubmit: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var query by remember { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val box = createRef()
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f) // 宽度为屏幕的比例
                .background(Color.White.copy(alpha = 0.8f))
                .constrainAs(box) {
                    start.linkTo(parent.start) // 将 Box 的左侧与父布局的左侧对齐
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
//                    .width(300.dp)
//                    .height(200.dp)
//                    .background(Color.White.copy(alpha = 0.8f))
//                    .align(Alignment.CenterStart) // 对齐到左侧
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("请输入查询内容") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Button(onClick = { /* onDismissRequest() */}) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = { /*  onSubmit(query) */}) {
                        Text("提交")
                    }
                }
            }
        }
    }
}

/*

//@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownExample() {
    var expanded by remember { mutableStateOf(false) } // 控制下拉菜单的显示/隐藏
    var selectedItem by remember { mutableStateOf("Option 1") } // 记录选中的选项

    Box(Modifier.wrapContentSize(Alignment.TopStart)) {
        ExposedDropdownMenu {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item.name
                    expanded = false
                }) {
                    Text(text = item.name)
                }
            }
        }
    }
}
* */