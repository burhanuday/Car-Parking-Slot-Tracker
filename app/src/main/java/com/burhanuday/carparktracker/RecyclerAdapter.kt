package com.burhanuday.carparktracker

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_seat.view.*

class RecyclerAdapter(private val seats: List<Seat>?): RecyclerView.Adapter<RecyclerAdapter.SeatHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.SeatHolder {
        val inflatedView = parent.inflate(R.layout.row_seat, false)
        return SeatHolder(inflatedView)
    }

    override fun getItemCount() = seats!!.size

    override fun onBindViewHolder(holder: RecyclerAdapter.SeatHolder, position: Int) {
        val item = seats!![position]
        holder.bindSeat(item)
    }

    class SeatHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener{
        private var view = v
        private lateinit var seat:Seat

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (view.iv_empty.visibility == View.VISIBLE){
                //executed when not booked->booked
                view.iv_empty.visibility = View.INVISIBLE
                view.iv_filled.visibility = View.VISIBLE
                //seat!!.booked = true

            }else{
                //executed when booked->not booked
                view.iv_empty.visibility = View.VISIBLE
                view.iv_filled.visibility = View.INVISIBLE
                //seat!!.booked = false
            }
        }

        companion object {
            private val SEAT_KEY = "SEAT"
        }

        fun bindSeat(seatbind: Seat){
            this.seat = seatbind
            view.tv_spot_name.text = seat.spot_id
            if (seat.isBooked!!){
                view.iv_empty.visibility = View.INVISIBLE
                view.iv_filled.visibility = View.VISIBLE
            }
            /*
            if (this.seat!!.booked){
                view.iv_empty.visibility = View.INVISIBLE
                view.iv_filled.visibility = View.VISIBLE
            }
            */
        }
    }

}