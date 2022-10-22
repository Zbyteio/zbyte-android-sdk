package com.zbyte.nftsdk

object ZByteConfig {

    /**
     * Function to get the current selected URL for WebView
     *
     * @return String web url to be opened in the WebView
     */
    fun getCurrentUrl(): String {
        return CUSTOM_URL.ifEmpty {
            if (IS_TEST) BuildConfig.WEB_URL_TEST else if (IS_DEV) BuildConfig.WEB_URL_DEV else BuildConfig.WEB_URL_PROD
        }
    }

    /**
     * Function to get the current selected API URL
     *
     * @return String API URL to use for calling the API in the SDK
     */
    fun getCurrentAPIUrl(): String {
        return CUSTOM_API_URL.ifEmpty {
            if (IS_TEST) BuildConfig.API_URL_TEST else if (IS_DEV) BuildConfig.API_URL_DEV else BuildConfig.API_URL_PROD
        }
    }
}