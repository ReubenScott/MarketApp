package com.kindustry.market.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideDrawer(
//    onDismissRequest: () -> Unit,
//    onSubmit: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var symbol by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

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
                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it },
                    label = { Text("コード") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
//                    readOnly = true,
                    value = name, // selectedOptionText
                    onValueChange = { name = it },
                    label = { Text("銘柄名") },
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(
//                            expanded = expanded
//                        )
//                    },
//                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
//                    modifier = Modifier.menuAnchor()
                )

                BasicSlider()

                DropdownMenuExample()


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

@Composable
fun DropdownMenuExample() {
    // 市場区分
    var index0 by remember { mutableStateOf(-1) }
    val exchanges = listOf("東証プライム", "東証スタンダード", "東証グロース")

    // 業種
    var index1 by remember { mutableStateOf(-1) }
    val items = listOf("Item 1", "Item 2", "Item 3")

    Column {
        DropdownMenuWithLabel(
            label = "市場区分 ： ",
            items = exchanges,
            selectedIndex = index0,
            onSelectionChanged = { index0 = it }
        )
        DropdownMenuWithLabel(
            label = "業種 ： ",
            items = items,
            selectedIndex = index1,
            onSelectionChanged = { index1 = it }
        )
    }
}

@Composable
fun BasicSlider() {
    var sliderPosition by remember { mutableStateOf(0f) }
    Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it }
    )
    Text("当前值：${sliderPosition}") // 显示当前值
}


@Composable
fun DropdownMenuWithLabel(
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(
            onClick = { expanded = !expanded }
        ) {
            Text(text = label)
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = if (selectedIndex >= 0) items[selectedIndex] else "")
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        onSelectionChanged(index)
                        expanded = false
                    }
                ) {
                    Text(text = item)
                }
            }
        }
    }
}



data class DropdownMenuItemData(val name: String, val description: String)

@Composable
fun DropdownMenuWithDescription() {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        DropdownMenuItemData("Item 1", "This is the first item"),
        DropdownMenuItemData("Item 2", "This is the second item with a longer description"),
        DropdownMenuItemData("Item 3", "Item 3")
    )
    var selectedIndex by remember { mutableStateOf(0) }

    Box {
        Button(
            onClick = { expanded = !expanded }
        ) {
            Text(text = items[selectedIndex].name)
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    }
                ) {
                    Column {
                        Text(text = item.name)
                        Text(text = item.description, style = MaterialTheme.typography.caption) // 调整描述的样式
                    }
                }
            }
        }
    }
}