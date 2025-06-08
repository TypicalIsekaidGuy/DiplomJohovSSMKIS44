package com.johov.bitcoin.ui.view.composables

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.AltBG
import com.johov.bitcoin.ui.theme.ColorOFBorder
import com.johov.bitcoin.ui.theme.ColorOFButton
import com.johov.bitcoin.ui.theme.ColorUnderButton
import com.johov.bitcoin.ui.theme.DarkOrange
import com.johov.bitcoin.ui.theme.ShadeBackground
import com.johov.bitcoin.ui.theme.Violet
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.big_text_size_f
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.utils.StringUtils.getBalanceText
import java.util.Locale

@Composable
fun ButtonBackground(
    multipliyer: Int,
    balance: Float,
    progress: Float,
    leftTime: Long,
    isFarming: Boolean = false,
     isPressable: Boolean = false,
    isAnimatable: Boolean? = null,
     onPress: (() -> Unit)? = null,
     content: (@Composable (mod: Modifier) -> Unit)?=null,
     text: String = ""){
    if(isFarming){
        ActiveBackground(multipliyer,balance,progress,leftTime)
    }
    else{
        InActiveBackground(
            isPressable = isPressable,
            onPress,
            content,
            if (balance == 0f) "Start" else "Claim ${getBalanceText(balance)} BTC",
            isAnimatable,
            isOrange = true
        )
    }
}
@Composable
fun InActiveBackground(
    isPressable: Boolean = false,
    onPress: (() -> Unit)? = null,
    content: (@Composable (mod: Modifier) -> Unit)?=null,
    text: String = "",
    isAnimatable: Boolean? = null,
    modifier: Modifier = Modifier,
    isOrange:Boolean = false,
    shouldBeActive: Boolean = true) {


    var paddingValues by remember { mutableStateOf(0f) }
    var paddingFontValues by remember { mutableStateOf(4f) }

    val animatedPaddingValuesHor by animateFloatAsState(
        targetValue = paddingValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    val animatedFontPaddingValues by animateFloatAsState(
        targetValue = paddingFontValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    if(isAnimatable == true){
        LaunchedEffect(true){
            while(true){
                paddingValues = 10f//6f
                paddingFontValues = if(text.length>10) 10f else 4f
                delay(600)
                paddingValues = 0f
                paddingFontValues =if(text.length>10) 6f else 0f
                delay(600)
            }
        }
    }
    val modifier = if(isPressable)
        modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) {
                MainScope().launch {
                    paddingValues = 0f//12f
                    paddingFontValues = 0f
                    delay(300)
                    paddingValues = 20f
                    paddingFontValues = 4f
                    if (onPress != null) {
                        onPress()
                    }
                    else{

                        throw IllegalArgumentException("onPress should not be null when isPressable is true")
                    }
                }
            }
    else modifier

    val maxHeight = 0.075f//was 0.085f

    Box(
        Modifier
            .fillMaxWidth()
            .height(
                MainActivity.maxHeight
                    ?.times(maxHeight)
                    ?.plus(3.dp) ?: 66.dp
            )){

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = animatedPaddingValuesHor.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp, top = 6.dp)
                    .border(1.dp, if (isOrange) ColorOFBorder else Violet, CircleShape)
                    .height(
                        MainActivity.maxHeight?.times(maxHeight - 0.003f)
                            ?: (66.dp - animatedPaddingValuesHor.dp)
                    )
                    .background(
                        if (isOrange) ColorUnderButton else ShadeBackground, CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 3.dp)
                    .border(1.dp, if (isOrange) AltBG else Violet, CircleShape)
                    .height(
                        MainActivity.maxHeight?.times(maxHeight - 0.003f)
                            ?: (66.dp - animatedPaddingValuesHor.dp)
                    )
                    .background(
                        if (isOrange) ColorOFButton else AltBG, CircleShape
                    )
            ) {
                    val color = if(isOrange) if(text == "Recieve"||text == "Withdraw") if(shouldBeActive) AltBG else DarkOrange else AltBG else Color(0xFF5745C6)

                    if(text!=""){

                        Text(
                            maxLines = 1,
                            text = text,
                            color = color,
                            fontFamily = krub_sb,
                            fontSize = ((if(MainActivity.isBiggerThen700dp == true) big_text_size_f else 24 )-animatedFontPaddingValues).sp,
                            textAlign = TextAlign.Center,
                            modifier = modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                        )
                    }
                    else
                        if (content != null) {
                            content(Modifier.align(Alignment.Center))
                        }

                }
            }
    }
    }

