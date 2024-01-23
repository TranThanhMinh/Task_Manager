package com.dn.vdp.base_mvvm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_mvvm.R

class TimeAdapter(var list: ArrayList<String>, val click : ((String)->Unit)?=null) : RecyclerView.Adapter<TimeAdapter.MyViewHolder>() {
    var mSelect:String =  "To day"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_time, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.apply {
            if (mSelect == list[position])
                tv_time.setBackgroundResource(R.drawable.border_time)
            else    tv_time.setBackgroundResource(R.drawable.border_time_2)
            tv_time.text = list[position]
            holder.ll_time.setOnClickListener {click!!.invoke(list[position])  }
        }

    }

    fun setPosition (mSelect:String){
        this.mSelect = mSelect
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv_time = v.findViewById<TextView>(R.id.tv_time)
        val ll_time = v.findViewById<LinearLayout>(R.id.ll_time)
    }
}