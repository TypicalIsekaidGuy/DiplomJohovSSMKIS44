package com.johov.bitcoin.ui.view.screen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.ui.theme.BorderColorButton
import com.johov.bitcoin.ui.theme.WhiteText
import com.johov.bitcoin.ui.theme.itimStyle
import com.johov.bitcoin.ui.view.composables.BalanceBar
import com.johov.bitcoin.ui.view.composables.*
import com.johov.bitcoin.viewmodel.MainViewModel
import com.johov.bitcoin.viewmodel.RefferralViewModel
import com.johov.bitcoin.R
import com.johov.bitcoin.ui.theme.AltBG
import com.johov.bitcoin.ui.theme.SideTextColor
import com.johov.bitcoin.ui.theme.Violet
import com.johov.bitcoin.ui.theme.krub_sb

@Composable
fun RefferalsScreen(
    viewModel: RefferralViewModel,
    mainViewModel: MainViewModel,
    onLaunch: () -> Unit,
    checkup: () -> Unit
){
    Column {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.fillMaxHeight(0.04f))
            BalanceBar(claimedBalance!!,balance!!,progress!!, isMining!!,coins!!,currentBoost!!.value!!, mainViewModel::removeCoin)
            Spacer(modifier = Modifier.fillMaxHeight(0.03f))
            CopyCodeBlock(refText1!!,code.value!!,{
                viewModel.shareCodeOnClick()
            },{
                viewModel.copyCodeOnClick()
            })
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            InputRefferalBlock(canGetRefferalBonus!!,limitOfCode,textValue) {
                viewModel.getBonusFromCodeOnClick()
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    }
}

/*@Composable
fun InviteRefferalTextBlock(refText1: String){
    Column( horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Invite Referral", fontSize = big_text_size, color = RefferalTextColor, fontFamily = itimStyle, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.fillMaxHeight(0.03f))
        Text(refText1, fontSize = average_text_size, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Center)

    }
}*/
@Composable
fun CopyCodeBlock(text: String,code:String,onClick1:()->Unit,onClick2: () -> Unit){

    var paddingValues by remember { mutableStateOf(0f) }
    var paddingValues2 by remember { mutableStateOf(0f) }

    val animatedPaddingValues by animateFloatAsState(
        targetValue = paddingValues,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val animatedPaddingValues2 by animateFloatAsState(
        targetValue = paddingValues2,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)){
        Image(painter = painterResource(id = R.drawable.big_black_background), contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier.fillMaxWidth())

        Column {
            Spacer(Modifier.fillMaxHeight(0.03f))
            Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()){
                Text("Invite Referral:", fontSize = 20.sp, color = Violet, fontFamily = itimStyle, textAlign = TextAlign.Start, modifier = Modifier.padding(top = 4.dp))
                Image(painter = painterResource(id = R.drawable.referal_first_illustration), contentDescription = "",modifier = Modifier.fillMaxWidth(), contentScale = ContentScale.FillWidth)

            }
            Spacer(Modifier.fillMaxHeight(0.05f))
            Text(text, fontSize = 14.sp, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 8.dp))
            Spacer(Modifier.fillMaxHeight(0.13f))
            Text("Your Referral code:", fontSize = 16.sp, color = Violet, fontFamily = itimStyle, textAlign = TextAlign.Start, modifier = Modifier.align(BiasAlignment.Horizontal(-0.5f)))

            Spacer(Modifier.fillMaxHeight(0.05f))
            Box(
                Modifier
                    .fillMaxHeight(if (MainActivity.isBiggerThen700dp == true) 0.55f else 0.75f)
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
                    .background(
                        AltBG, CircleShape
                    )
                    .border(
                        1.dp,
                        Violet, CircleShape
                    )){
                Text(code.toString(),modifier = Modifier.align(Alignment.Center), fontSize = if(MainActivity.isBiggerThen700dp == true) 25.sp else 20.sp, color = Color(0xFF8426FB), fontFamily = krub_sb, textAlign = TextAlign.Center)

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)){

                    Image(painter = painterResource(id = R.drawable.copy_button), contentDescription = "",modifier = Modifier
                        .padding(vertical = 4.dp).padding(animatedPaddingValues2.dp)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            MainScope().launch {
                                paddingValues2 = 6f
                                delay(300)
                                paddingValues2 = 0f
                                onClick2()
                            }
                        }, contentScale = ContentScale.FillHeight)//clickable

                    Image(painter = painterResource(id = R.drawable.share_button), contentDescription = "",modifier = Modifier
                        .padding(animatedPaddingValues.dp)
                        .fillMaxHeight()
                        .padding(vertical = 4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            MainScope().launch {
                                paddingValues = 6f
                                delay(300)
                                paddingValues = 0f
                                onClick1()
                            }
                        }, contentScale = ContentScale.FillHeight)
                }
            }
