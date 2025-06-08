package com.johov.bitcoin.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.OffTextColor
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_i
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.ui.theme.medium_text_size
import com.johov.bitcoin.ui.view.composables.MultiStyleText
import com.johov.bitcoin.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen3(navigateTo:()->Unit) {

    Box(
        Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.learning_bg3),
                contentScale = ContentScale.FillBounds
            )) {
        Column(Modifier.fillMaxSize()) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.02f))
        ProgressIndicator(3,Modifier.align(Alignment.CenterHorizontally))
        Column(
            Modifier
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Text(
                    text = "!! There are no real withdraws !!",
                    color = Color(0xFF5745C6),
                    fontFamily = krub_i,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )
            }

            BottomColumn()
        }
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            }
    }

    Column(Modifier.align(Alignment.BottomCenter)) {
        Box(
            Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()){
            NavigateToButton("GO TO START", Modifier.align(Alignment.Center), { navigateTo() })
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.07f))
    }
}
}

@Composable
fun FourthText(modifier: Modifier){
    MultiStyleText(modifier = Modifier,colors = listOf(
        WhiteText, OffTextColor,
    ), fontSize = medium_text_size,textAlign = TextAlign.Center, "Withdrawal  ","available")
}

@Composable
fun BottomColumn(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.weight(1f))
        LearnDecoration(index = 5)
        Spacer(modifier = Modifier.weight(0.5f))
        Row(modifier = Modifier,horizontalArrangement = Arrangement.spacedBy(4.sp.value.dp)){

            Text(
                text = "Withdraw",
                color = WhiteText,
                fontFamily = krub_sb,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Text(
                text = "Bitcoin to your wallet",
                color = WhiteText,
                fontFamily = itimStyle,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
/*
        Spacer(modifier = Modifier.weight(1f))
*/

        Image(painter = painterResource(id = R.drawable.learning_withdrawal_pic),"", Modifier.weight(4f), contentScale = ContentScale.FillHeight)

/*
        Spacer(modifier = Modifier.weight(1f))
*/

        Text(
            text = "It's time to get started",
            color = WhiteText,
            fontFamily = krub_sb,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(3f))

    }
}