package com.deta.digitalteam

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by YORIS on 8/5/17.
 */
class store : Serializable{

    @SerializedName("store_id")
    var store_id = 0

    @SerializedName("store_name")
    var store_name = ""

    @SerializedName("latitude")
    var latitude  = 0.0

    @SerializedName("longitude")
    var longitude = 0.0

    constructor() {}

    constructor(store_id: Int, store_name: String, latitude: Double, longitude: Double) {
        this.store_id = store_id
        this.store_name = store_name
        this.latitude = latitude
        this.longitude = longitude
    }
}