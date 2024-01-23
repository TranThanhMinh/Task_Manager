package com.dn.vdp.base_mvvm.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_mvvm.R

class ColorAdapter(
    var list: ArrayList<Int>,
    val click: ((Int) -> Unit)? = null,
    val context: Context
) : RecyclerView.Adapter<ColorAdapter.MyViewHolder>() {
    var color: Int = R.color.color_1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //   holder.tv_color.setBackgroundResource(list[position])
        holder.image_color_2.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, list[position]))
        holder.ll_color.setOnClickListener { click!!.invoke(list[position]) }
        if (color == list[position])
            holder.image_color_1.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
        else holder.image_color_1.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_800))
    }

    fun setPosition(color: Int) {
        this.color = color
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ll_color = v.findViewById<RelativeLayout>(R.id.ll_color)
        val image_color_1 = v.findViewById<ImageView>(R.id.image_color_1)
        val image_color_2 = v.findViewById<ImageView>(R.id.image_color_2)
    }
}