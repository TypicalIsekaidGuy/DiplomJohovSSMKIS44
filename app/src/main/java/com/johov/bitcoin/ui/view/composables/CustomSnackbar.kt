package com.johov.bitcoin.ui.view.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johov.bitcoin.MainActivity
import com.johov.bitcoin.ui.theme.MainTextColor
import com.johov.bitcoin.ui.theme.ToastNotSuceded
import com.johov.bitcoin.ui.theme.ToastSucceded
import com.johov.bitcoin.ui.theme.itimStyle

@Composable
fun CustomSnackbar(data: SnackbarData){
    Box(modifier = Modifier
        .background(
            if (MainActivity.hasSucceded == null || MainActivity.hasSucceded == false) ToastNotSuceded else ToastSucceded,
            shape = CircleShape
        )
        .fillMaxHeight(0.1f)
        .fillMaxWidth()){
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center).padding(horizontal = MainActivity.maxWidth?.times(0.07f)?:40.dp)){
            Text(data.visuals.message, fontSize = 16.sp, color = MainTextColor, fontFamily = itimStyle, textAlign = TextAlign.Center, modifier = Modifier.align(
                Alignment.CenterVertically).fillMaxWidth(0.9f))
        }
/*        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.align(Alignment.CenterEnd)){
            Image(painterResource(id = R.drawable.dismiss_snackbar),"",
                modifier = Modifier
                    .size(if(MainActivity.isBiggerThen700dp==true) 20.dp else 16.dp)
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        data.dismiss()
                    })
            Spacer(Modifier.width(16.dp))
        }*/
    }
}