package com.uae.core_common

import com.uae.core_common.utils.Constants
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeepLinkingHandler @Inject constructor(@ApplicationContext val context : Context){

    fun isFromUnAuthorizeAction(intent : Intent) : Boolean{
        val isUnauthorize = intent.getBooleanExtra(Constants.IntentKeys.`401_UNAUTHORIZE_ACTION`,false)
        intent.removeExtra(Constants.IntentKeys.`401_UNAUTHORIZE_ACTION`)
        return isUnauthorize
    }

    fun navigateToMainActivity(){
//        val intent = Intent(context, MainActivity::class.java)
//        intent.putExtra(Constants.IntentKeys.`401_UNAUTHORIZE_ACTION`, true)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(intent)
    }

}

