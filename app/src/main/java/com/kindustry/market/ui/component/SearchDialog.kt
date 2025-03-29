package com.kindustry.market.ui.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SearchDialog(
    onSubmit: (String) -> Unit
){
    var codeOrName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onSubmit("")  },
        text = {
            OutlinedTextField(
                value = codeOrName,
                onValueChange = { codeOrName = it },
                label = { Text("銘柄コード or 銘柄名") }
            )
        },
        confirmButton = {
            Button(onClick = { onSubmit(codeOrName)  }) {
                Text(text = "検索")
            }
        }
    )
}




