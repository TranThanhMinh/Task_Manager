package com.dn.vdp.base_mvvm.data.roomdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(@PrimaryKey val id:String,
                @ColumnInfo(name = "description") val description:String?,
                @ColumnInfo(name = "date") val date:String?,
                @ColumnInfo(name = "time") val time:String?,
                @ColumnInfo(name = "color") val color:Int,
                @ColumnInfo(name = "reason") val reason:String?,
                @ColumnInfo(name = "complete") val complete:Int,
                @ColumnInfo(name = "alarm") val alarm:Int = 0,
) {
}