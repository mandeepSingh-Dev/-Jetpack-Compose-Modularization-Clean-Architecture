package com.uae.feature_chat_with_us.domain.mapper

import com.uae.core_common.utils.DateFormats.DATE_FORMAT_5
import com.uae.core_common.utils.localeUtils.convertToDateFormat
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import com.uae.feature_chat_with_us.ui.utils.RoleType
import com.uae.feature_profile.remote.model.response.ProfileResponse
import java.util.Date

fun SendMessageBody?.toMsgData(userDetails: ProfileResponse.UserData?): ChatsListResponse.Data.MsgData {

    return ChatsListResponse.Data.MsgData(
        createdAt = Date().time.convertToDateFormat(toFormat = DATE_FORMAT_5, isUtc = true),
        fileName = this?.fileName,
        id = this?.chatId,
        isRead = null,
        message = this?.message,
        messageType = this?.messageType,
        mimeType = this?.mimeType,
        role = RoleType.USER.type,
        size = this?.size,
        updatedAt = Date().time.convertToDateFormat(toFormat = DATE_FORMAT_5, isUtc = true),
        url = this?.url ?: this?.fileUri,
        user = ChatsListResponse.Data.User(
            _id = userDetails?.userId,
            fullname = userDetails?.fullName,
            profilePic = userDetails?.profilePic
        ),
        userId = null
    )


}