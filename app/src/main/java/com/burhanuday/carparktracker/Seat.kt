package com.burhanuday.carparktracker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

class Seat{

    @SerializedName("spot_id")
    var spot_id:String?=null

    @SerializedName("_id")
    var _id:String?=null

    @SerializedName("isBooked")
    var isBooked:Boolean?=null
}