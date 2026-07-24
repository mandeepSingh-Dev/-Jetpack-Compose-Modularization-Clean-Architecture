package com.uae.feature_home.utils

enum class AssistanceType(val status : Int){
        PENDING(0),
        ACCEPTED(1),
        RESOLVED(2),
        CANCELLED(3);

    companion object {
        fun default() = PENDING
    }
}