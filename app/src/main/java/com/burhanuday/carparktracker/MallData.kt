package com.burhanuday.carparktracker

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

class MallData{

    @SerializedName("data")
    var data:List<MallDataData>?=null

}