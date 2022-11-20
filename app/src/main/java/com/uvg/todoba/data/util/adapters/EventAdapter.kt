package com.uvg.todoba.data.util.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.uvg.todoba.R
import com.uvg.todoba.data.local.entity.Event

class EventAdapter (
    private val dataSet: MutableList<Event>,
    private val eventListener: EventListener
        ): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    interface EventListener {
        fun onPlaceClicked(event: Event, position: Int)
    }


    class ViewHolder(
        private val view: View,
        private val listener: EventListener
    ): RecyclerView.ViewHolder(view){

        private val layoutEvent: ConstraintLayout = view.findViewById(R.id.layout_itemPlace)
        private val eventTitle: TextView = view.findViewById(R.id.textView_recyclerPlace_name)
        private val eventCategory: TextView = view.findViewById(R.id.textView_recyclerPlace_Status)
        private val eventDate: TextView = view.findViewById(R.id.textView_recyclerPlace_fecha)
        private val eventTime: TextView = view.findViewById(R.id.textView_recyclerPlace_hora)

        fun setData(event: Event){
            eventTitle.text = event.title
            eventCategory.text = event.category
            eventDate.text = event.date
            eventTime.text = event.time
            layoutEvent.setOnClickListener {
                listener.onPlaceClicked(event, this.adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_place, parent, false)
        return ViewHolder(view, eventListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}