package com.burhanuday.carparktracker

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.row_seat.view.*

class RecyclerAdapter(private val seats: List<Seat>?): RecyclerView.Adapter<RecyclerAdapter.SeatHolder>(){

    companion object {
        var lastCheckedPos = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.SeatHolder {
        val inflatedView = parent.inflate(R.layout.row_seat, false)
        return SeatHolder(inflatedView)
    }

    override fun getItemCount() = seats!!.size

    override fun onBindViewHolder(holder: RecyclerAdapter.SeatHolder, position: Int) {
        val item = seats!![position]
        holder.bindSeat(item, position)
        holder.view.setOnClickListener{
            if (holder.view.iv_empty.visibility == View.VISIBLE){
                lastCheckedPos = position

            }else{

            }
            notifyDataSetChanged()
        }
    }

    class SeatHolder(v: View): RecyclerView.ViewHolder(v){
        var view = v
        private lateinit var seat:Seat

        init {
            view = v
        }

        fun bindSeat(seatbind: Seat, position: Int){
            this.seat = seatbind
            view.tv_spot_name.text = seat.spot_id
            if (seat.isBooked!!){
                view.iv_empty.visibility = View.INVISIBLE
                view.iv_filled.visibility = View.VISIBLE
            }else{
                view.iv_empty.visibility = View.VISIBLE
                view.iv_filled.visibility = View.INVISIBLE
            }
            if (lastCheckedPos == position){
                view.iv_empty.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                view.tv_spot_name.setTextColor(Color.GREEN)
            }else{
                view.iv_empty.clearColorFilter()
                view.tv_spot_name.setTextColor(Color.RED)
            }
        }
    }

}