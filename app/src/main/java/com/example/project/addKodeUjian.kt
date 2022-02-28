package com.example.project

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isInvisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addKodeUjian.newInstance] factory method to
 * create an instance of this fragment.
 */
class addKodeUjian : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth;
    var kodeujian = ""
    private lateinit var _tvKodeSoal: TextView
    private lateinit var _tvDateMulai: TextView
    private lateinit var _tvDateSelesai: TextView
    private lateinit var _etNamaSoal: EditText
    private lateinit var _btnBack: AppCompatButton
    private lateinit var _btnBuatUjian: AppCompatButton
    private lateinit var _btnRefresh: ImageButton
    private lateinit var _btnDateMulai: ImageButton
    private lateinit var _btnDateSelesai: ImageButton

    var pickmulai = true

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var SMulaiday = 0
    var SMulaimonth = 0
    var SMulaiyear = 0
    var SMulaihour = ""
    var SMulaiminute = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun getUjian() {
        db.collection("tbUjian").get()
            .addOnSuccessListener { result ->
                for (ujian in result) {
                    if (ujian.data.get("kode").toString() == kodeujian) {
                        _tvKodeSoal.setText(ujian.data.get("kode").toString())
                        _btnRefresh.visibility = View.INVISIBLE
                        _etNamaSoal.setText(ujian.data.get("nama").toString())
                        _btnBuatUjian.setText("Edit Ujian")
                        _tvDateMulai.setText(ujian.data.get("tanggalmulai").toString())
                        _tvDateSelesai.setText(ujian.data.get("tanggalselesai").toString())
                    }
                }
            }
    }
    fun checkDigit(number: Int): String? {
        return if (number <= 9) "0$number" else number.toString()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        val uid = auth.uid.toString()


        _tvKodeSoal = view.findViewById(R.id.tvKodeSoal)
        _etNamaSoal = view.findViewById(R.id.etNamaSoal)
        _btnBack = view.findViewById(R.id.btnBack)
        _btnRefresh = view.findViewById(R.id.btnRefresh)
        _btnBuatUjian = view.findViewById(R.id.btnBuatUjian)
        _btnDateMulai = view.findViewById(R.id.btnDateMulai)
        _btnDateSelesai = view.findViewById(R.id.btnDateSelesai)
        _tvDateMulai = view.findViewById(R.id.tvDateMulai)
        _tvDateSelesai = view.findViewById(R.id.tvDateSelesai)
        pickDate()


        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomKode = (1..10)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
        _tvKodeSoal.setText(randomKode)

        if (arguments != null) {
            var kode = arguments?.getString("kodeujian").toString()
            _tvKodeSoal.setText("Kode Ujian :" + kode)
            _btnRefresh.visibility = View.INVISIBLE
            if (kode != null) {
                kodeujian = kode
            }
            getUjian()
        }

        _btnRefresh.setOnClickListener {
            val randomKode = (1..10)
                .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("");
            _tvKodeSoal.setText(randomKode)
        }

        _btnBuatUjian.setOnClickListener {

            if(_tvDateMulai.text.toString()!="null"&&_tvDateSelesai.text.toString()!="null"){
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                val datemulai = LocalDateTime.parse(_tvDateMulai.text.toString(), formatter);
                val dateselesai = LocalDateTime.parse(_tvDateSelesai.text.toString(), formatter);

                if (dateselesai.isAfter(datemulai) || datemulai == dateselesai) {
                    if (!_etNamaSoal.text.isEmpty()) {
                        var kode = _tvKodeSoal.text.toString()
                        val ujian = ujianclass(
                            kode,
                            _etNamaSoal.text.toString(),
                            uid,
                            _tvDateMulai.text.toString(),
                            _tvDateSelesai.text.toString()
                        )
                        db.collection("tbUjian").document(kode)
                            .set(ujian)
                            .addOnSuccessListener {
                                Toast.makeText(activity, "Ujian Berhasil Dibuat", Toast.LENGTH_SHORT)
                                    .show()
                                val mBundle = Bundle()
                                mBundle.putString("kodeujian", kode)

                                val mBuatUjian = pembuatanUjian()
                                mBuatUjian.arguments = mBundle

                                val mFragmentManager = parentFragmentManager
                                mFragmentManager.beginTransaction().apply {
                                    replace(
                                        R.id.frameGuruContainer,
                                        mBuatUjian,
                                        pembuatanUjian::class.java.simpleName
                                    )
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                    } else {
                        _etNamaSoal.error = "Nama Ujian Tidak Boleh Kosong!"
                    }
                } else {
                    Toast.makeText(
                        activity,
                        "Tanggal / Jam Selesai Ujian tidak boleh\nkurang dari jam mulai!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            else{
                Toast.makeText(
                    activity,
                    "Tanggal / Jam tidak boleh kosong!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        _btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_kode_ujian, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment addKodeUjian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            addKodeUjian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun pickDate() {
        getDateTimeCalendar()
        var tempday=""
        var tempmonth=""
        if(day<10){
            tempday="0$day"
        }
        if(month<10){
            tempmonth="0${SMulaimonth+1}"
        }
        _tvDateMulai.setText("$tempday-$tempmonth-$year $hour:$minute")
        _tvDateSelesai.setText("$tempday-$tempmonth-$year $hour:$minute")

        _btnDateMulai.setOnClickListener {
            pickmulai = true
            DatePickerDialog(requireActivity(), this, year, month, day).show()
        }
        _btnDateSelesai.setOnClickListener {
            pickmulai = false
            DatePickerDialog(requireActivity(), this, year, month, day).show()
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
//        if(pickmulai){
//            SMulaiday=p3
//            SMulaimonth=p2+1
//            SMulaiyear=p1
//        }

        SMulaiday = p3
        SMulaimonth = p2 + 1
        SMulaiyear = p1

        getDateTimeCalendar()
        TimePickerDialog(requireActivity(), this, hour, minute, true).show()

    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
//        SMulaihour = p1.toString()
        SMulaihour=String.format("%02d", p1)
//        SMulaiminute = p2.toString()
        SMulaiminute=String.format("%02d", p2)

        var tempday=""
        var tempmonth=""
        if(SMulaiday<10){
            tempday="0$SMulaiday"
        }
        if(SMulaimonth<10){
            tempmonth="0$SMulaimonth"
        }

        if (pickmulai) {
            _tvDateMulai.setText("$tempday-$tempmonth-$SMulaiyear $SMulaihour:$SMulaiminute")
        } else {
            _tvDateSelesai.setText("$tempday-$tempmonth-$SMulaiyear $SMulaihour:$SMulaiminute")
        }
    }
}