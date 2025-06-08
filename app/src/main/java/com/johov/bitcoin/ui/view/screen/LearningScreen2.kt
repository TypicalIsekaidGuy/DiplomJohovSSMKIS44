package com.johov.bitcoin.ui.view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.SideTextColor
import com.johov.bitcoin.ui.theme.Violet
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.ui.view.composables.MultiStyleText
import com.johov.bitcoin.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen2(learningText: MutableLiveData<String>, navigateTo:()->Unit){
    val text by learningText.observeAsState()

    Box(Modifier
        .paint(painterResource(id = R.drawable.learning_bg2),
            contentScale = ContentScale.FillBounds).fillMaxSize()){

        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)) {

            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                ProgressIndicator(2,Modifier.align(Alignment.CenterHorizontally))
                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(if(MainActivity.isBiggerThen700dp==true)0.17f else 0.1f))

                    Text(buildAnnotatedString {
                        text!!.split(" ").forEach { index ->
                            withStyle(style = SpanStyle(color = WhiteText, fontFamily = if (index=="friends"||index=="and") itimStyle else  krub_sb, fontSize = 26.sp)) {
                                append(index)
                                append(" ")
                            }
                        }
                    }, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.CenterHorizontally))

                    Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                    FourthText()
                    LastImage()
                }
            }

            Column (Modifier.align(Alignment.BottomCenter)){
                Box(
                    Modifier
                        .fillMaxWidth()){
                    NavigateToButton("NEXT", Modifier, { navigateTo() })
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.07f))
            }
        }
    }
}


@Composable
fun FourthText(){
    Column(verticalArrangement = Arrangement.spacedBy(MainActivity.maxHeight?.times(0.02f)?:20.dp)){
        LearnDecoration(Modifier.align(Alignment.CenterHorizontally), index = 4)

        MultiStyleText(modifier = Modifier,colors = listOf(
            WhiteText, Violet, WhiteText,
            SideTextColor, Violet), fontSize = 17.sp,textAlign = TextAlign.Center, "Each new ", "USER", " who enters your referral promo code will ","receive a reward.", "\n This section is still in development")

        Spacer(Modifier.fillMaxHeight(if(MainActivity.isBiggerThen700dp==true)0.05f else 0.02f))
    }
}

@Composable
fun LastImage(){
    Column(Modifier.fillMaxSize()){
        Image(painter = painterResource(id = R.drawable.learning_referal_pic), contentDescription = "", Modifier.weight(5f)/*.size(MainActivity.maxWidth?.times(0.6f)?:64.dp).fillMaxHeight(0.6f)*/.align(
            BiasAlignment.Horizontal(0.6f)))
        Box(Modifier.weight(2f))
    }
}


/*

@Composable
fun FourthText(modifier: Modifier = Modifier){

    Column(
        Modifier
            .padding(top = 32.dp)
            .fillMaxHeight(0.6f)
            .fillMaxWidth(), verticalArrangement = Arrangement.SpaceAround) {

        MultiStyleText(modifier = Modifier,colors = listOf(
            WhiteText, SideTextColor, WhiteText,
            WithdrawalTextColor
        ), fontSize = medium_text_size,textAlign = TextAlign.Center, "Press ", """"Claim"""", " and receive your reward ","within 4 hours",)

        Image(painter = painterResource(id = R.drawable.refferral_illustration), contentDescription = "", contentScale = ContentScale.FillWidth, modifier = Modifier.align(Alignment.CenterHorizontally).padding(start = MainActivity.maxWidth?.times(0.08f)?:24.dp, end  = MainActivity.maxWidth?.times(0.03f)?:24.dp))
    }

}*/
