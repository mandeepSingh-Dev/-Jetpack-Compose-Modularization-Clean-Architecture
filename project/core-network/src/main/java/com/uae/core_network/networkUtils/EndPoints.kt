package com.uae.core_network.networkUtils


object EndPoints{

    object Auth{

        const val LOGIN = "auth/login"
        const val VERIFY_OTP = "auth/otp/verify"
        const val PROFILE = "auth/profile"
        const val ASTROLOGER_SIGNUP = "auth/signup"
         const val ASTRO_PROFILE = "auth/astrologer/profile"
        const val LOGOUT = "auth/logout"
        const val AGORA_TOKEN = "auth/agora/token"
        const val UPDATE_FCM_TOKEN = "auth/fcm/token"
        const val NOTIFICATION_SETTINGS = "auth/notification/setting"
        const val APPLY_REFERRAL = "auth/referral/apply"
        const val UPDATE_REFERRAL = "auth/referral-code/status"

    }

    object Assistance{
        const val ASSISTANCE = "user/assistance"
        const val LAST_ACCEPTED = "user/assistance/last-accepted"
    }

    object Home{
        const val CATEGORY = "user/category"
        const val SUB_CATEGORY = "user/subcategory"
    }

    object Contact{
        const val CONTACT = "user/contact"
    }
    object Rating{
        const val RATING = "user/review"
    }
    object Faq{
        const val FAQ = "faq"
    }
    object Notification{
        const val NOTIFICATION = "user/notification"
    }
    object ZodiaQAstrology{
        const val KUNDALI = "astrologers/kundali"
        const val USERS_DETAILS = "users/details"
        const val CHART_IMAGE = "users/details"
        const val SOOKSHMA_DASHA = "astrologers/sukshama-dasha"
        const val CLIENTS = "client"
//        =============
        const val PANCHANG = "astrologers/panchang"
//        =============

        const val GEO_SEARCH = "astrologers/geo-search"
        const val KUNDALI_MILAN = "astrologers/kundali-milan"

//        ===========Patrika============
        const val PATRIKA_AI = "astrologers/aipatrika"
        const val ASTROLOGY_ORCHESTRATOR = "astrologers/astrology-orchestrator"
        const val YOGA_LIST = "astrologers/yoga-list"
        const val DOSHA_LIST = "astrologers/dosha-list"
        const val REMEDIES_GEMS = "astrologers/remedies-gems"
        const val BASIC_PROFILE = "astrologers/basic-profile"
        const val RAAHU_N_GULIK_KAAL = "astrologers/advanced-panchang"
        const val HORA_MUHURTA = "astrologers/hora-muhurta"
        const val CHOGHADIYA_MUHURTA = "astrologers/choghadiya-muhurta"
//        ==================
    }

    object Calculator{

        object AstrologyCalculators {
            //Astrology Calculators:
            const val MOON_SIGN = "astrologers/moon-sign"
            const val SUN_SIGN = "astrologers/sun-sign"
            const val LAGNA_SIGN = "astrologers/ascendant-sign"
            const val EXTENDED_KUNDALI_DETAILS = "astrologers/extended-kundali-details"
            const val DAILY_NAKSHATRA = "astrologers/daily-nakshatra"
            const val MOLE_ANALYSIS = "astrologers/mole"
            const val LOVE_COMPATIBILITY = "astrologers/love-compatibility"
            const val FRIENDSHIP_REPORT = "astrologers/friendship-report"
            const val BABY_NAME_SUGGESTIONS = "astrologers/baby-name-suggestions"
            const val RUDRAKSHA_RECOMMENDATION = "astrologers/rudraksha-recommendation"
            const val GEMSTONE_RECOMMENDATION = "astrologers/gemstone-recommendation"
            const val PITRA_DOSHA = "astrologers/pitra-dosha"
        }

        object NumerologyCalculators{
            const val PERSONALITY_NUMBER = "astrologers/personality-number"
            const val EXPRESSION_NUMBER = "astrologers/expression-number"
            const val SOUL_URGE_NUMBER = "astrologers/soul-urge-number"
            const val LIFE_PATH_NUMBER = "astrologers/life-path-number"
            const val DESTINY_NUMBER = "numerology/destiny-number"
            const val CAREER_NUMBER = "numerology/career-number"
            const val KARMIC_DEBT_NUMBER = "numerology/karmic-debt"
            const val MATURITY_NUMBER = "numerology/maturity-number"
            const val ATTITUDE_NUMBER = "numerology/attitude-number"
            const val CHALLENGE_NUMBER = "astrologers/challenge-number"


        }

