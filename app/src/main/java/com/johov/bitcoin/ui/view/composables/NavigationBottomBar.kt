package com.johov.bitcoin.ui.view.composables

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.R
import com.johov.bitcoin.data.model.Screen
import com.johov.bitcoin.ui.theme.AltBG
import com.johov.bitcoin.ui.theme.ButtonTextColor
import com.johov.bitcoin.ui.theme.OffTextTextColor
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.theme.krub_sb

@Composable
fun NavigationBottomBar(modifier: Modifier, selected: Screen, onHomeChoose: ()->Unit, onRefferalsChoose: ()->Unit, onWithdrawalChoose: ()->Unit,){


    var paddingValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues1 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues1 by animateFloatAsState(
        targetValue = paddingValues1,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    var paddingValues2 by remember { mutableStateOf(0f) }

    val animatedPaddingValues2 by animateFloatAsState(
        targetValue = paddingValues2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues2 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues2 by animateFloatAsState(
        targetValue = paddingValues2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    var paddingValues3 by remember { mutableStateOf(0f) }

    val animatedPaddingValues3 by animateFloatAsState(
        targetValue = paddingValues3,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    var paddingFontValues3 by remember { mutableStateOf(0f) }

    val animatedPaddingFontValues3 by animateFloatAsState(
        targetValue = paddingValues3,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    Box(
        modifier
            .fillMaxWidth()
            .background(brush = Brush.horizontalGradient(listOf(AltBG, AltBG)))
            .fillMaxHeight(0.1f)){
        Row(modifier = Modifier
            .fillMaxWidth(0.8f)
            .align(Alignment.Center), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){

            Box(
                Modifier
                    .width(MainActivity.maxWidth?.times(0.2f) ?: 64.dp)
                    .fillMaxHeight()
                    .align(
                        Alignment.CenterVertically
                    )){
                Column(modifier = Modifier
                    .align(
                        BiasAlignment(0f, -0.085f  )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {

                        MainScope().launch {
                            paddingValues1 = 6f
                            paddingFontValues1 = 1f
                            delay(300)
                            paddingValues1 = 0f
                            paddingFontValues1 = 0f
                            onHomeChoose()
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally){
                    Image(painter = painterResource(id = if(selected== Screen.HomeScreen) R.drawable.home_active_icon else R.drawable.home_inactive_icon),"", modifier = Modifier
                        .width((MainActivity.maxWidth?.times(0.08f)?:32.dp) - animatedPaddingValues1.dp)
                        .height((MainActivity.maxWidth?.times(0.08f)?:32.dp) - animatedPaddingValues1.dp)
                        .align(Alignment.CenterHorizontally))
                    Text("Home", textDecoration = if(selected== Screen.HomeScreen) TextDecoration.Underline else TextDecoration.None,  fontSize = (14 - animatedPaddingFontValues1).sp,color =  if(selected== Screen.HomeScreen) ButtonTextColor else OffTextTextColor, fontFamily = if(selected== Screen.HomeScreen) krub_sb else itimStyle)
                }

            }
            Box(
                Modifier
                    .width(MainActivity.maxWidth?.times(0.35f) ?: 64.dp)
                    .fillMaxHeight()
                    .align(
                        Alignment.CenterVertically
                    )){
                Column(modifier = Modifier
                    .align(
                        BiasAlignment(0f, -0.085f  )
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        MainScope().launch {
                            paddingValues2 = 6f
                            paddingFontValues2 = 1f
                            delay(300)
                            paddingValues2 = 0f
                            paddingFontValues2 = 0f
                            onRefferalsChoose()
                        }
                    }, horizontalAlignment = Alignment.CenterHorizontally){
                    Image(painter = painterResource(id = if(selected== Screen.RefferalsScreen) R.drawable.referrals_active_icon else R.drawable.referrals_inactive_icon),"", modifier = Modifier
                        .size((MainActivity.maxWidth?.times(0.08f)?:30.dp) - animatedPaddingValues2.dp)
                        .align(Alignment.CenterHorizontally))
                    Text("Referral", textDecoration = if(selected== Screen.RefferalsScreen) TextDecoration.Underline else TextDecoration.None,  fontSize =  (14 - animatedPaddingFontValues2).sp,color =  if(selected== Screen.RefferalsScreen) ButtonTextColor else OffTextTextColor, fontFamily = if(selected== Screen.RefferalsScreen) krub_sb else itimStyle)
                }
            }

            Box(
                Modifier
                    .width(MainActivity.maxWidth?.times(0.2f) ?: 64.dp)
                    .fillMaxHeight()
                    .align(
                        Alignment.CenterVertically
                    )){


                    Column(modifier = Modifier
                        .align(

                            BiasAlignment(0f, -0.085f  )

                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            MainScope().launch {
                                paddingValues3 = 6f
                                paddingFontValues3 = 1f
                                delay(300)
                                paddingValues3 = 0f
                                paddingFontValues3 = 0f
                                onWithdrawalChoose()
                            }
                        }, horizontalAlignment = Alignment.CenterHorizontally){
                        Image(painter = painterResource(id = if(selected== Screen.WithdrawalScreen) R.drawable.wallet_active_icon else R.drawable.wallet_inactive_icon),"", modifier = Modifier
                            .size(((MainActivity.maxWidth?.times(0.08f)?:30.dp)  - animatedPaddingValues3.dp))
                            .padding(top = 2.dp)
                            .align(Alignment.CenterHorizontally))
                        Text("Wallet", textDecoration = if(selected== Screen.WithdrawalScreen) TextDecoration.Underline else TextDecoration.None, fontSize =  (14 - animatedPaddingFontValues3).sp,color =  if(selected== Screen.WithdrawalScreen) ButtonTextColor else OffTextTextColor, fontFamily =  if(selected== Screen.WithdrawalScreen) krub_sb else itimStyle, letterSpacing = 0.5.sp)
                    }

                }
        }
    }
}