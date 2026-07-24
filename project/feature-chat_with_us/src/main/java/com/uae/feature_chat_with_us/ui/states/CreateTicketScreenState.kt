package com.uae.feature_chat_with_us.ui.states

import com.uae.core_common.UIState
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody

data class CreateTicketScreenState(
    val isLoading : Boolean = false,
    val createTickerRequestBody: CreateTickerRequestBody = CreateTickerRequestBody(),
) : UIState