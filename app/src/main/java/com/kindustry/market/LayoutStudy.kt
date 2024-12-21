package com.kindustry.market

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LayoutStudy(){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                  Text(text = "LayoutStudy")
                }
                , actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ){
        innerPadding ->  BodyContent(Modifier.padding(innerPadding))
    }
    
}


@Composable
fun BodyContent(modifier: Modifier = Modifier){
    Column (modifier = modifier.padding(8.dp)){
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the LayoutStudy")
    }
}





