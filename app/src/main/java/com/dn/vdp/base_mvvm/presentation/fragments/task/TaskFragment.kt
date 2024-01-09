package com.dn.vdp.base_mvvm.presentation.fragments.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import androidx.core.view.marginLeft
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vdp.base_module.presentation.BaseFragment
import com.dn.vdp.base_module.utils.clicks
import com.dn.vdp.base_module.utils.viewBindings
import com.dn.vdp.base_mvvm.AuthNavigationDirections
import com.dn.vdp.base_mvvm.R
import com.dn.vdp.base_mvvm.data.roomdata.Task
import com.dn.vdp.base_mvvm.databinding.FragmentTaskBinding
import com.dn.vdp.base_mvvm.presentation.adapter.ColorAdapter
import com.dn.vdp.base_mvvm.presentation.adapter.TaskAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import javax.xml.datatype.DatatypeConstants.MONTHS

@AndroidEntryPoint
class TaskFragment : BaseFragment<TaskViewModel, FragmentTaskBinding>(R.layout.fragment_task) {
    override val binding by viewBindings(FragmentTaskBinding::bind)
    override val viewModel by viewModels<TaskViewModel>()
    var colorAdapter: ColorAdapter? = null
    var taskAdapter: TaskAdapter? = null
    var listColor: ArrayList<Int>? = null
    var color: Int = R.color.purple_200
    var mDescription: String = ""
    var mDate: String = ""
    var mTime: String = ""
    var mUuid: String = ""
    var mAdd: Boolean = false

    override fun setupViews() {
//        binding.loginRegisterBtn.clicks {
//            findNavController().navigate(AuthNavigationDirections.actionAuthToMain())
//        }
        viewModel.fetchTask()

        val layout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            taskAdapter = TaskAdapter(click = {
                mDescription = it.description!!
                mDate = it.date!!
                mTime = it.time!!
                mUuid = it.id!!
                color = it.color
                mAdd = false
                bottomsheet()
                colorAdapter!!.setPosition(color)
            })
            rvTask.layoutManager = layout
            rvTask.adapter = taskAdapter
        }

        listColor = ArrayList()
        listColor?.add(R.color.purple_200)
        listColor?.add(R.color.teal_200)
        listColor?.add(R.color.teal_700)
        listColor?.add(R.color.purple_500)
        listColor?.add(R.color.purple_700)

        binding.btnAdd.setOnClickListener {
            mAdd = true
            mDescription = ""
            mDate = ""
            mTime = ""
            mUuid = ""
            color = R.color.purple_200
            bottomsheet()
        }
    }

    fun bottomsheet() {
        mUuid = if (mUuid == "") java.util.UUID.randomUUID().toString() else mUuid
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(requireContext())

        // on below line we are inflating a layout file which we have created.
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val tv_create = view.findViewById<TextView>(R.id.tv_create)
        val btnClose = view.findViewById<ImageView>(R.id.img_close)
        val rv_color = view.findViewById<RecyclerView>(R.id.ll_color)
        val tv_date = view.findViewById<TextView>(R.id.tv_date)
        val tv_time = view.findViewById<TextView>(R.id.tv_time)
        val edit_description = view.findViewById<EditText>(R.id.edit_description)

        colorAdapter = ColorAdapter(listColor!!, click = {
            color = (it)
            colorAdapter!!.setPosition(it)
        })

        val date = getCurrentDateTime()
        val dateInString = date.toString("dd/MM/yyyy")
        val timeInString = date.toString("HH:mm")

        tv_date.text = if (mDate == "") dateInString else mDate
        tv_time.text = if (mTime == "") timeInString else mTime
        edit_description.setText(mDescription)

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


                val date = "$d/$m/$mYear"
                tv_date.text = date
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
                var h = if (hourOfDay < 10) "0$hourOfDay" else "$hourOfDay"
                var m = if (minute < 10) "0$minute" else "$minute"
                tv_time.text = "$h:$m"
                mTime = "$h:$m"
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
            val task = Task(
                mUuid,
                edit_description.text.toString(),
                tv_date.text.toString(),
                tv_time.text.toString(),
                color
            )
            if (!mAdd) viewModel.update(task)
            else viewModel.insert(task)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    viewModel.fetchTask()
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

    override fun bindViewModel() {
        lifecycleScope.launch {
            viewModel.stateFlowAccount.collect {
                when (it) {
                    is TaskViewModel.Tasks.Empty -> {

                    }

                    is TaskViewModel.Tasks.getTask -> {
                        taskAdapter!!.setData(it.list)
//                        for( i in 0 until  it.list.size)
//                        {
//                            Log.d("getTask",it.list[i].toString())
//                        }
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

}