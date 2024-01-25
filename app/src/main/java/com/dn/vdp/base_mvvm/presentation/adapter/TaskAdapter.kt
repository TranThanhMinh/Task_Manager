package com.dn.vdp.base_mvvm.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.data.roomdata.Task
import java.text.SimpleDateFormat

class TaskAdapter(
    val click: ((Task) -> Unit)? = null,
    val done: ((Task) -> Unit)? = null,
    val select: ((ArrayList<Task>) -> Unit)? = null,
    val context: Context
) :
    RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {
    var color: Int = R.color.color_1
    var list: ArrayList<Task> = ArrayList()
    var listSelect: ArrayList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.apply {
            if (position == 0) {
                tv_line.visibility = View.INVISIBLE
            } else tv_line.visibility = View.VISIBLE

            if (position == list.size - 1) {
                tv_line_2.visibility = View.INVISIBLE
            } else tv_line_2.visibility = View.VISIBLE


            holder.ll_color.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, list[position].color))
            tv_description.text = list[position].description
            tv_date_time.text = convertDate(list[position].date!!) + " " + list[position].time
            if ((list[position].alarm == 0))
                img_alarm.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_alarm_off_24))
            else img_alarm.setImageDrawable(context.resources.getDrawable(R.drawable.baseline_alarm_on_24))

            if (list[position].reason != "") {
                tv_reason.text = "Note: ${list[position].reason}"
                tv_reason.visibility = View.VISIBLE
                tv_done.visibility = View.GONE

                ll_color.setOnClickListener {
                    if (listSelect.contains(list[position])){
                        listSelect.remove(list[position])
                        holder.ll_color.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context,list[position].color))
                    }else {
                        listSelect.add(list[position])
                        holder.ll_color.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.teal_700))
                    }

                     select!!.invoke(listSelect)
                }

            } else {
                tv_reason.visibility = View.GONE
                tv_done.visibility = View.VISIBLE
                ll_color.setOnClickListener {
                    click!!.invoke(list[position])
                }
            }

            tv_done.setOnClickListener {
                done!!.invoke(list[position])
            }
        }
    }

    fun setData(list: ArrayList<Task>) {
        this.list.removeAll { true }
        this.list = list
        notifyDataSetChanged()
    }

    private fun convertDate(mDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy/MM/dd")
        val targetFormat = SimpleDateFormat("dd/MM/yyyy")

        val date = originalFormat.parse(mDate)
        return targetFormat.format(date)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ll_color = v.findViewById<LinearLayout>(R.id.ll_color)
        val tv_description = v.findViewById<TextView>(R.id.tv_description)
        val tv_date_time = v.findViewById<TextView>(R.id.tv_date_time)
        val tv_line = v.findViewById<TextView>(R.id.tv_line)
        val tv_line_2 = v.findViewById<TextView>(R.id.tv_line_2)
        val tv_done = v.findViewById<TextView>(R.id.tv_done)
        val tv_reason = v.findViewById<TextView>(R.id.tv_reason)
        val img_alarm = v.findViewById<ImageView>(R.id.img_alarm)
    }
}