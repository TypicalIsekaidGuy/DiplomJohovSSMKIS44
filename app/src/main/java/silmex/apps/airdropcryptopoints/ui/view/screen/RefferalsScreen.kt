package silmex.apps.airdropcryptopoints.ui.view.screen

import android.util.Log
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import silmex.apps.airdropcryptopoints.R
import silmex.apps.airdropcryptopoints.ui.theme.AltBG
import silmex.apps.airdropcryptopoints.ui.theme.AltBorder
import silmex.apps.airdropcryptopoints.ui.theme.ButtonTextColor
import silmex.apps.airdropcryptopoints.ui.theme.MainTextColor
import silmex.apps.airdropcryptopoints.ui.theme.MiniBorder
import silmex.apps.airdropcryptopoints.ui.theme.OffTextColor
import silmex.apps.airdropcryptopoints.ui.theme.ShadeBackground
import silmex.apps.airdropcryptopoints.ui.theme.WhiteText
import silmex.apps.airdropcryptopoints.ui.theme.average_text_size
import silmex.apps.airdropcryptopoints.ui.theme.big_text_size
import silmex.apps.airdropcryptopoints.ui.theme.itimStyle
import silmex.apps.airdropcryptopoints.ui.view.composables.BalanceBar
import silmex.apps.airdropcryptopoints.ui.view.composables.*
import silmex.apps.airdropcryptopoints.viewmodel.MainViewModel
import silmex.apps.airdropcryptopoints.viewmodel.RefferralViewModel

@Composable
fun RefferalsScreen(viewModel: RefferralViewModel,mainViewModel: MainViewModel, onLaunch: ()->Unit){
    val balance by viewModel.balance.observeAsState()
    val isMining by viewModel.isMining.observeAsState()
    val coins by mainViewModel.coins.observeAsState()
    val code = viewModel.refCode.observeAsState()
    val textValue = viewModel.textValue
    val refText1 by viewModel.refferralTextValue1.observeAsState()
    val progress by viewModel.progress.observeAsState()
    val limitOfCode = viewModel.limitOfCode
    val currentBoost by mainViewModel.currentChosenMultipliyer.observeAsState()
    val claimedBalance by mainViewModel.claimedBalance.observeAsState()
    val canGetRefferalBonus by mainViewModel.canGetRefferalBonus.observeAsState()

    var didLaunch = remember { mutableStateOf(false) }
    LaunchedEffect(true) {
        onLaunch()
        if(didLaunch.value){
            didLaunch.value = true
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BalanceBar(claimedBalance!!,balance!!,progress!!, isMining!!,coins!!,currentBoost!!.value!!, mainViewModel::removeCoin)
        InviteRefferalTextBlock(refText1!!)
        CopyCodeBlock(code.value!!,{
                           viewModel.shareCodeOnClick()
        },{
            viewModel.copyCodeOnClick()
        })
        InputRefferalBlock(canGetRefferalBonus!!,limitOfCode,textValue) {
            viewModel.getBonusFromCodeOnClick()
        }
        Image(
            painter = painterResource(id = R.drawable.refferral_illustration),
            contentDescription = "",
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ).fillMaxWidth().size(128.dp)
        )
    }
}

@Composable
fun InviteRefferalTextBlock(refText1: String){
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Invite Referral", fontSize = big_text_size, color = MainTextColor, fontFamily = itimStyle, textAlign = TextAlign.Center)
        Text(refText1, fontSize = average_text_size, color = MainTextColor, fontFamily = itimStyle, textAlign = TextAlign.Center)

    }
}
@Composable
fun CopyCodeBlock(code:Int,onClick1:()->Unit,onClick2: () -> Unit){

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
    InActiveBackground(content = {
        Row(modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column(horizontalAlignment = Alignment.Start) {
                Text("Your referral promo code", fontSize = 12.sp, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Start, modifier = Modifier.padding(top = 4.dp))
                Text(code.toString(), fontSize = 30.sp, color = WhiteText, fontFamily = itimStyle, textAlign = TextAlign.Start)

            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxHeight()){
                Box(
                    Modifier
                        .size(60.dp)
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)){
                    Image(painter = painterResource(id = R.drawable.share_button), contentDescription = "",modifier = Modifier
                        .size((60 - animatedPaddingValues).dp)
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            MainScope().launch {
                                paddingValues = 24f
                                delay(300)
                                paddingValues = 0f
                                onClick1()
                            }
                        }, contentScale = ContentScale.FillHeight)//clickable

                }
                Box(
                    Modifier
                        .size(60.dp)
                        .fillMaxHeight()
                        .padding(vertical = 6.dp)){
                    Image(painter = painterResource(id = R.drawable.copy_button), contentDescription = "",modifier = Modifier
                        .size((60 - animatedPaddingValues2).dp)
                        .align(Alignment.Center)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            MainScope().launch {
                                paddingValues2 = 24f
                                delay(300)
                                paddingValues2 = 0f
                                onClick2()
                            }
                        }, contentScale = ContentScale.FillHeight)//clickable
                }
            }
        }
    })
}

@Composable
fun InputRefferalBlock(isActive: Boolean, limitOfCode: Int,textValue: MutableLiveData<String>, onClick:()->Unit){
    val textValueState = textValue.observeAsState(initial = "")
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
        .height(64.dp)
        .border(
            1.dp, if(isActive) ButtonTextColor else MiniBorder,
            RoundedCornerShape(16.dp)
        )
        .background(AltBG, RoundedCornerShape(16.dp))
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Image(painterResource(id = if(isActive) R.drawable.referrals_active_icon else R.drawable.referrals_inactive_icon),"",modifier = Modifier
            .fillMaxWidth(0.1f)
            .padding(start = 12.dp))

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
        Box(modifier = Modifier.fillMaxWidth(0.7f)){

            BasicTextField(
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
                    .padding(start = 4.dp, end = 4.dp)
                    .background(
                        Color.Transparent
                    ),
                textStyle = TextStyle(
                    fontFamily = itimStyle,
                    fontSize = average_text_size,
                    color = MainTextColor
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .padding(vertical  =4.dp)


                    ) {
                        if(isTextEmpty){
                            Text(if(isActive)"Enter code here" else "You have received a referral", fontSize = average_text_size, color = OffTextColor, fontFamily = itimStyle, textAlign = TextAlign.Start)
                        }

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
        Box(Modifier.padding(end = 12.dp)){
            Image(painterResource(id = if(isActive) R.drawable.get_right_icon else R.drawable.get_right_icon_inactive),"",modifier = Modifier
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
                })

        }
    }
}