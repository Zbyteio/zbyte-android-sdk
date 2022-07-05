package com.zbyte.nftsdk.model

import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("status")
    val status: Boolean? = false,

    @SerializedName("data")
    val data: UserData?
)

data class UserData(
    @SerializedName("userId")
    val userId: Int?,

    @SerializedName("firstName")
    val firstName: String?,

    @SerializedName("lastName")
    val lastName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("userRole")
    val userRole: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("designation")
    val designation: String?
)

data class RequestBody(
    val userId: Int?
)
