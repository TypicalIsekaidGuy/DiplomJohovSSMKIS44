package com.johov.bitcoin.utils;

import static kotlin.random.RandomKt.Random;

import android.util.Log;

public class IntegerUtils {

    public static Integer generateRandomInteger(){
        Log.d(TagUtils.MAINVIEWMODELTAG,"Worked main");

        return Random(System.nanoTime()).nextInt();
    }
}
