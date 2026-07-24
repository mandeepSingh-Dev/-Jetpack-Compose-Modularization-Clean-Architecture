package com.uae.core_common.utils


object Constants {


    const val SPLASH_DELAY = 2000L
    const val OTP_RESEND_TIME = 59
    const val YOE_SIZE = 60

    object IntentKeys{
        const val `401_UNAUTHORIZE_ACTION` = "401_unauthorize_action"

        const val CHANNEL_NAME = "channel_name"
        const val BOOKING_ID = "booking_id"

        const val CHAT_ID = "chatId"

        const val ACTION_LEAVE_CALL = "action_leave_call"

        const val PERSON_NAME = "person_name"
        const val PERSON_IMAGE = "person_image"
        const val PRODUCT_ID = "product_id"

        const val SUBTOPICS_LIST = "subtopics_list"
        const val TOPIC = "topic"
        const val RESPONSE_ID = "responseID"
        const val PLACE_OF_BIRTH_1 = "place_of_birth_1"
        const val PLACE_OF_BIRTH_2 = "place_of_birth_2"
        const val IS_TWO_PERSON_SUBTOPIC = "is_two_person_subtopic"


    }


    object FileFolders{
        const val POST_IMAGES = "Post images"
        const val IMAGE_FOLDER = "images"
    }


    object NotificationKeys{
        const val SIMPLE_NOTIFICATION_ID = 100
        const val SIMPLE_NOTIFICATION_CHANNEL_ID = "simple_notification_channel_id"
        const val SIMPLE_NOTIFICATION_CHANNEL_NAME = "simple_notification_channel"

        const val CALL_INITIATED_NOTIFICATION_ID = 101
        const val CALL_INITIATED_NOTIFICATION_CHANNEL_ID = "call_initiated_notification_channel_id"
        const val CALL_INITIATED_NOTIFICATION_CHANNEL_NAME = "call_initiated_notification_channel"


        const val ONGOING_VIDEO_CALL_NOTIFICATION_ID = 102
        const val ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_ID = "ongoing_video_call_notification_channel_id"
        const val ONGOING_VIDEO_CALL_NOTIFICATION_CHANNEL_NAME = "ongoing_video_call_notification_channel"
    }

    object LangCode{
        const val english = "en"
        const val hindi = "hi"
    }

    object ExternalAppLinks{
        const val WhatsappLink = "https://whatsapp.com/channel/0029VakBwPe59PwTHkGBHR3K"
        const val InstagramLink = "https://www.instagram.com/myzodiaq/"
        const val FacebookLink = "https://www.facebook.com/profile.php?id=61563525901332"
        const val YoutubeLink = "https://www.youtube.com/@myZODIAQ"
    }

    const val HANGUP_PENDING_INTENT_CODE = 101
    const val OPEN_VIDEO_CALL_ACTIVITY_PENDING_INTENT_CODE = 100



    val USER_SIDE_START_0F_5MIN = "The astrologer is facing a network issue, we request you to please wait on the call for upto 5 mins while the astrologer tries to rejoin the call."
    val ASTROLOGER_SIDE_START_OF_5MIN = "The user is facing a network issue, we request you to please wait on the call for upto 5 mins while the user tries to rejoin the call."

    val USER_SIDE_END_OF_5_Min = "Apologies for the wait, the astrologer is unable to reconnect. You may end the call now, please raise a ticket through the Help Center in app menu if your discussion with the astrologer was not completed."
    val ASTROLOGER_SIDE_END_OF_5_MIN = "Apologies for the wait, the user is unable to reconnect. You may end the call now, you will receive the complete consultation fee for the call.."

    val USER_SIDE_ON_ASTROLOGER_NOT_JOINED_WITHIN_15_MIN = "Apologies for the wait, the user is unavailable right now. You may end the call now, you will recieve the complete consultation fee for the call."
    val ASTROLOGER_SIDE_ON_USER_NOT_JOINED_WITHIN_15_MIN = "Apologies for the wait, the astrologer is unavailable right now. You may end the call now and will be refunded the complete booking amount."

}

object DateFormats{
    const val DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"
    const val DATE_FORMAT_2 = "yyyy-MM-dd"
    const val DATE_FORMAT_3 = "E"
    const val DATE_FORMAT_4 = "dd MMM, yyy"
    const val DATE_FORMAT_5 = "yyyy-MM-dd'T'HH:mm:ss.SSSX"


    const val DATE_FORMAT_6 = "MMM dd, yyyy"
    const val DATE_FORMAT_7 = "MM/dd/yyyy"
    const val DATE_FORMAT_8 = "dd/MM/yyyy"
    const val DATE_FORMAT_9 = "dd MMM, yyyy"
    const val DATE_FORMAT_10 = "dd MMM"
    const val DATE_FORMAT_11 = "dd, MMM yyyy"
    const val DATE_FORMAT_12 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DATE_FORMAT_13 = "EEE MMM dd yyyy"
    const val DATE_FORMAT_14 = "dd-MMM-yyyy"
    const val DATE_FORMAT_15 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z"
    const val DATE_FORMAT_16 = "EEE MMM dd yyyy HH:mm:ss"

    const val DATE_FORMAT_17 = "dd MMM yyyy"
    const val DATE_FORMAT_18 = "EEE MMM dd yyyy h:mm:ss a"
    const val DATE_FORMAT_19 = "MMMM yyyy"
    const val DATE_FORMAT_20 = "dd-MMMM-yyyy"
    const val DATE_FORMAT_21 = "EEEE MMMM dd yyyy"
    const val DATE_FORMAT_22 = "MMMM dd yyyy"
    const val DATE_FORMAT_23 = "EEEE, d MMMM, a h:mm yyyy"

    const val DATE_FORMAT_24 = "EEE, MMM d, h:mm a yyyy"
    const val DATE_FORMAT_25 = "dd MMMM yyyy"
    const val DATE_FORMAT_26 = "d MMMM yyyy"
    const val DATE_FORMAT_27 = "dd MMMM yyyy 'at' hh:mm a"


    const val DATE_FORMAT_YEAR = "YYYY"
    const val DATE_FORMAT_MONTH = "MM"
    const val DATE_FORMAT_MONTH_NAME = "MMM"
    const val DATE_FORMAT_DAY = "dd"
    const val DATE_FORMAT_WEEK = "EEE"


    const val TIME_FORMAT_1 = "hh:mm a"
    const val TIME_FORMAT_2 = "hh:mm:ss a"
    const val TIME_FORMAT_3 = "hh:mm"
    const val TIME_FORMAT_4 = "HH:mm"
    const val DATE_TIME_FORMAT = "yyyy-MMM-dd hh:mm aa"
    const val DATE_TIME_FORMAT_2 = "yyyy-MMM-dd hh:mm"
    const val DATE_TIME_FORMAT_3 = "dd MMM yyyy hh:mm aa"
    const val PM = "pm"
    const val AM = "am"
   }