        const val ALL_BODY_PARTS = "mole-calculator/body-parts"
        const val SUB_BODY_PARTS = "mole-calculator/sub-parts"
    }

    object JyotisReport{
        const val KAAL_SARP_DOSHA = "astrologers/kaalsarp-dosha"
        const val SADE_SATI = "astrologers/sade-sati"
        const val MANGAL_DOSHA = "astrologers/mangal-dosha"
        const val LAAL_KITAAB_REMEDIES = "astrologers/laal-kitaab-remedies"
        const val VIMSHOTTARI_DOSHA = "astrologers/vimshottari-dasha"
        const val RAJ_YOGA = "astrologers/raj-yoga"
        const val LAAL_KITAB_DEBTS = "astrologers/laal-kitaab-debts"
        const val PUJA_SUGGESTION_V2 = "astrologers/puja-suggestion-v2"
    }
    object Vedic{
        const val CHART_IMAGE = "horoscope/chart-image"
        const val ASHTAKVARGA_CHART_IMAGE = "horoscope/ashtakvarga-chart-image"
        const val PANCHANG_HORA_MUHURTA = "panchang/hora-muhurta"
        const val PANCHANG_CHOGHADIYA_MUHURTA = "panchang/choghadiya-muhurta"
    }
    object Astrology_Api{
        const val HORO_CHART_IMAGE = "horo_chart_image/${ChartsApiConstants.chartID}"
        }

    object Common{
        const val BLOOD_GROUPS = "bloodgroup"
        const val MEDICAL_CONDITIONS = "medical-condition"
        const val IMAGE_UPLOAD = "upload"
        const val FCM_TOKEN = "user/fcmToken"
        const val CMS = "cms"
    }
    object Astrologer{
        const val APPLY_CONSULTING = "auth/astrologer/apply/consulting"

        const val TIME_SLOT_FORMATS = "manage/slots/format"
        const val HOME_PAGE = "astrologer/homepage"
        const val SELECTED_SLOTS = "manage/slots/selected"
        const val UPDATE_SLOTS = "manage/slots"
        const val VIEW_SLOTS = "manage/slots/view"
        const val BANK_DETAILS = "auth/bank/details"
        const val ASTROLOGER_REVIEW_USAGE_RATINGS = "booking/review/usage/astrologer"

    }

    object Analytics{
        const val ANALYTICS_COUNT = "analytics/count"
    }
    object Pdf{
        const val GENERATE_PATRIKA_PDF = "client/generate-patrika-pdf"
        const val GENERATE_KUNDALI_PDF = "client/generate-kundli-pdf"
        const val GENERATE_KUNDALI_MATCH_1TO1_PDF = "client/generate-kundali-matching-pdf"
        const val GENERATE_KUNDALI_MATCH_MULTI_USER_PDF = "client/generate-multiple-user-kundali-matching-pdf"
    }
    object User{
        const val HOME_PAGE = "user/homepage"
        const val PERSONALIZED_ASTROLOGERS = "user/homepage/recommended-astrologers"
        const val ASTROLOGER_REVIEWS = "booking/review/astrologer"

    }


    object CMS{
        const val FAQS = "manage/cms/faq"
        const val CMS = "manage/cms"
    }

    object ContactUs{
        const val TOPICS = "manage/cms/contact/us/topics"
        const val CONTACT_US = "manage/cms/contact/us"
        const val CONTACT_US_USER_LIST = "manage/cms/user/contact/us"
        const val REPLY_MESSAGE_CONTACT_US = "manage/cms/reply/contact/us"
        const val CLOSE_TICKET = "manage/cms/user/close/contact/us"
    }

    object AskZodiaQ{
        const val ASK_ZODIAQ_QUERY = "ask"
    }
    object ZodiaQHoroscope{
        const val DAILY_HOROSCOPE = "{lang}/horoscope/daily/{sign}/{day}"
        const val WEEKLY_HOROSCOPE = "{lang}/horoscope/weekly/{sign}/{week}"
        const val MONTHLY_HOROSCOPE = "{lang}/horoscope/monthly/{sign}/{month}"
        const val YEARLY_HOROSCOPE = "{lang}/horoscope/yearly/{sign}/{year}"
    }
    object PersonalizedHoroscope{
        const val horoscope = "horoscope/{id}"
    }

    object Muhurat{
        const val MUHURAT = "muhurat"
    }

    object HelpSupport {
        const val SUPPORT = "user/support"
        const val CHAT_MESSAGES = "user/support/message"
    }
}

enum class ApiWithoutTokens(val value : String){
    FAQS(value = EndPoints.CMS.FAQS)
}