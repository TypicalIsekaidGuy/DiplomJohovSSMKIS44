package com.johov.bitcoin.utils;

import androidx.lifecycle.MutableLiveData;

public class MethodUtils {
    public static <T> void safeSetValue(MutableLiveData<T> liveData, T value) {
        try {
            liveData.setValue(value);
        } catch (Exception e) {
            liveData.postValue(value);
        }
    }
}
