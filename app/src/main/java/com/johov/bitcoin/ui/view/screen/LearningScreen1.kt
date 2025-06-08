package com.johov.bitcoin.ui.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.Black
import com.johov.bitcoin.ui.theme.LearningBright
import com.johov.bitcoin.ui.theme.LearningShade
import com.johov.bitcoin.ui.theme.SideTextColor
import com.johov.bitcoin.ui.theme.Violet
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.average_text_size
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_i
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.ui.theme.medium_text_size
import com.johov.bitcoin.ui.view.composables.DashedLine
import com.johov.bitcoin.ui.view.composables.MultiStyleText
import com.johov.bitcoin.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen1(navigateTo:()->Unit) {
    val appName = stringResource(id = R.string.app_name)

    Box(
        Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.learning_bg1),
                contentScale = ContentScale.FillBounds
            )){

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            ProgressIndicator(1,Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Welcome to the $appName app",
                    color = Color(0xFF5845C6),
                    fontFamily = krub_sb,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                Text(
                    text = "This app simulates earning \n" +
                            "crypto in form of bitcoin\n" +
                            "!! No real bitcoin mining is happening !!",
                    color = Black,
                    fontFamily = krub_i,
                    fontSize = average_text_size,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                )

            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            FirstText()

            SecondText()

            ThirdText()


/*            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Column(
                    Modifier.fillMaxWidth(0.717f), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.Start) {

                    MultiStyleText(modifier = Modifier,colors = listOf(MainTextColor, SideTextColor, MainTextColor,
                        ButtonTextColor), fontSize = average_text_size,textAlign = TextAlign.End, "Press ", """"Claim"""", " and receive your reward ","within 4 hours")
                    *//*   MultiStyleText(modifier = Modifier.align(Alignment.End),colors = listOf(SideTextColor), fontSize = average_text_size,textAlign = TextAlign.End, "within 4 hours")*//*
                }
                Spacer(modifier = Modifier.width(16.dp))
                Image(painter = painterResource(id = R.drawable.learnin_1_icon), contentDescription = "",modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            DashedLine()
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
                Image(painter = painterResource(id = R.drawable.learnin_2_icon), contentDescription = "",modifier = Modifier.size(56.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 32.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.Start) {

                    MultiStyleText(modifier = Modifier,colors = listOf(MainTextColor, SideTextColor, MainTextColor), fontSize = average_text_size,textAlign = TextAlign.Start, "Use ", """"Upgrade""""," to get much more rewards")
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            Image(painterResource(id = R.drawable.present_illustration),"", modifier = Modifier
                .align(
                    Alignment.CenterHorizontally
                )
                .fillMaxHeight(0.5f), contentScale = FillHeight)
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))*/
        }
        Column (Modifier.align(Alignment.BottomCenter)){
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)){
                NavigateToButton("NEXT", Modifier.align(Alignment.Center), { navigateTo() })
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.07f))
        }
    }
}

@Composable
fun ProgressIndicator(step: Int, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Step(
            stepNumber = "Page $step of 3",
            isActive = true,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )


        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StepIndicator(isActive = true, modifier = Modifier.weight(1f))

            StepIndicator(isActive = step > 1, modifier = Modifier.weight(1f))

            StepIndicator(isActive = step > 2, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Step(stepNumber: String, isActive: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = stepNumber,
        color = Color(0xFF9F82F9),
        modifier = modifier,
        textAlign = TextAlign.Start,
        fontFamily = itimStyle
    )
}

@Composable
fun StepIndicator(isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(MainActivity.maxWidth?.times(0.03f) ?: 32.dp)
            .background(
                color = if (isActive) LearningBright else LearningShade,
                CircleShape
            )
    )
}

@Composable
fun FirstText(){
    Column(verticalArrangement = Arrangement.spacedBy(MainActivity.maxHeight?.times(0.02f)?:20.dp)){
        LearnDecoration(Modifier.align(Alignment.CenterHorizontally), index = 1)

        MultiStyleText(modifier = Modifier,colors = listOf(
            WhiteText, SideTextColor, WhiteText,
            Violet), fontSize = 20.sp,textAlign = TextAlign.Center, "Click ", """"Start"""", " and receive your reward ","within 4 hours",)

        DashedLine()
    }
}

@Composable
fun SecondText(){
    Spacer(modifier = Modifier.fillMaxHeight(0.07f))
    Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.05f)?:20.dp)){
        LearnDecoration(Modifier.align(Alignment.CenterVertically), index = 2)

        MultiStyleText(modifier = Modifier,colors = listOf(
            WhiteText, Violet, WhiteText), fontSize = 20.sp,textAlign = TextAlign.Center, "Use ", """"Upgrade""""," to get much more rewards")

    }
    Spacer(modifier = Modifier.fillMaxHeight(0.08f))
    DashedLine()
}


@Composable
fun ThirdText(){
    Spacer(modifier = Modifier.fillMaxHeight(0.05f))
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier=Modifier.fillMaxWidth()){


        MultiStyleText(modifier = Modifier.fillMaxWidth(0.8F),colors = listOf(
            WhiteText, SideTextColor, WhiteText), fontSize = 17.sp,textAlign = TextAlign.Center, "Don't forget to click on ", """"Claim""""," after the timer runs out")

        LearnDecoration(Modifier.align(Alignment.CenterVertically), index = 3)


    }
}

val cornerRadius = 40f + ((MainActivity.maxHeight?.minus(640.dp)?.value?.div(2f)) ?: 0f)


@Composable
fun LearnDecoration(modifier: Modifier = Modifier,index: Int){

    Box(
        modifier
            .size(MainActivity.maxWidth?.times(0.15f) ?: 32.dp)
            .paint(painterResource(id = R.drawable.learn_decoration_bg))){

        Text(
            text = index.toString(),
            color = WhiteText,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = medium_text_size,
            fontFamily = itimStyle
        )
    }

}