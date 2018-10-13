package com.burhanuday.carparktracker

class Seat(){
    var isBooked:Boolean?=null
    var name:String?=null
    var timestamp:String?=null

    constructor(isBooked:Boolean, name:String, timestamp:String) : this() {
        this.isBooked = isBooked
        this.name = name
        this.timestamp = timestamp
    }
}