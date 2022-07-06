package com.zbyte.nftsdk.model

import com.google.gson.annotations.SerializedName

data class ConnectedAccount(
    @SerializedName("connectedAccount")
    val connectedAccount: Account
)

data class Account(
    @SerializedName("publicKey")
    val publicKey: String,

    @SerializedName("loginOptions")
    val loginOptions: LoginOptions
)

data class LoginOptions(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String
)