package com.johov.bitcoin.ui.view.composables

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.AlmostTransparent
import com.johov.bitcoin.ui.theme.ButtonTextColor
import com.johov.bitcoin.ui.theme.SideTextColor
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_sb
import com.johov.bitcoin.ui.theme.medium_text_size
import java.util.Locale

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BalanceBar(claimedBalance: Float,balance: Float,progress: Float, isFarming: Boolean, coins: List<Coin?>,currentMultiplier: Int, onRemove: (Int) -> Unit) {
    var progress = 1-progress
    var alphaText by remember { mutableStateOf(0f) }

    val animatedAlphaText by animateFloatAsState(
        targetValue = alphaText,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    var image by remember { mutableStateOf(R.drawable.empty) }
    LaunchedEffect(progress) {

        if(progress!=1f){

            when(progress){
                in 0f..(1f / 5f) -> image = R.drawable.full_1
                in (1f / 5f)..(1f / 5f)*2 -> image = R.drawable.full_2
                in (1f / 5f)*2..(1f / 5f)*3 -> image = R.drawable.full_3
                in (1f / 5f)*3 ..(1f / 5f)*4 -> image = R.drawable.full_4
                in (1f / 5f)*4..1f -> image = R.drawable.full_5
            }
        }
        else{
            image = R.drawable.empty
        }
        alphaText = if(alphaText== almostAlpha0){
            1f
        } else almostAlpha0
    }
    LaunchedEffect(key1 = isFarming) {
        if(!isFarming){
            alphaText = almostAlpha0
        }
    }
    var finalColorPlus by remember { mutableStateOf(AlmostTransparent) }
    val animatedColorPlus by animateColorAsState(
        targetValue = finalColorPlus,
        animationSpec = tween(durationMillis = 400)
    )
    LaunchedEffect(claimedBalance) {
        Log.d("ssssssdss",MainActivity.maxHeight?.value.toString())
        if(balance.toInt() !=0){
            finalColorPlus = SideTextColor
            delay(300)
            finalColorPlus = AlmostTransparent
        }
    }
    val stroke = Stroke(
        width = 5f,
        pathEffect = pathEffect
    )
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.28F)
    ) {

        Image(
            painter = painterResource(id = R.drawable.balance_bg),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .align(Alignment.TopCenter)
        )
        FallingCoins(coins,MainActivity.maxHeight?.value?:1000f,MainActivity.maxWidth?.value?:1000f,1f+currentMultiplier*0.01f, onRemove)
        Box(modifier = Modifier
            .fillMaxWidth()){
           Image(
                painter = painterResource(id = R.drawable.balance_top_bg),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter), contentScale = ContentScale.FillWidth
            )
            Column(Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.fillMaxHeight(0.08f))
                Text("Total balance in BTC", fontSize = 16.sp, color = Color(0xFF4E4E6C), fontFamily = itimStyle, modifier = Modifier
                    .align(Alignment.CenterHorizontally))
            }
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.6f))
                    Text("", fontSize = medium_text_size, color = ButtonTextColor, fontFamily = itimStyle, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                Spacer(Modifier.width(48.dp))
            }
            Spacer(Modifier.height(12.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .align(BiasAlignment(0f,0.1f))
                .padding(end = 16.dp)
        ) {
            Text(" ", fontSize = 36.sp, color = animatedColorPlus, fontFamily = itimStyle, modifier = Modifier)
            BalanceText(claimedBalance)
            Spacer(Modifier.height(1.dp))
        }
        Text("Don't forget to credit your points when the timer ends", textAlign = TextAlign.Center, fontSize = 12.sp, color = WhiteText, fontFamily = itimStyle, maxLines = 1, modifier = Modifier
            .align(
                Alignment.BottomCenter
            )
            .alpha(animatedAlphaText)
            .padding(bottom = 20.dp)
            .fillMaxWidth())
    }
}


val pathEffect = if (MainActivity.isBiggerThen700dp == true) PathEffect.dashPathEffect(
    floatArrayOf(MainActivity.maxHeight?.value?.div(20) ?: 12f, MainActivity.maxHeight?.value?.div(20) ?: 12f),
    0f
) else PathEffect.dashPathEffect(
    floatArrayOf(10f, 10f),
    0f
)

