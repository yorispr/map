package com.deta.digitalteam

import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by YORIS on 7/31/17.
 */
interface Endpoint {
    @GET("deta.php")
    fun getStore() : Call<ArrayList<store>>

    @POST("edit.php/")
    fun editStore(@Body store: store) : Call<JsonElement>

}