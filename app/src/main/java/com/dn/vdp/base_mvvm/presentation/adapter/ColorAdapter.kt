package com.dn.vdp.base_mvvm.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_mvvm.R

class ColorAdapter(var list: ArrayList<Int>,val click : ((Int)->Unit)?=null) : RecyclerView.Adapter<ColorAdapter.MyViewHolder>() {
    var color:Int =  R.color.purple_200
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_color.setBackgroundResource(list[position])
        holder.ll_color.setOnClickListener {click!!.invoke(list[position])  }
        if (color == list[position])
            holder.ll_color.setBackgroundResource(R.color.red)
        else     holder.ll_color.setBackgroundResource(R.color.teal_800)
    }

    fun setPosition (color:Int){
        this.color = color
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ll_color = v.findViewById<LinearLayout>(R.id.ll_color)
        val tv_color = v.findViewById<TextView>(R.id.tv_color)
    }
}