fun deriveIntegerFromFraction(number: Float): Int {
    // Convert the number to a string

    if(number<0){
        return 0
    }
    val derivedInt = number.toInt()

    return derivedInt
}
@Composable
fun BalanceText(balanceFloat: Float) {

    val balance = deriveIntegerFromFraction(balanceFloat)

    val enLocale = Locale("en", "EN")
    var formattedString = String.format(enLocale,"%.11f", balanceFloat).trimEnd('0').trimEnd('.') // Format to 8 decimal places and trim trailing zeros and decimal point

    val charList = formattedString.toCharArray()

// Extract digits from the integer part
    val lastNumber = balance % 10
    val number1 = balance / 10 % 10
    val number2 = balance / 100 % 10
    val number3 = balance / 1000 % 10
    val number4 = balance / 10000 % 10
    val number5 = balance / 100000 % 10
    val number6 = balance / 1000000 % 10
    val number7 = balance / 10000000 % 10
    val number8 = balance / 100000000 % 10

    var fractionPart = transformNumber(formattedString)

    LaunchedEffect(key1 = balanceFloat) {
/*        if(fractionPart[0]==lastNumber.toString()[0]&&lastNumber.toChar()!=(deriveIntegerFromFraction(balanceFloat*10f).toString().last())){
            *//*fractionPart.replaceFirst(fractionPart[0],'0')*//*
            fractionPart = "0"+fractionPart.slice(1..<fractionPart.length)
            Log.d("sssss","true worked")
        }
        var s = "skibidi"
        Log.d("ssssssss","first is " + s)
        s =  "0"+s.slice(1..<s.length)
        Log.d("ssssssss","sec is " + s)*/
        Log.d("sssss",fractionPart[0].toString())
        Log.d("sssss",lastNumber.toString()[0].toString())
        Log.d("sssss",deriveIntegerFromFraction(balanceFloat*10f).toString().last().toString())
        Log.d("sssss",(fractionPart[0]==lastNumber.toChar()).toString())
        Log.d("sssss",(lastNumber.toChar()!=(deriveIntegerFromFraction(balanceFloat*10f).toString().last())).toString())
        Log.d("sssss","here")
        Log.d("BalanceTag1",""+balanceFloat)
        Log.d("BalanceTag2",""+balance)
        Log.d("BalanceTag3",""+fractionPart)
        Log.d("BalanceTag4",""+fractionPart[0])
        Log.d("BalanceTag5",""+fractionPart[5])
    }
    Column {
        Row {
            if (number8 > 0) {
                AnimatedBalanceUnit(timeUnit = number8) // Units place of minutes
            }
            if (number8 > 0 || number7 > 0) {
                AnimatedBalanceUnit(timeUnit = number7) // Units place of minutes
            }
            if (number8 > 0 || number7 > 0 || number6 > 0) {
                AnimatedBalanceUnit(timeUnit = number6) // Units place of minutes
                Text(" ", fontSize = 30.sp, color = SideTextColor, fontFamily = itimStyle, modifier = Modifier)
            }
            if (number8 > 0 || number7 > 0 || number6 > 0 || number5 > 0) {
                AnimatedBalanceUnit(timeUnit = number5) // Units place of minutes
            }
            if (number8 > 0 || number7 > 0 || number6 > 0 || number5 > 0 || number4 > 0) {
                AnimatedBalanceUnit(timeUnit = number4) // Units place of minutes
            }
            if (number8 > 0 || number7 > 0 || number6 > 0 || number5 > 0 || number4 > 0 || number3 > 0) {
                AnimatedBalanceUnit(timeUnit = number3) // Units place of minutes
                Text(" ", fontSize = 30.sp, color = SideTextColor, fontFamily = itimStyle, modifier = Modifier)
            }
            if (number8 > 0 || number7 > 0 || number6 > 0 || number5 > 0 || number4 > 0 || number3 > 0 || number2 > 0) {
                AnimatedBalanceUnit(timeUnit = number2) // Units place of minutes
            }
            if (number8 > 0 || number7 > 0 || number6 > 0 || number5 > 0 || number4 > 0 || number3 > 0 || number2 > 0 || number1 > 0) {
                AnimatedBalanceUnit(timeUnit = number1) // Units place of minutes
            }
            AnimatedBalanceUnit(timeUnit = lastNumber) // Units place of seconds


            Row {
                Text(".", fontSize = 30.sp, color = SideTextColor, fontFamily = krub_sb, modifier = Modifier)
                AnimatedBalanceStringUnit(delay = 1000, timeUnit = if(fractionPart[0]==lastNumber.toString()[0]&&lastNumber.toString()[0]!=(deriveIntegerFromFraction(balanceFloat*10f).toString().last())) '0' else fractionPart[0]) // First digit after decimal
                AnimatedBalanceStringUnit(delay = 900,timeUnit = fractionPart[1]) // Second digit after decimal
                AnimatedBalanceStringUnit(delay = 800,timeUnit = fractionPart[2]) // Third digit after decimal
                AnimatedBalanceStringUnit(delay = 700,timeUnit = fractionPart[3]) // Fourth digit after decimal
                AnimatedBalanceStringUnit(delay = 600,timeUnit = fractionPart[4]) // Fifth digit after decimal
                AnimatedBalanceStringUnit(delay = 500,timeUnit = fractionPart[5]) // Sixth digit after decimal
                AnimatedBalanceStringUnit(delay = 400,timeUnit = fractionPart[6]) // Sixth digit after decimal
                AnimatedBalanceStringUnit(delay = 300,timeUnit = fractionPart[7]) // Sixth digit after decimal
                AnimatedBalanceStringUnit(delay = 200,timeUnit = fractionPart[8]) // Sixth digit after decimal
                AnimatedBalanceStringUnit(delay = 100,timeUnit = fractionPart[9]) // Sixth digit after decimal
                AnimatedBalanceStringUnit(delay = 0,timeUnit = fractionPart[10]) // Sixth digit after decimal
            }
        }

        LaunchedEffect(key1 = balanceFloat) {
            Log.d("UITESTS!",""+ balance + " "+balanceFloat)
            Log.d("UITESTS!",""+ balance + " "+formattedString)
            Log.d("UITESTS!",""+ balance + " "+formattedString.length)
        }
        // Add a row for the decimal point and fractional part
    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBalanceUnit(timeUnit: Int, color: Color = SideTextColor) {
    // State to hold the current displayed number
    var currentNumber = timeUnit

    // AnimatedContent for the scrolling animation
    AnimatedContent(
        targetState = currentNumber,
        transitionSpec = {
            (slideInVertically { height -> height } + fadeIn() with
                    slideOutVertically { height -> -height } + fadeOut()).using(
                SizeTransform(clip = false)
            )
        }
    ) { targetNumber ->
        Text(
            targetNumber.toString(),
            fontSize = 30.sp,
            color = color,
            fontFamily = krub_sb,
            modifier = Modifier
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedBalanceStringUnit(timeUnit: Char, color: Color = SideTextColor, delay: Int = 0) {
    // State to hold the current displayed number
    var currentNumber = timeUnit

    // AnimatedContent for the scrolling animation
    AnimatedContent(
        targetState = currentNumber,
        transitionSpec = {
            (slideInVertically(animationSpec = tween(delayMillis = delay, durationMillis = 700) ) { height -> height } + fadeIn(animationSpec = tween(delayMillis = delay,durationMillis = 700)) with
                    slideOutVertically(animationSpec = tween(delayMillis = delay,durationMillis = 700) )  { height -> -height } + fadeOut(animationSpec = tween(delayMillis = delay,durationMillis = 700))).using(
                SizeTransform(clip = false)
            )
        }
    ) { targetNumber ->
        Text(
            targetNumber.toString(),
            fontSize = 30.sp,
            color = color,
            fontFamily = krub_sb,
            modifier = Modifier
        )
    }
}




fun formatTime(time: Int): String {
    val minutes = time / 60
    val seconds = time % 60
    return String.format("%02d:%02d", minutes, seconds)
}
@Composable
fun FallingCoins(coins: List<Coin?>,maxHeight: Float, maxWidth: Float,speedModifier: Float,onRemove: (Int) -> Unit) {
    val scope = rememberCoroutineScope()

/*    LaunchedEffect(Unit) {
        while (true) {
            for(i in 0..MULTIPLYER_ENUM.MULTYPLIER_5x.value){
                coins = coins + Coin(
                    id = System.currentTimeMillis(),
                    x = (0..300).random().toFloat()
                )
            }
            delay(1000)
        }
    }*/
    for(i in 0..<coins.size){
        if(coins[i]!=null){
            FallingCoin(
                modifier = Modifier,
                coin = coins[i]!!,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                speedModifier = speedModifier,
                onRemove = { onRemove(i) }
            )
        }
    }
/*    coins.forEach { coin ->
        FallingCoin(
            modifier = Modifier,
            coin = coin,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            onRemove = { onRemove(coin.key) }
        )
    }*/
}


@Composable
fun FallingCoin(modifier: Modifier, maxHeight: Float, maxWidth:Float, coin: Coin,speedModifier: Float, onRemove: (Integer) -> Unit) {
    val yOffset = remember { Animatable(coin.startY*maxHeight) }
    val xOffset = remember { Animatable(coin.startX*maxWidth) }
    val size = remember { Animatable(1f) }
    val alpha = remember { Animatable(almostAlpha0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(coin.id) {
        scope.launch {
            yOffset.animateTo(
                targetValue = coin.endY*maxHeight, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            xOffset.animateTo(
                targetValue = coin.endX*maxWidth, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            size.animateTo(
                targetValue = coin.endSize, // Adjust this value as needed for the falling distance
                animationSpec = tween(durationMillis = (1000/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
        }
        scope.launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = (500/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
            delay((200/speedModifier).toLong())
            alpha.animateTo(
                targetValue = almostAlpha0,
                animationSpec = tween(durationMillis = (300/speedModifier).toInt(), easing = FastOutLinearInEasing)
            )
            Log.d("Worked","work + "+coin.id)
            onRemove(coin.id) // Remove the coin after the animation ends
        }
    }
    Image(
        painter = rememberAsyncImagePainter(model = R.drawable.anim_item),
        contentDescription = "",
        modifier = modifier
            .absoluteOffset(x = xOffset.value.dp, y = yOffset.value.dp)
            .size((32 * size.value).dp)
            .alpha(alpha.value)
    )
}

const val almostAlpha0 = 0.000001f

data class Coin(
    var id: Integer,
    var endSize: Float,
    var startX: Float = (0..1).random().toFloat(),
    var startY: Float = (0..1).random().toFloat(),
    var endX: Float = (0..1).random().toFloat(),
    var endY: Float = (0..1).random().toFloat(),
)
