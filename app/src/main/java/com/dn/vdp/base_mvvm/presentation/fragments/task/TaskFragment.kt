package com.dn.vdp.base_mvvm.presentation.fragments.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.data.roomdata.Task
import com.dn.vdp.base_mvvm.databinding.FragmentTaskBinding
import com.dn.vdp.base_mvvm.presentation.adapter.ColorAdapter
import com.dn.vdp.base_mvvm.presentation.adapter.TaskAdapter
import com.dn.vdp.base_mvvm.presentation.adapter.TimeAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


@AndroidEntryPoint
class TaskFragment : BaseFragment<TaskViewModel, FragmentTaskBinding>(R.layout.fragment_task) {
    override val binding by viewBindings(FragmentTaskBinding::bind)
    override val viewModel by viewModels<TaskViewModel>()
    var colorAdapter: ColorAdapter? = null
    var timeAdapter: TimeAdapter? = null
    var taskAdapter: TaskAdapter? = null
    var listColor: ArrayList<Int>? = null
    var listTime: ArrayList<String>? = null
    var color: Int = R.color.color_1
    var mDescription: String = ""
    var mDate: String = ""
    var mTime: String = ""
    var mUuid: String = ""
    var mReason: String = ""
    var mComplete: Int = 0
    var mFromDate: String = ""
    var mToDate: String = ""
    var mHistory: Boolean = false
    var mAdd: Boolean = false
    var mAlarm: Int = 0
    var mSelect: String = "To day"
    var oldTask: Task? = null
    var listSelect: ArrayList<Task> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setupViews() {
        changeStatusBarColor(R.color.white)
        getToday()

        listColor = ArrayList()
        listColor?.add(R.color.color_1)
        listColor?.add(R.color.color_2)
        listColor?.add(R.color.color_3)
        listColor?.add(R.color.color_4)
        listColor?.add(R.color.color_5)

        listTime = ArrayList()
        listTime?.add("To day")
        listTime?.add("Weekend")
        listTime?.add("Month")
        listTime?.add("Select day")


        binding.apply {
            imgHistory.setOnClickListener {
                //findNavController().navigate(AuthNavigationDirections.actionAuthToMain())
            }

            imgBack.setOnClickListener {
                mHistory = false
                imgBack.visibility = View.INVISIBLE
                imgHistory.visibility = View.VISIBLE
                getToday()
            }

            imgHistory.setOnClickListener {
                mHistory = true
                imgBack.visibility = View.VISIBLE
                imgHistory.visibility = View.INVISIBLE
                getToday()
            }

            tvDelete.setOnClickListener {
                showDeleteDialog()
            }

            taskAdapter = TaskAdapter(
                click = {
                    oldTask = it
                    mDescription = it.description!!
                    mDate = it.date!!
                    mTime = it.time!!
                    mUuid = it.id!!
                    color = it.color
                    mReason = it.reason!!
                    mComplete = it.complete
                    mAlarm = it.alarm
                    mAdd = false
                    bottomsheet()
                    colorAdapter!!.setPosition(color)
                },
                done = {
                    mDescription = it.description!!
                    mDate = it.date!!
                    mTime = it.time!!
                    mUuid = it.id!!
                    color = it.color
                    mReason = it.reason!!
                    mComplete = it.complete
                    mAlarm = it.alarm
                    showDialog()
                },
                select = {
                    listSelect = it
                    if (it.size>0)
                        tvDelete.visibility = View.VISIBLE
                    else tvDelete.visibility = View.GONE

                }, requireContext()
            )

            val layout =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvTask.layoutManager = layout
            rvTask.adapter = taskAdapter

            timeAdapter = TimeAdapter(listTime!!, click = {
                mSelect = it
                timeAdapter!!.setPosition(it)
                getSelect()
            })
            val layout2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvTime.layoutManager = layout2
            binding.rvTime.adapter = timeAdapter
        }

        binding.btnAdd.setOnClickListener {
            mAdd = true
            mDescription = ""
            mDate = ""
            mTime = ""
            mUuid = ""
            mComplete = 0
            mAlarm = 0
            color = R.color.color_1
            bottomsheet()
        }
    }

