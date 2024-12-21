package com.kindustry.market

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

@Composable
fun SimpleColumn(){
    Column() {
        repeat( 100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }

    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        repeat( 100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }

    }
}

@Composable
fun LazyList() {
    val scrollState = rememberLazyListState()
    LazyColumn(state = scrollState) {
        items( 100){
            Text(text = "Item #$it", style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
fun ScrollingList(){
    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutlineScope =  rememberCoroutineScope()

    Column {
        Row() {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutlineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                }
            ) {
                Text(text = "Scroll to the top")
            }

            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    coroutlineScope.launch {
                        scrollState.animateScrollToItem(listSize -1 )
                    }
                }
            ) {
                Text(text = "Scroll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items( 50){
                ImageListItem(index = it)
            }
        }
    }

}

@Composable
fun ImageListItem(index: Int){
    Row (verticalAlignment = Alignment.CenterVertically){
        Image(
            painter = rememberImagePainter(
                data = "https://upload.wikimedia.org/wikipedia/commons/e/e6/Android_vector.jpg"
            ),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}