package com.zbyte.nftsdk.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Interface to define the API call structure
 *
 * @since 30/06/2022
 * @author Yash Parikh
 */
interface ZByteApi {

    @POST("api/userService")
    suspend fun getUserEmail(
        @Header("Cookie") cookieData: String,
        @Header("action") userProfile: String,
        @Body requestBody: RequestBody
    ): Response<UserDetails>
}