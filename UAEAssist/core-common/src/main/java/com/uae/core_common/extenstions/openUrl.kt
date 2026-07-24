package com.uae.core_common.extenstions

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun openDialPad(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL,
        Uri.parse("tel:$phoneNumber")
    ).addFlags(FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}