/*            Row(modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(vertical = 1.dp)) {
                    Text("Your code for a friend", fontSize = if(MainActivity.isBiggerThen700dp == true) 12.sp else 10.sp, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Start, modifier = Modifier.padding(top = 4.dp))
                    Text(code.toString(), fontSize = if(MainActivity.isBiggerThen700dp == true) 30.sp else 24.sp, color = WithdrawalTextColor, fontFamily = itimStyle, textAlign = TextAlign.Start)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp), modifier = Modifier
                    .padding(start = MainActivity.maxWidth?.times(0.08f) ?: 40.dp)){

                    val fractionWidth = 0.2f
                    val fractionHeight = 0.062f

                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                            .padding(animatedPaddingValues.dp)
                            .height(
                                MainActivity.maxHeight
                                    ?.times(fractionHeight)
                                    ?.plus(3.dp) ?: 66.dp
                            )){

                        Box(
                            Modifier

                                .width(MainActivity.maxWidth?.times(fractionWidth)?:80.dp)) {

                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .padding(top = 12.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(fractionHeight - 0.005f)
                                            ?: 66.dp
                                    )
                                    .background(
                                        ShadeBackground, CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(fractionHeight - 0.005f)
                                            ?: 66.dp
                                    )
                                    .background(
                                        WhiteText, CircleShape
                                    )
                            ) {

                                Box(
                                    Modifier
                                        .size(
                                            MainActivity.maxHeight?.times(fractionHeight - 0.008f)
                                                ?: 60.dp
                                        )
                                        .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                        .align(Alignment.Center)
                                        .padding(vertical = 6.dp)){
                                    Image(painter = painterResource(id = R.drawable.share_button), contentDescription = "",modifier = Modifier
                                        .padding(animatedPaddingValues.dp)
                                        .align(Alignment.Center)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            MainScope().launch {
                                                paddingValues = 6f
                                                delay(300)
                                                paddingValues = 0f
                                                onClick1()
                                            }
                                        }, contentScale = ContentScale.FillWidth)//clickable

                                }
                            }
                        }
                    }



                    Box(
                        Modifier
                            .align(Alignment.CenterVertically)
                            .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                            .padding(animatedPaddingValues2.dp)
                            .height(
                                MainActivity.maxHeight
                                    ?.times(fractionHeight)
                                    ?.plus(3.dp) ?: 66.dp
                            )){

                        Box(
                            Modifier
                                .width(MainActivity.maxWidth?.times(fractionWidth)?:80.dp)) {

                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .padding(top = 12.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(fractionHeight - 0.005f)
                                            ?: 66.dp
                                    )
                                    .background(
                                        ShadeBackground, CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(fractionHeight - 0.005f)
                                            ?: 66.dp
                                    )
                                    .background(
                                        WhiteText, CircleShape
                                    )
                            ) {


                                Box(
                                    Modifier

                                        .size(
                                            MainActivity.maxHeight?.times(fractionHeight - 0.008f)
                                                ?: 60.dp
                                        )
                                        .fillMaxHeight()
                                        .align(Alignment.Center)
                                        .padding(vertical = 6.dp)){
                                    Image(painter = painterResource(id = R.drawable.copy_button), contentDescription = "",modifier = Modifier

                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            MainScope().launch {
                                                paddingValues2 = 6f
                                                delay(300)
                                                paddingValues2 = 0f
                                                onClick2()
                                            }
                                        }, contentScale = ContentScale.FillHeight)//clickable
                                }
                            }
                        }
                    }
                }
            }*/
        }
    }
}

