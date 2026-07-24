package com.uae.feature_chat_with_us.ui.utils.extensions

import com.uae.feature_chat_with_us.ui.utils.MessageType


fun String?.getMessageTypeFromMimeType(): MessageType{

    val mimeType = this
    if (mimeType.isNullOrBlank()) return MessageType.FILE



    return when {

        mimeType.startsWith("image/") -> MessageType.IMAGE

        mimeType.startsWith("video/") -> MessageType.VIDEO

        mimeType.startsWith("audio/") -> MessageType.AUDIO



        mimeType == "application/pdf" ||

                mimeType == "application/msword" ||

                mimeType == "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ||

                mimeType == "application/vnd.ms-excel" ||

                mimeType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ||

                mimeType == "application/vnd.ms-powerpoint" ||

                mimeType == "application/vnd.openxmlformats-officedocument.presentationml.presentation" ||

                mimeType == "text/plain" -> MessageType.DOCUMENT



        else -> MessageType.FILE

    }

}