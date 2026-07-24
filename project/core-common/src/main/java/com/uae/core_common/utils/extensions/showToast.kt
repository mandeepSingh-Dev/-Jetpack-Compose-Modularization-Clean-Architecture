package com.uae.core_common.utils.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(message : String?, length : Int = Toast.LENGTH_LONG){
    if(message.isNullOrEmpty()) return
    Toast.makeText(this,message, length).show()
}

