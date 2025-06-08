package com.johov.bitcoin.ui.view.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NavigateToButton(text: String = "",modifier: Modifier, navitgateTo: ()->Unit){
    Box(Modifier.fillMaxWidth()){

        InActiveBackground(true,{navitgateTo()},text = text, modifier = Modifier.fillMaxWidth().align(Alignment.Center))
    }
}
