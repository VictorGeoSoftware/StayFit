package com.victor.health.stayfit.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victor.health.stayfit.MainApplication

/**
 * Created by victorpalmacarrasco on 14/9/18.
 * ${APP_NAME}
 */


val Activity.mainApplication: MainApplication
    get() = application as MainApplication

fun ViewGroup.inflate(layoutRes: Int): View =
        LayoutInflater.from(context).inflate(layoutRes, this, false)

fun Context.getDpFromValue(value: Int): Int =
        Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), this.resources.displayMetrics))