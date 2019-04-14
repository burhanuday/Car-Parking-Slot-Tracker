package com.burhanuday.carparktracker

import com.google.gson.annotations.SerializedName

/**
 * Created by burhanuday on 14-04-2019.
 */
class UserData {
    @SerializedName("email")
    var email:String? = null

    @SerializedName("name")
    var name:String? = null

    @SerializedName("wallet_amount")
    var wallet:Int? = null


}