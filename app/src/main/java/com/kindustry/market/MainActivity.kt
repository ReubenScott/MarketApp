package com.kindustry.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kindustry.market.ui.theme.MarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketTheme {

                val result by mainViewModel.readAll.collectAsState(initial = emptyList())

                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    if (result.isNotEmpty()){
                        for (company in result){
                            Row() {
                                Text(
                                    text = company.symbol,
                                    fontSize = MaterialTheme.typography.h4.fontSize
                                )
                                Text(
                                    text = company.name ?: "公司名称未知", // 当 name 为 null 时，显示默认值
                                    fontSize = MaterialTheme.typography.h4.fontSize
                                )
                            }

                        }

                    } else{
                        Text(
                            text = "Empty Database",
                            fontSize = MaterialTheme.typography.h4.fontSize
                        )

                    }


                }


                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    MessageCard(Message(author = "Android", body = "Jectpack"))
//                    Conversation(SampleData.conversationSample)
//                }

//                SimpleColumn()
//                SimpleList()
//                LazyList()
//                ScrollingList()
//                PhotographerCard()
//                LayoutStudy()



            }

        }
    }

}

@Composable
fun MessageCard(msg: Message) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.snap1),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember {
            mutableStateOf(false )
        }
        val surfaceColor: Color by animateColorAsState(
            if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )
        Column (
            modifier = Modifier.clickable {isExpanded = !isExpanded}
        ){
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.author, color = MaterialTheme.colors.secondaryVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = surfaceColor,
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.body2,
                    maxLines = if(isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }
}

@Composable
fun Conversation(messages : List<Message>){
    LazyColumn(){
        items(messages){
            message -> MessageCard(message)
        }
    }
}

//    @Composable
//    fun Conversation2(messages : LiveData<Message>){
//        var msgs by messages.observeAsState()
//        LazyColumn(){
//            items(messages){
//                    message -> MessageCard(message)
//            }
//        }
//    }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MarketTheme {
        MessageCard(Message(author = "Android", body = "Jectpack"))
    }
}


data class Message(val author:String , val body: String)
