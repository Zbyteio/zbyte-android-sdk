package com.zbyte.nftsdk

import android.content.Context
import android.net.Uri
import androidx.appcompat.content.res.AppCompatResources
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsIntent.COLOR_SCHEME_DARK
import androidx.core.graphics.drawable.toBitmap

private class ZByteTab {

    private val webURL = BuildConfig.WEB_URL

    fun openCustomTab(context: Context, toolbarColor: Int) {
        val builder = CustomTabsIntent.Builder()
        val defaultColor = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor)
            .build()
        builder.setColorSchemeParams(COLOR_SCHEME_DARK, defaultColor)
        AppCompatResources.getDrawable(context, R.drawable.ic_back)?.let {
            builder.setCloseButtonIcon(it.toBitmap())
        }
        builder.setShareState(CustomTabsIntent.SHARE_STATE_OFF)
        val intent = builder.build()
        intent.launchUrl(context, Uri.parse(webURL))
    }
}