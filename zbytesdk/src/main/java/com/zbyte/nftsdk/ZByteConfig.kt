package com.zbyte.nftsdk

object ZByteConfig {

    fun getCurrentUrl(): String {
        return CUSTOM_URL.ifEmpty {
            if (IS_TEST) BuildConfig.WEB_URL_TEST else BuildConfig.WEB_URL_PROD
        }
    }

    fun getCurrentAPIUrl(): String {
        return CUSTOM_API_URL.ifEmpty { if (IS_TEST) BuildConfig.API_URL_TEST else BuildConfig.API_URL_PROD }
    }
}