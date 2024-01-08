package com.dn.vdp.base_mvvm.data.retofit

import com.dn.vdp.base_mvvm.data.dto.person.PersonResponse
import retrofit2.http.GET

interface PersonApi {
    @GET("/api")
    suspend fun getPersonInfo(): PersonResponse
}