    private fun getSelect() {
        when (mSelect) {
            "To day" -> {
                getToday()
            }

            "Weekend" -> {
                getWeekend()
            }

            "Month" -> {
                getMonth()
            }

            "Select day" -> {
                showSelectDateDialog()

            }
        }
    }

    private fun getToday() {
        val date = getCurrentDateTime()
        val dateInString = date.toString("yyyy/MM/dd")
        mFromDate = dateInString
        mToDate = dateInString
        getTask()
    }

    private fun getTask() {
    //    convertDate()
        Log.e("getTask","${mFromDate} ${mToDate}")
        if (mHistory) viewModel.fetchTaskHistoryDateToDate(mFromDate, mToDate) else
            viewModel.fetchTaskDateToDate(mFromDate, mToDate)
    }


    private fun getWeekend() {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_WEEK] = cal.firstDayOfWeek
        val sdf = SimpleDateFormat("yyyy/MM/dd")

        for (i in 0..6) {
            if (i == 0) {
                mFromDate = sdf.format(cal.time)
            } else if (i == 6) {
                mToDate = sdf.format(cal.time)
            }
            cal.add(Calendar.DAY_OF_WEEK, 1)
        }
        Log.e("getWeekend", "$mFromDate $mToDate")
        getTask()
    }

    private fun getMonth() {
        val cal = Calendar.getInstance()
        cal[Calendar.DAY_OF_MONTH] = cal.firstDayOfWeek
        val sdf = SimpleDateFormat("MM")
        val sdf2 = SimpleDateFormat("yyyy")
        val daysInMonth: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        mFromDate = "${sdf2.format(cal.time)}/${sdf.format(cal.time)}/01"
        mToDate = "${sdf2.format(cal.time)}/${sdf.format(cal.time)}/$daysInMonth"
        Log.e("getMonth","${mFromDate} ${mToDate}")
        getTask()
    }

    private fun convertDate(mDate: String): String {
        val originalFormat = SimpleDateFormat("yyyy/MM/dd")
        val targetFormat = SimpleDateFormat("dd/MM/yyyy")

        val date = originalFormat.parse(mDate)
        return targetFormat.format(date)
    }

    private fun bottomsheet() {
        mUuid = if (mUuid == "") java.util.UUID.randomUUID().toString() else mUuid
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val tv_create = view.findViewById<TextView>(R.id.tv_create)
        val tv_delete = view.findViewById<TextView>(R.id.tv_delete)
        val tv_update = view.findViewById<TextView>(R.id.tv_update)
        val btnClose = view.findViewById<ImageView>(R.id.img_close)
        val img_alarm = view.findViewById<ImageView>(R.id.img_alarm)
        val rv_color = view.findViewById<RecyclerView>(R.id.ll_color)
        val tv_date = view.findViewById<TextView>(R.id.tv_date)
        val tv_time = view.findViewById<TextView>(R.id.tv_time)
        val edit_description = view.findViewById<EditText>(R.id.edit_description)
        val ll_update = view.findViewById<LinearLayout>(R.id.ll_update)

        val date = getCurrentDateTime()
        if (mAdd) {
            ll_update.visibility = View.GONE
            tv_create.visibility = View.VISIBLE
            mDate = date.toString("yyyy/MM/dd")
            mTime = date.toString("HH:mm")
        } else {
            ll_update.visibility = View.VISIBLE
            tv_create.visibility = View.GONE

        }
        colorAdapter = ColorAdapter(listColor!!, click = {
            color = (it)
            colorAdapter!!.setPosition(it)
        }, requireContext())


        tv_date.text = convertDate(mDate)
        tv_time.text = mTime
        edit_description.setText(mDescription)

        if (mAlarm == 1) {
            img_alarm.setImageDrawable(resources.getDrawable(R.drawable.baseline_alarm_on_24))
        } else {
            img_alarm.setImageDrawable(resources.getDrawable(R.drawable.baseline_alarm_off_24))
        }

        img_alarm.setOnClickListener {
            if (mAlarm == 0) {
                mAlarm = 1
                img_alarm.setImageDrawable(resources.getDrawable(R.drawable.baseline_alarm_on_24))
            } else {
                mAlarm = 0
                img_alarm.setImageDrawable(resources.getDrawable(R.drawable.baseline_alarm_off_24))
            }
        }

        //dialog date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                // Display Selected date in textbox
                val mmMonth = mMonth + 1
                val d = if (mDay < 10) "0$mDay" else "$mDay"
                val m = if (mmMonth < 10) "0$mmMonth" else "$mmMonth"


                val date = "$mYear/$m/$d"
                tv_date.text = convertDate(date)
                mDate = date
            },
            year,
            month,
            day
        )
        tv_date.setOnClickListener {
            dpd.show()
        }

        //dialog time
        var mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)

        mTimePicker = TimePickerDialog(
            requireContext(),
            { view, hourOfDay, minute ->
                val mHour = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
                val mMinute = if (minute < 10) "0$minute" else "$minute"
                tv_time.text = "$mHour:$mMinute"
                mTime = "$mHour:$mMinute"
            }, hour, minute, false
        )

        tv_time.setOnClickListener {
            mTimePicker.show()
        }

        val layout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rv_color.layoutManager = layout
        rv_color.adapter = colorAdapter

        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }

        tv_create.setOnClickListener {
            if (edit_description.text.toString() == "") {
                edit_description.error = "Cannot be empty"
            } else {
                val task = Task(
                    mUuid,
                    edit_description.text.toString(),
                    mDate,
                    mTime,
                    color,
                    mReason,
                    mComplete,
                    mAlarm
                )

                val date = mDate.split("/")
                val time = mTime.split(":")
                setAlarm(
                    date[0],
                    date[1],
                    date[2],
                    time[0],
                    time[1],
                    date[0],
                    date[1],
                    date[2],
                    time[0],
                    time[1],
                    mAlarm.toString(),
                    edit_description.text.toString()
                )
                viewModel.insert(task)
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        getSelect()
                    }

                }, 1000)
                dialog.dismiss()
            }
        }
        tv_update.setOnClickListener {
            if (edit_description.text.toString() == "") {
                edit_description.error = "Cannot be empty"
            } else {
                val task = Task(
                    mUuid,
                    edit_description.text.toString(),
                    mDate,
                    mTime,
                    color,
                    mReason,
                    mComplete,
                    mAlarm
                )
                viewModel.update(task)
                val date_old = oldTask!!.date!!.split("/")
                val time_old = oldTask!!.time!!.split(":")

                val date = mDate.split("/")
                val time = mTime.split(":")
                setAlarm(
                    date_old[0],
                    date_old[1],
                    date_old[2],
                    time_old[0],
                    time_old[1],
                    date[0],
                    date[1],
                    date[2],
                    time[0],
                    time[1],
                    mAlarm.toString(),
                    edit_description.text.toString()
                )

                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        getSelect()
                    }

                }, 1000)
                dialog.dismiss()
            }
        }

        tv_delete.setOnClickListener {
            viewModel.delete(oldTask!!)
            val date_old = mDate.split("/")
            val time_old = mTime.split(":")
            cancelAlarm(date_old[0], date_old[1], date_old[2], time_old[0], time_old[1])
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    getSelect()
                }

            }, 1000)
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun setAlarm(
        d_old: String,
        m_old: String,
        y_old: String, hour_old: String, minute_old: String,
        d: String,
        m: String,
        y: String, hour: String, minute: String, mAlarm: String, mDescription: String
    ) {
        val intent = Intent("android.intent.action.START_ALARM")
        val bundle = Bundle()
        bundle.putString("d_old", d_old)
        bundle.putString("m_old", m_old)
        bundle.putString("y_old", y_old)
        bundle.putString("hour_old", hour_old)
        bundle.putString("minute_old", minute_old)

        bundle.putString("d", d)
        bundle.putString("m", m)
        bundle.putString("y", y)
        bundle.putString("hour", hour)
        bundle.putString("minute", minute)
        bundle.putString("mAlarm", mAlarm)
        bundle.putString("description", mDescription)
        intent.putExtras(bundle)
        requireContext().sendBroadcast(intent)
    }

    private fun cancelAlarm(
        d_old: String,
        m_old: String,
        y_old: String, hour_old: String, minute_old: String,
    ) {
        val intent = Intent("android.intent.action.STOP_ALARM")
        val bundle = Bundle()
        bundle.putString("d_old", d_old)
        bundle.putString("m_old", m_old)
        bundle.putString("y_old", y_old)
        bundle.putString("hour_old", hour_old)
        bundle.putString("minute_old", minute_old)

        intent.putExtras(bundle)
        requireContext().sendBroadcast(intent)
    }

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.stateFlowAccount.collect {
                when (it) {
                    is TaskViewModel.Tasks.Empty -> {

                    }

                    is TaskViewModel.Tasks.getTask -> {
                        taskAdapter!!.setData(it.list)
                    }
                }
            }
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun showDialog() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialog_reason, null)

        val reason = view.findViewById(R.id.edit_reason) as EditText

        val tv_done = view.findViewById(R.id.tv_done) as TextView
        val img_close = view.findViewById(R.id.img_close) as ImageView
        tv_done.setOnClickListener {
            if (reason.text.toString() == "") {
                reason.error = "Cannot be empty"
            } else {
                val task = Task(
                    mUuid,
                    mDescription,
                    mDate,
                    mTime,
                    R.color.gray_2,
                    reason.text.toString(),
                    1
                )
                viewModel.update(task)
                val date_old = mDate.split("/")
                val time_old = mTime.split(":")
                cancelAlarm(date_old[0], date_old[1], date_old[2], time_old[0], time_old[1])
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        getToday()
                    }
                }, 1000)

                dialog.dismiss()
            }
        }

        img_close.setOnClickListener {
            dialog.dismiss()
        }

        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun showDeleteDialog() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialog_delete, null)
        val tv_done = view.findViewById(R.id.tv_done) as TextView
        val img_close = view.findViewById(R.id.img_close) as ImageView
        tv_done.setOnClickListener {
            for (i in 0 until listSelect.size){
                viewModel.delete(listSelect[i])
                if (i == listSelect.size - 1) {
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            getSelect()
                        }

                    }, 1000)
                    binding.tvDelete.visibility = View.GONE
                    dialog.dismiss()
                }
            }
        }

        img_close.setOnClickListener {
            dialog.dismiss()
        }

        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    private fun showSelectDateDialog() {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext(), R.style.TransparentDialog)
        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.dialog_select_date, null)
        val tv_done = view.findViewById(R.id.tv_done) as TextView
        val tv_from_date = view.findViewById(R.id.tv_from_date) as TextView
        val tv_to_date = view.findViewById(R.id.tv_to_date) as TextView
        val img_close = view.findViewById(R.id.img_close) as ImageView


        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var selectDate = false

        val date = getCurrentDateTime()
        mFromDate = date.toString("yyyy/MM/dd")
        mToDate = date.toString("yyyy/MM/dd")
        tv_to_date.text = convertDate(mFromDate)
        tv_from_date.text =  convertDate(mToDate)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                // Display Selected date in textbox
                val mmMonth = mMonth + 1
                val d = if (mDay < 10) "0$mDay" else "$mDay"
                val m = if (mmMonth < 10) "0$mmMonth" else "$mmMonth"

                val date = "$mYear/$m/$d"
                if (!selectDate) {
                    mFromDate = date
                    tv_from_date.text = convertDate(date)
                }
                else {
                    mToDate = date
                    tv_to_date.text = convertDate(date)
                }
            },
            year,
            month,
            day
        )

        tv_from_date.setOnClickListener {
            selectDate = false
            dpd.show()
        }

        tv_to_date.setOnClickListener {
            selectDate = true
            dpd.show()
        }

        tv_done.setOnClickListener {
            getTask()
            dialog.dismiss()
        }

        img_close.setOnClickListener {
            dialog.dismiss()
        }

        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    fun changeStatusBarColor(color: Int) {
        val window: Window = activity?.window!!
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //  set status text dark
        var flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.decorView.systemUiVisibility = flags  //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireActivity(), color)
    }
}