@Composable
fun ActiveBackground(
    multipliyer: Int,
    balance: Float,
    progress: Float,
    leftTime: Long){

    val brush = Brush.horizontalGradient(
        0.0f to DarkOrange,
        progress to DarkOrange,
        progress+ 0.00001f to ColorOFButton,
        1.0f to ColorOFButton)
    val maxHeight = 0.075f//was 0.085f

    Box(
        Modifier
            .fillMaxWidth()
            .height(
                MainActivity.maxHeight
                    ?.times(maxHeight)
                    ?.plus(3.dp) ?: 66.dp
            )){

        Box(
            Modifier
                .fillMaxWidth()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 6.dp)
                    .border(1.dp, ColorOFBorder, CircleShape)
                    .height(
                        MainActivity.maxHeight?.times(maxHeight - 0.003f) ?: (66.dp)
                    )
                    .background(
                        ColorUnderButton, CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 6.dp)
                    .border(1.dp, AltBG, CircleShape)
                    .height(
                        MainActivity.maxHeight?.times(maxHeight - 0.003f) ?: (66.dp)
                    )
                    .background(
                        brush, CircleShape
                    )
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween,verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9f)
                    .align(Alignment.Center)
                    .padding(start = 8.dp,end = 8.dp)){
                    Column(verticalArrangement = Arrangement.SpaceBetween) {

                        Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.02f)?:22.dp), modifier = Modifier){

                            Text(//usdt
                                text = "Mining:",
                                color = WhiteText,
                                textAlign = TextAlign.Center,fontFamily = krub_sb,
                                fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.01f)?:22.dp)){
                                Image(painterResource(id = R.drawable.balance_small_icon),"",modifier = Modifier.align(
                                    Alignment.CenterVertically
                                ))
                                /*
                                                            Text(//usdt
                                                                text = getBalanceText(balance.toFloat()),
                                                                color = WhiteText,
                                                                textAlign = TextAlign.Center,fontFamily = itimStyle,
                                                                fontSize = 20.sp
                                                            )*/
                                AnimatedSum(balance)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.047f)?:22.dp), modifier = Modifier){


                            Text(//usdt
                                text = "Timer:",
                                color = WhiteText,
                                textAlign = TextAlign.Center,fontFamily = krub_sb,
                                fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp
                            )
                            val f = if((MainActivity.maxWidth)!! > 360.dp) 0.02f else 0.04f
                            Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(f)?:0.dp)){
                                Image(painterResource(id = R.drawable.time_icon),"",
                                    Modifier
                                        .fillMaxHeight(0.5f)
                                        .align(Alignment.CenterVertically),contentScale = ContentScale.FillHeight)

                                /*                        Text(//usdt
                                                            text = timerText,
                                                            color = WhiteText,
                                                            textAlign = TextAlign.Center,fontFamily = itimStyle,
                                                            fontSize = 20.sp
                                                        )*/
                                AnimatedNumbers(leftTime)
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End){

                        Row(horizontalArrangement = Arrangement.spacedBy(MainActivity.maxWidth?.times(0.01f)?:22.dp)){
                            Image(painterResource(id = R.drawable.upgrade_icon),"",
                                Modifier
                                    .fillMaxHeight(0.3f)
                                    .align(Alignment.CenterVertically),contentScale = ContentScale.FillHeight)

                            Text(//usdt
                                text = "x$multipliyer",
                                color = WhiteText,
                                textAlign = TextAlign.Center,fontFamily = krub_sb,
                                fontSize = if(MainActivity.isBiggerThen700dp==true) 16.sp else 14.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                    }

                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedNumbers(timeRemaining: Long) {

    val hours = ((timeRemaining/1000) / 3600).toInt()
    val minutes = (((timeRemaining/1000) / 60)%60).toInt()
    val seconds = ((timeRemaining/1000) % 60).toInt()

    Column(
    ) {
        Row {
            AnimatedTimeUnit(timeUnit = hours / 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Tens place of minutes
            AnimatedTimeUnit(timeUnit = hours % 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Tens place of minutes
            Text(//usdt
                text = ":",
                color = WhiteText,
                textAlign = TextAlign.Center,fontFamily = krub_sb,
                fontSize =  if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp
            )
            AnimatedTimeUnit(timeUnit = minutes / 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Tens place of minutes
            AnimatedTimeUnit(timeUnit = minutes % 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Units place of minutes

            Text(//usdt
                text = ":",
                color = WhiteText,
                textAlign = TextAlign.Center,fontFamily = krub_sb,
                fontSize =  if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp
            )
            AnimatedTimeUnit(timeUnit = seconds / 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Tens place of seconds
            AnimatedTimeUnit(timeUnit = seconds % 10, fontSize = if(MainActivity.isBiggerThen700dp==true) 14.sp else 12.sp) // Units place of seconds
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedSum(balance: Float) {

    val enLocale = Locale("en", "EN")
    var formattedString = String.format(enLocale,"%.11f", balance).trimEnd('0').trimEnd('.') // Format to 8 decimal places and trim trailing zeros and decimal point




    val bal = balance.toInt()
    val lastNumber = bal%10
    val number1 = bal/10 %10
    val number2 = bal/100 %10
    val number3 = bal/1000 %10
    val number4 = bal/10000 %10
    val number5 = bal/100000 %10
    val number6 = bal/1000000 %10
    val number7 = bal/10000000 %10
    val number8 = bal/100000000 %10



    Column(
    ) {
        Row {
            if(number8>0){
                AnimatedTimeUnit(timeUnit = number8) // Units place of minutes
            }
            if(number8>0||number7>0){
                AnimatedTimeUnit(timeUnit = number7) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0){
                AnimatedTimeUnit(timeUnit = number6) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0||number5>0){
                AnimatedTimeUnit(timeUnit = number5) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0||number5>0||number4>0){
                AnimatedTimeUnit(timeUnit = number4) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0||number5>0||number4>0||number3>0){
                AnimatedTimeUnit(timeUnit = number3) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0||number5>0||number4>0||number3>0||number2>0){
                AnimatedTimeUnit(timeUnit = number2) // Units place of minutes
            }
            if(number8>0||number7>0||number6>0||number5>0||number4>0||number3>0||number2>0||number1>0){
                AnimatedTimeUnit(timeUnit = number1) // Units place of minutes
            }
            AnimatedTimeUnit(timeUnit = lastNumber) // Units place of seconds


            formattedString = transformNumber(formattedString)

            Row {
                Text(".", fontSize = timersp, color = WhiteText, fontFamily = krub_sb, modifier = Modifier)
                AnimatedBalanceFractionUnit(timeUnit = formattedString[0]) // First digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[1]) // Second digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[2]) // Third digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[3]) // Fourth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[4]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[5]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[6]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[7]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[8]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[9]) // Fifth digit after decimal
                AnimatedBalanceFractionUnit(timeUnit = formattedString[10]) // Fifth digit after decimal

            }
        }
    }
}
fun transformNumber(number: String): String {
    // Check if the number contains a decimal point
    var numberWithoutCommas = number.replace("0,", "")
    numberWithoutCommas = numberWithoutCommas.replace(",", "")
    val decimalIndex = numberWithoutCommas.indexOf('.')
    if (decimalIndex == -1) {
        return numberWithoutCommas +"000000000000" // If no decimal point, return the number as is
    }

    // Get the integer and fractional parts
    val integerPart = numberWithoutCommas.substring(0, decimalIndex)
    val fractionalPart = numberWithoutCommas.substring(decimalIndex + 1)

    Log.d("formatted balance",""+numberWithoutCommas)
    // Combine the fractional part with the integer part and pad with zeros if necessary
    val result = fractionalPart.padEnd(fractionalPart.length + integerPart.length, '0')

    return result +"000000000000"
}

val timersp = if(MainActivity.isBiggerThen700dp==true) 16.sp else 14.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTimeUnit(timeUnit: Int, color: Color = WhiteText, fontSize: TextUnit = timersp) {
    AnimatedContent(
        targetState = timeUnit,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()).using(
                SizeTransform(clip = false)
            )
        }
    ) { targetNumber ->
        Text(//usdt
            text = "$targetNumber",
            color = color,
            textAlign = TextAlign.Center,fontFamily = krub_sb,
            fontSize = fontSize
        )
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBalanceFractionUnit(timeUnit: Char, color: Color = WhiteText) {
    AnimatedContent(
        targetState = timeUnit,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()).using(
                SizeTransform(clip = true)
            )
        }
    ) { targetNumber ->
        Text(//usdt
            text = "$targetNumber",
            color = color,
            textAlign = TextAlign.Center,fontFamily = krub_sb,
            fontSize = timersp
        )
    }
}

/*
fun dateToString(date: DateTime){

}*/
