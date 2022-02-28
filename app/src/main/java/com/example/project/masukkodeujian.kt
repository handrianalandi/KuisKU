package com.example.project

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [masukkodeujian.newInstance] factory method to
 * create an instance of this fragment.
 */
class masukkodeujian : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth;


    private lateinit var _tvKode: TextView
    private lateinit var _tvNama: TextView
    private lateinit var _tvDeadline: TextView
    private lateinit var _btnMulai: AppCompatButton
    private lateinit var _tvHintNilai: TextView
    private lateinit var _tvNilai: TextView
    private lateinit var _tvWarning: TextView
    private lateinit var _btnSetReminder:AppCompatButton


    private lateinit var kodeujian: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_masukkodeujian, container, false)
    }

    private fun getUjian() {
        db.collection("tbUjianSiswa").get()
            .addOnSuccessListener { result ->
                var nilai = "0"
                var ada = false
                for (data in result) {
                    if (data.data.get("uuidsiswa")
                            .toString() == auth.uid.toString() && data.data.get("uuidujian")
                            .toString() == kodeujian
                    ) {
                        ada = true
                        nilai = data.data.get("nilai").toString()
                        break
                    }
                }
                db.collection("tbUjian").get()
                    .addOnSuccessListener { result ->
                        for (ujian in result) {
                            if (ujian.data.get("kode").toString() == kodeujian) {
                                _tvKode.setText(ujian.data.get("kode").toString())
                                _tvNama.setText(ujian.data.get("nama").toString())
                                _tvDeadline.setText(ujian.data.get("tanggalselesai").toString())
                                if (ada) {
                                    _tvHintNilai.visibility = View.VISIBLE
                                    _tvNilai.setText(nilai+"/100")
                                    _tvNilai.visibility = View.VISIBLE
                                    _btnMulai.setTextColor(Color.parseColor("#FFFFFF"))
                                    _btnMulai.setBackgroundColor(Color.parseColor("#AAAAAA"))
                                    _tvWarning.visibility = View.VISIBLE
                                }
                                else{
                                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                                    val mulai = LocalDateTime.parse(ujian.data.get("tanggalmulai").toString(), formatter)
                                    val selesai=LocalDateTime.parse(ujian.data.get("tanggalselesai").toString(),formatter)
                                    val todaydate = Calendar.getInstance().time
                                    val sekarang = todaydate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                                    val namaujian=ujian.data.get("nama").toString()
                                    if(sekarang.isAfter(mulai)){
                                        _btnMulai.isClickable=true
                                        _btnMulai.isEnabled=true
                                    }
                                    else{
                                        _btnMulai.setTextColor(Color.parseColor("#FFFFFF"))
                                        _btnMulai.setBackgroundColor(Color.parseColor("#AAAAAA"))
                                        _tvWarning.visibility = View.VISIBLE
                                        _tvWarning.setText("Ujian belum dibuka!\nUjian akan dibuka pada: ${ujian.data.get("tanggalmulai").toString()}")
                                        _btnSetReminder.visibility=View.VISIBLE
                                        _btnSetReminder.setOnClickListener{
                                            val _MulaiUjian:Long=Calendar.getInstance().run {
                                                set(mulai.year,mulai.monthValue-1,mulai.dayOfMonth,mulai.hour,mulai.minute)
                                                timeInMillis
                                            }
                                            val _SelesaiUjian:Long=Calendar.getInstance().run {
                                                set(selesai.year,selesai.monthValue-1,selesai.dayOfMonth,selesai.hour,selesai.minute)
                                                timeInMillis
                                            }
                                            val _eventIntent=Intent(Intent.ACTION_INSERT).apply {
                                                data = CalendarContract.Events.CONTENT_URI
                                                putExtra(CalendarContract.Events.TITLE,"Ujian $namaujian")
                                                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,_MulaiUjian)
                                                putExtra(CalendarContract.EXTRA_EVENT_END_TIME,_SelesaiUjian)
                                                putExtra(CalendarContract.Events.DESCRIPTION,"Ujian $namaujian akan segera dimulai!\nKode ujian: ${ujian.data.get("kode").toString()}")
                                            }
                                            startActivity(_eventIntent)
                                        }
                                    }
                                }
                            }
                        }
                    }

            }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val _btnBackToHome = view.findViewById<AppCompatButton>(R.id.btnbacktoHome)
        _btnMulai = view.findViewById(R.id.buttonSubmit)
        _tvKode = view.findViewById(R.id.tvKode)
        _tvNama = view.findViewById(R.id.tvNama)
        _tvDeadline = view.findViewById(R.id.tvDeadline)
        _tvHintNilai = view.findViewById(R.id.textView7)
        _tvNilai = view.findViewById(R.id.tvNilai)
        _tvWarning = view.findViewById(R.id.tvWarning)
        _btnSetReminder=view.findViewById(R.id.btnSetReminder)

        _btnMulai.isClickable=false
        _btnMulai.isEnabled=false
        if (arguments != null) {
            var kode = arguments?.getString("kodeujian").toString()
            if (kode != null) {
                kodeujian = kode
            }
        }

        getUjian()

        _btnMulai.setOnClickListener {
            AlertDialog.Builder(activity)
                .setTitle("Masuk Ujian")
                .setMessage(
                    "Ketika masuk, sistem akan mencatat langsung absen anda!\n" +
                            "-Jangan Keluar dari aplikasi ketika mengikuti ujian\n" +
                            "-Pastikan koneksi internet anda stabil\n" +
                            "-Ujian hanya bisa dimasuki 1x\n" +
                            "-Pengumpulan akan dilakukan otomatis ketika deadline tercapai!\n" +
                            "Semoga Berhasil!"
                )
                .setPositiveButton(
                    "Masuk",
                    DialogInterface.OnClickListener { dialog, which ->
                        var newujiansiswa = ujiansiswaclass(
                            UUID.randomUUID().toString(),
                            auth.uid.toString(),
                            kodeujian,
                            "0"
                        )
                        db.collection("tbUjianSiswa").document(newujiansiswa.uuid)
                            .set(newujiansiswa).addOnSuccessListener {
                                val mUjian = isisoal()
                                var mBundle = Bundle()
                                mBundle.putString("kodeujian", kodeujian)
                                mBundle.putString("kodeujiansiswa", newujiansiswa.uuid)
                                mUjian.arguments = mBundle
                                val mFragmentManager = parentFragmentManager
                                mFragmentManager.beginTransaction().apply {
                                    replace(
                                        R.id.frameSiswaContainer,
                                        mUjian,
                                        isisoal::class.java.simpleName
                                    )
                                    addToBackStack(null)
                                    commit()
                                }
                            }
                    }
                )
                .setNegativeButton(
                    "Batal",
                    DialogInterface.OnClickListener { dialog, which ->

                    }
                ).show()


        }

        _btnBackToHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment masukkodeujian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            masukkodeujian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}