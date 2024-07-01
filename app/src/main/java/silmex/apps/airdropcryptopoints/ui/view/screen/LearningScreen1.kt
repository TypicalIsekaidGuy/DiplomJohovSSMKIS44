package silmex.apps.airdropcryptopoints.ui.view.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.AltBorder
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.LearningBright
import silmex.apps.airdropcryptopoints.ui.theme.LearningShade
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.OffTextColor
import silmex.apps.airdropcryptopoints.ui.theme.ShadeBackground
import silmex.apps.airdropcryptopoints.ui.theme.SideTextColor
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.theme.medium_text_size
import silmex.apps.airdropcryptopoints.ui.view.composables.BalanceBar
import silmex.apps.airdropcryptopoints.ui.view.composables.DashedLine
import silmex.apps.airdropcryptopoints.ui.view.composables.MultiStyleText
import silmex.apps.airdropcryptopoints.ui.view.composables.NavigateToButton

@Composable
fun LearningScreen1(navigateTo:()->Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 64.dp), verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        ProgressIndicator(1)
        Column(
            Modifier
                .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Welcome to the Airdrop Crypto Points app",
                color = MainTextColor,
                fontFamily = itimStyle,
                fontSize = big_text_size,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Now we will tell you how to get rewards in a few clicks:",
                color = MainTextColor,
                fontFamily = itimStyle,
                fontSize = average_text_size,
                textAlign = TextAlign.Start
            )

        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Column(
                Modifier.fillMaxWidth(0.717f), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.Start) {

                MultiStyleText(modifier = Modifier,colors = listOf(MainTextColor, ButtonTextColor, MainTextColor,SideTextColor), fontSize = average_text_size,textAlign = TextAlign.End, "Press ", """"Claim"""", " and receive your reward ","within 4 hours")
             /*   MultiStyleText(modifier = Modifier.align(Alignment.End),colors = listOf(SideTextColor), fontSize = average_text_size,textAlign = TextAlign.End, "within 4 hours")*/
            }
            Spacer(modifier = Modifier.width(16.dp))
            LearnDecoration(1)
        }
        DashedLine()
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround){
            LearnDecoration(2, 74)
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.Start) {

                MultiStyleText(modifier = Modifier,colors = listOf(MainTextColor, SideTextColor, MainTextColor), fontSize = average_text_size,textAlign = TextAlign.Start, "Use ", """"Upgrade""""," to get much more rewards")
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(id = R.drawable.present_illustration),"")
            NavigateToButton("NEXT",Modifier.align(Alignment.CenterHorizontally),{navigateTo()})
        }
    }
}

@Composable
fun ProgressIndicator(step: Int) {
    Column(Modifier, horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Step(
            stepNumber = "Step $step of 3",
            isActive = true,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 20.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StepIndicator(isActive = true, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            StepIndicator(isActive = step > 1, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            StepIndicator(isActive = step > 2, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Step(stepNumber: String, isActive: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = stepNumber,
        color = OffTextColor,
        modifier = modifier,
        textAlign = TextAlign.Start,
    )
}

@Composable
fun StepIndicator(isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(8.dp)
            .background(
                color = if (isActive) LearningBright else LearningShade,
                RoundedCornerShape(16.dp)
            )
    )
}
@Composable
fun LearnDecoration(index: Int, width: Int? = null){

    Box(if(width==null)Modifier.fillMaxWidth() else Modifier.width(width.dp)) {

        Box(
            modifier = Modifier
                .padding(start = 6.dp, top = 6.dp)
                .border(1.dp, AltBorder, RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(64.dp)
                .fillMaxWidth()
                .background(
                    ShadeBackground, RoundedCornerShape(16.dp)
                )
        )
        Box(
            modifier = Modifier
                .padding(end = 6.dp)
                .fillMaxWidth()
                .border(1.dp, AltBG, RoundedCornerShape(16.dp))
                .height(64.dp)
                .background(
                    ButtonTextColor, RoundedCornerShape(16.dp)
                )
        ) {
            Text(index.toString(), fontFamily = itimStyle, fontSize = medium_text_size,color = WhiteText, modifier = Modifier.align(
                Alignment.Center))
        }
    }
}