@Composable
fun InputRefferalBlock(isActive: Boolean, limitOfCode: Int,textValue: MutableLiveData<String>, onClick:()->Unit){
    val textValueState = textValue.observeAsState(initial = "")

    var isOverCount by remember{
        mutableStateOf(textValue.value!!.count() <= limitOfCode)
    }
    var isTextEmpty by remember {
        mutableStateOf(textValueState.value.isEmpty())
    }

    LaunchedEffect(textValueState.value.isEmpty()) {
        if(textValueState.value.isNullOrEmpty()){
            isTextEmpty = true
        }
    }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Box(
        Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth()){
        Image(painter = painterResource(id = if(isActive) R.drawable.referal_input_bg else R.drawable.referal_input_bg_inactive),"", Modifier.fillMaxSize(), contentScale = ContentScale.FillBounds)



        Text("Your friend's referral code:", fontSize = 16.sp, color = Color(0xFFF99F3D), fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.align(BiasAlignment(-0.6f,-0.9f)))




        if(isActive){

            Box(
                Modifier.fillMaxHeight(0.35f).fillMaxWidth(0.85f).align(BiasAlignment(0f,-0.2f)).background(
                    BorderColorButton,RoundedCornerShape(16.dp))

                    .align(Alignment.Center)){
/*            Box(){
                Modifier.fillMaxHeight(0.2f).fillMaxWidth(0.8f).align(Alignment.Center).background(Violet,RoundedCornerShape(16.dp))
            }*/

                BasicTextField(
                    cursorBrush = Brush.verticalGradient(listOf(SideTextColor,SideTextColor)),
                    enabled = isActive,
                    value = textValueState.value,
                    onValueChange = { newText ->
                        isOverCount = newText.count() <= limitOfCode
                        if(isOverCount){
                            textValue.value = newText
                        }
                        else{

                            focusManager.clearFocus()
                        }
                        if(newText.count() >= limitOfCode){

                            focusManager.clearFocus()
                        }
                        isTextEmpty = textValue.value!!.isEmpty()
                    },
                    modifier = Modifier
                        .fillMaxHeight(0.5f)
                        .fillMaxWidth(0.9f).align(Alignment.Center)
                        .focusRequester(focusRequester)/*
                    .padding(
                        start = MainActivity.maxWidth?.times(0.1f) ?: 50.dp,
                        end = 4.dp
//                    )*//*.background(BorderColorButton,RoundedCornerShape(16.dp))*/,
                    textStyle = TextStyle(
                        textAlign = TextAlign.Start,
                        fontFamily = krub_sb,
                        fontSize = 20.sp,
                        color = SideTextColor
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                            /*.padding(vertical  =4.dp)*/


                        ) {
                            if(isTextEmpty){
                                Text("Enter your friend's referral code here", fontSize = 14.sp, color = Color(0xFF4E4E6C), fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.align(BiasAlignment(0f,0f)))

                            }

                            innerTextField()
                        }
                    }
                )
            }

            Box(Modifier.fillMaxWidth().fillMaxHeight(0.35f).align(Alignment.BottomCenter)){
                InActiveBackground(isPressable = true, onPress = {
                    focusManager.clearFocus()
                    onClick()},text = "RECIEVE", isOrange = true, shouldBeActive = !isTextEmpty)
            }
        }
        else{
            Image(painter = painterResource(id = R.drawable.recieved_pic),"", Modifier.align(Alignment.Center))

        }
    }

