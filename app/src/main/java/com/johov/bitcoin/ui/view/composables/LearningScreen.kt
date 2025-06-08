package com.johov.bitcoin.ui.view.composables

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import com.johov.bitcoin.ui.view.screen.LearningScreen1
import com.johov.bitcoin.ui.view.screen.LearningScreen2
import com.johov.bitcoin.ui.view.screen.LearningScreen3

@Composable
fun LearningScreen(pageState: Int, navigateTo1: ()->Unit, learningScreen: MutableLiveData<String>, navigateTo2: ()->Unit, navigateTo3: () -> Unit){
    if(pageState==0){
        LearningScreen1(navigateTo1)
    }
    else if(pageState==1){
        LearningScreen2(learningText = learningScreen,navigateTo2)
    }
    else if(pageState==2){
        LearningScreen3(navigateTo3)
    }
}