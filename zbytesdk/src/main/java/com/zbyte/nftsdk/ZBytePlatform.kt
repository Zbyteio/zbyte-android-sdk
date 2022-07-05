package com.zbyte.nftsdk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import com.zbyte.nftsdk.model.ApiService
import com.zbyte.nftsdk.model.ConnectedAccount
import com.zbyte.nftsdk.model.RequestBody
import com.zbyte.nftsdk.model.ZByteApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Custom WebView class to handle the Zbyte URL with functions
 *
 * @since 09/06/2022
 * @author Yash Parikh
 */
class ZBytePlatform : WebView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val apiService = ApiService.getInstance().create(ZByteApi::class.java)
    private val webURL = BuildConfig.WEB_URL
    private val cookieName = "accessToken"
    private val suffix = "mynft"
    private val suffixTest = "nftService"
    private val fireStoreDB = Firebase.firestore

    //Initializing the WebView on loading the Activity/Fragment
    init {
        loadZByte()
    }

    /**
     * Loading the properties of WebView with URL and custom settings
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadZByte() {
        val view = inflate(context, R.layout.zbyte_layout, this)
        val progress = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorAnim = view.findViewById<LottieAnimationView>(R.id.errorAnim)

        this.apply {
            loadUrl(webURL)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.databaseEnabled = true
            settings.loadWithOverviewMode = true
            settings.userAgentString = APP_NAME
            webViewClient = Client(context, progress, errorAnim)
        }
    }

    /**
     * Custom WebViewClient class to handle the response from the WebView
     *
     * @param context Required Context of Class
     * @param progress Progressbar to show when webpage is loading
     * @param errorAnim Failure Animation View of failed to load webpage
     */
    private inner class Client(
        private val context: Context,
        private val progress: ProgressBar,
        private val errorAnim: LottieAnimationView
    ) : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progress.visibility = View.VISIBLE //Display Loader
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            progress.visibility = View.GONE //Hide Loader
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            //Check for the url after login success
            if (url.toString().endsWith(suffix) || url.toString().endsWith(suffixTest)) {
                view.apply {
                    evaluateJavascript(
                        "javascript:localStorage.getItem('account')"
                    ) {
                        val userID = getUserID(it)
                        fetchEmail(userID, url)
                    }
                }
            }
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url != null) {
                if (request.url.toString().contains("intent://")) {
                    val intent = Intent.parseUri(request.url.toString(), Intent.URI_INTENT_SCHEME)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            //Handling the error for loading the webpage
            if (error?.errorCode == ERROR_HOST_LOOKUP || error?.errorCode == ERROR_UNKNOWN) {
                errorAnim.visibility = View.VISIBLE
                progress.visibility = View.GONE
                view?.loadUrl("about:blank")
            } else {
                errorAnim.visibility = View.GONE
            }
            super.onReceivedError(view, request, error)
        }
    }

    /**
     * Getting UserId from the webpage local storage
     *
     * @param jsonString String JSON from the local storage of key 'account'
     * @return User Id in String format
     */
    private fun getUserID(jsonString: String): String {
        val formattedJSON = jsonString.replace("\\", "")
        val result = formattedJSON.substring(1, formattedJSON.length - 1)
        Log.e("JSON::", result)

        val gson = GsonBuilder().create()
        val account = gson.fromJson(result, ConnectedAccount::class.java)
        Log.e("Received User ID:", "${account.connectedAccount.loginOptions.id}")

        return account.connectedAccount.loginOptions.id.toString()
    }

    /**
     * API call for getting the User email from User Id
     *
     * @param userID String UserId returned from 'getUserID(jsonString: String)' function
     * @param url The loaded URL from the WebView
     *
     * @throws NumberFormatException in case of invalid userId String
     */
    private fun fetchEmail(userID: String, url: String?) {
        try {
            if (userID.isNotEmpty()) {
                val token = getToken(url!!)
                CoroutineScope(Dispatchers.IO).launch {
                    val response = apiService.getUserEmail(
                        "Bearer $token",
                        RequestBody(userID.toInt())
                    )
                    if (response.isSuccessful) {
                        try {
                            Log.e("EMAIL", response.body()?.data?.email!!)
                            val email = response.body()?.data?.email!!
                            isDocumentPresent(email)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        try {
                            val jObjError = JSONObject(
                                response.errorBody()!!.charStream().readText()
                            )
                            Log.e(
                                "ERROR::",
                                jObjError.getJSONObject("error").getString("message")
                            )
                        } catch (e: Exception) {
                            Log.e("ERROR MESSAGE::", e.message!!)
                        }
                    }
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    /**
     * Function to fetch the token from the webpage cookie
     *
     * @param url The loaded URL from the WebView
     * @return String token to access the API to fetch Email Address
     */
    private fun getToken(url: String): String {
        var token = ""
        val cookieManager = CookieManager.getInstance()
        val tokenReceived = cookieManager.getCookie(url)
        val temp = tokenReceived.split(";").toTypedArray()
        for (ar1 in temp) {
            if (ar1.contains(cookieName)) {
                val temp1 = ar1.split("=").toTypedArray()
                token = temp1[1]
                break
            }
        }
        return token
    }

    /**
     * Function to store data to the Firebase Firestore Database
     *
     * @param userEmail Email received after calling 'fetchEmail(userID: String, url: String?)' function
     */
    private fun storeDataToFireStore(userEmail: String) {
        val userInfo = hashMapOf(
            "user_email" to userEmail,
            "fcm_token" to TOKEN,
            "device_type" to BuildConfig.DEVICE_TYPE
        )

        fireStoreDB.collection("users")
            .add(userInfo)
            .addOnSuccessListener { Log.e("DOCUMENT::", "Document ID:: ${it.id}") }
            .addOnFailureListener { Log.e("FAIL::", "Error adding Document", it) }
    }

    /**
     * Function to check for the duplicate entry present in the Firebase Firestore Database before
     * storing the user details the Database
     *
     * @param email Email received after calling 'fetchEmail(userID: String, url: String?)' function
     */
    private fun isDocumentPresent(email: String) {
        fireStoreDB.collection("users")
            .whereEqualTo("user_email", email)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty) {
                    storeDataToFireStore(email)
                } else {
                    for (document in it) {
                        Log.e("TAG", "${document.id} => ${document.data}")
                    }
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "Error getting documents: ", it)
            }
    }
}