/*    Box(
        Modifier
            .fillMaxWidth()
            .height(
                MainActivity.maxHeight
                    ?.times(0.09f)
                    ?.plus(3.dp) ?: 66.dp
            )
            .paint(
                painterResource(id = if (isActive) R.drawable.ref_enter_field else R.drawable.ref_enter_field_inactive),
                contentScale = ContentScale.FillWidth
            )){

        if(isActive){

            Text("Enter your friend's code", fontSize = average_text_size, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier
                .align(
                    Alignment.TopStart
                )
                .padding(
                    start = MainActivity.maxWidth?.times(0.22f) ?: 100.dp,
                    top = MainActivity.maxWidth?.times(0.025f) ?: 12.dp
                ))


        }
        Box( modifier = Modifier
            .height(
                MainActivity.maxHeight
                    ?.times(0.08f)
                    ?.plus(3.dp) ?: 66.dp
            )
            .fillMaxWidth()){

            var isOverCount by remember{
                mutableStateOf(textValue.value!!.count() <= limitOfCode)
            }
            var isTextEmpty by remember {
                mutableStateOf(textValueState.value.isEmpty())
            }

            LaunchedEffect(textValueState.value.isEmpty()) {
                if(textValueState.value.isNullOrEmpty()){
                    isTextEmpty = true
                }
            }
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            if(isActive){

                Box(
                    Modifier
                        .fillMaxWidth(0.7f)
                        .align(Alignment.CenterStart)
                        .padding(
                            start = MainActivity.maxWidth?.times(0.2f) ?: 100.dp,
                            top = MainActivity.maxWidth?.times(0.1f) ?: 50.dp
                        )){

                    BasicTextField(
                        cursorBrush = Brush.verticalGradient(listOf(WithdrawalTextColor,WithdrawalTextColor)),
                        enabled = isActive,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = textValueState.value,
                        onValueChange = { newText ->
                            for(i in newText){
                                if (!i.isDigit()){
                                    return@BasicTextField
                                }
                            }
                            isOverCount = newText.count() <= limitOfCode
                            if(isOverCount)
                                textValue.value = newText
                            isTextEmpty = textValue.value!!.isEmpty()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .padding(
                                start = MainActivity.maxWidth?.times(0.1f) ?: 50.dp,
                                end = 4.dp
                            )
                            .background(
                                Color.Transparent
                            ),
                        textStyle = TextStyle(
                            fontFamily = itimStyle,
                            fontSize = 20.sp,
                            color = WithdrawalTextColor
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    *//*.padding(vertical  =4.dp)*//*


                            ) {

                                innerTextField()
                            }
                        }
                    )
                }

                var paddingValues by remember { mutableStateOf(0f) }

                val animatedPaddingValues by animateFloatAsState(
                    targetValue = paddingValues,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )
                val fractionWidth = 0.2f
                val fractionHeight = 0.062f
                Box(
                    Modifier
                        .padding(end = MainActivity.maxWidth?.times(0.05f) ?: 50.dp)
                        .align(BiasAlignment(1f, 0.2f))
                        .padding(animatedPaddingValues.dp)
                        .padding(4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            MainScope().launch {
                                paddingValues = 6f
                                delay(300)
                                paddingValues = 0f
                                onClick()
                                focusManager.clearFocus()
                            }
                        }

                ){
                    Box(
                        Modifier
                            .align(Alignment.CenterEnd)
                            .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                            .height(
                                MainActivity.maxHeight
                                    ?.times(
                                        fractionHeight - 0.005f
                                    )
                                    ?.plus(3.dp) ?: 66.dp
                            )){

                        Box(
                            Modifier
                                .width(MainActivity.maxWidth?.times(fractionWidth)?:80.dp)) {

                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .padding(top = 12.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(
                                            fractionHeight - 0.005f
                                        )
                                            ?: 66.dp
                                    )
                                    .background(
                                        ShadeBackground, CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .width(MainActivity.maxWidth?.times(fractionWidth) ?: 80.dp)
                                    .border(2.dp, BorderColorButton, CircleShape)
                                    .height(
                                        MainActivity.maxHeight?.times(
                                            fractionHeight - 0.008f
                                        )
                                            ?: 66.dp
                                    )
                                    .background(
                                        WhiteText, CircleShape
                                    )
                            ) {


                                Box(
                                    Modifier.padding(animatedPaddingValues.dp)){
                                    Image(painter = painterResource(id = R.drawable.recieve_inside), contentDescription = "",modifier = Modifier
                                        .padding(animatedPaddingValues.dp)
                                        .fillMaxSize()
                                        .padding(MainActivity.maxWidth?.times(0.01f) ?: 4.dp)
                                        .align(Alignment.Center))//clickable
                                }
                            }
                        }
                    }
                    *//*                Image(painterResource(id = R.drawable.recieve_inside),"",modifier = Modifier
                                        .size((48 - animatedPaddingValues).dp)
                                        .clickable(
                                            enabled = isActive,
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null
                                        ) {
                                            MainScope().launch {
                                                paddingValues = 8f
                                                delay(300)
                                                paddingValues = 0f
                                                focusManager.clearFocus()
                                                onClick()
                                            }
                                        })*//*

                }
            }
            else{

                Text("You have activated\n" +
                        "the code and received a reward", fontSize = 20.sp, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.align(
                    BiasAlignment(0.5f, 0f)))

            }
        }
    }*/
}