package com.example.project

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [isisoal.newInstance] factory method to
 * create an instance of this fragment.
 */
class isisoal : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapterS:adapterSoalViewSiswa
    private var arSoal:MutableList<soalGuru> = mutableListOf()
    private lateinit var kodeujian:String
    private lateinit var kodeujiansiswa:String
    private var jawaban :MutableList<Boolean> = mutableListOf()
    private lateinit var waktu:CountDownTimer
    private lateinit var _tvWaktu:TextView
    private var warningtime=false
    private lateinit var auth: FirebaseAuth;



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
        return inflater.inflate(R.layout.fragment_isisoal, container, false)
    }

    private fun getSoal(){
        db.collection("tbSoal").get()
            .addOnSuccessListener { result->
                arSoal.clear()
                for (soal in result){
                    if(soal.data.get("kodeujian").toString()==kodeujian){
                        val newsoal=soalGuru(
                            soal.data.get("idsoal").toString(),
                            soal.data.get("soal").toString(),
                            soal.data.get("jawaban1").toString(),
                            soal.data.get("jawaban2").toString(),
                            soal.data.get("jawaban3").toString(),
                            soal.data.get("jawaban4").toString(),
                            soal.data.get("jawabanbenar").toString(),
                            soal.data.get("kodeujian").toString()
                        )
                        arSoal.add(newsoal)
                    }
                }
                adapterS.notifyDataSetChanged()
                for(i in 0..arSoal.size-1){
                    jawaban.add(false)
                }
                Log.d("jumlahsoal",jawaban.size.toString())
            }
    }
    private fun getUjian(){
        Log.d("teswaktu","masuk1")

        db.collection("tbUjian").get()
            .addOnSuccessListener { result->
                Log.d("teswaktu","masuk2")
                for (ujian in result){
                    Log.d("teswaktu","${ujian.data.get("kodeujian").toString()} - $kodeujian")
                    if(ujian.data.get("kode").toString()==kodeujian){

                        val hariini=Calendar.getInstance()
                        val sekarang = GregorianCalendar(
                            hariini.get(Calendar.YEAR),
                            hariini.get(Calendar.MONTH)+1,
                            hariini.get(Calendar.DAY_OF_MONTH),
                            hariini.get(Calendar.HOUR_OF_DAY),
                            hariini.get(Calendar.MINUTE),
                            hariini.get(Calendar.SECOND)
                        )
                        Log.d("hari ini",sekarang.toString())
                        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                        val datemulai = LocalDateTime.parse(ujian.data.get("tanggalselesai").toString(), formatter);
                        val deadline=GregorianCalendar(
                            datemulai.year,
                            datemulai.month.value,
                            datemulai.dayOfMonth,
                            datemulai.hour,
                            datemulai.minute,
                            0
                        )
                        Log.d("deadline",deadline.toString())

                        val waktusisa:Long=deadline.timeInMillis-sekarang.timeInMillis
                        waktu= object : CountDownTimer(waktusisa,1000){
                            override fun onTick(millisUntilFinished: Long) {
                                _tvWaktu?.setText("Sisa Waktu: " + getString(R.string.formatted_time,
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60
                                ))
                                if(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished).toInt()<=4&&!warningtime){
                                    Toast.makeText(activity,"Waktu Sisa 5 Menit!",Toast.LENGTH_SHORT).show()
                                    warningtime=true
                                    _tvWaktu.setTextColor(Color.parseColor("#FF0000"))
                                }
                            }

                            override fun onFinish() {
                                Toast.makeText(activity,"Waktu telah habis!",Toast.LENGTH_SHORT).show()
                                submitUjian()
                            }
                        }
                        waktu.start()
                        break;
                    }
                }
            }
    }
    private fun submitUjian(){
        waktu.cancel()
        //get banyaknya jawaban yang benar
        var jawabanbenar=0
        for (each in jawaban){
            if (each==true){
                jawabanbenar+=1
            }
        }

        //hitung nilai
        var nilaisiswa= String.format("%.1f",(jawabanbenar.toDouble()/jawaban.size)*100)
        var newujiansiswa = ujiansiswaclass(
            kodeujiansiswa,
            auth.uid.toString(),
            kodeujian,
            nilaisiswa
        )
        db.collection("tbUjianSiswa").document(kodeujiansiswa)
            .set(newujiansiswa)
            .addOnSuccessListener {
                val intent = Intent(activity, muridHome::class.java)
                startActivity(intent)
                activity?.finish()
            }



    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        _tvWaktu=view.findViewById(R.id.tvWaktu)
        var _btnSubmit=view.findViewById<AppCompatButton>(R.id.btnSubmit)
        val _rvSoal=view.findViewById<RecyclerView>(R.id.rvSoal)

        if (arguments != null) {
            var kode = arguments?.getString("kodeujian").toString()
            var kodeusiswa=arguments?.getString("kodeujiansiswa").toString()
            if (kode != null) {
                kodeujian = kode
                kodeujiansiswa=kodeusiswa
            }
        }
//        val _rgJawaban=view.findViewById<RadioGroup>(R.id.rgJawaban)
        var waktusisa:Long=10000


        adapterS= adapterSoalViewSiswa(arSoal)
        adapterS.setOnClickCallback(object :adapterSoalViewSiswa.OnItemClickCallback{
            override fun setJawaban(jawabansoal: String, position: Int) {
                if(jawabansoal==arSoal[position].jawabanbenar){
                    jawaban[position]=true
                    Log.d("jawaban","soal index:"+(position).toString()+" jawaban:"+jawaban[position])
                }else{
                    jawaban[position]=false
                }
            }
        })




        //testing add data
//        for (i in 1..10){
//            arSoal.add(soal(i.toString()+".","soal "+i.toString(),"jawaban1 "+i.toString(),
//            "jawaban2 "+i.toString(),"jawaban3 "+i.toString(),"jawaban4 "+i.toString()
//                ))
//        }
        _rvSoal.layoutManager=LinearLayoutManager(activity)
        _rvSoal.adapter=adapterS
        //testing add data

        getSoal()
        getUjian()

        //get jumlah soal
        var jumlahSoal:Int=adapterS.itemCount
        Log.d("jumlahsoal",jumlahSoal.toString())

        //create string list jawaban
//        for(i in 1..jumlahSoal){
//            jawaban.add(i.toString())
//        }


        //TODO: get jawaban dari tiap soal





        //timer
//        waktusisa=100000000
//        var waktu= object : CountDownTimer(waktusisa,1000){
//            override fun onTick(millisUntilFinished: Long) {
////            Log.d("waktu",_tvWaktu?.text.toString())
//                _tvWaktu?.setText("Sisa Waktu: " + getString(R.string.formatted_time,
//                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
//                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)%60,
//                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)%60
//                ))
//            }
//
//            override fun onFinish() {
//                Toast.makeText(activity,"waktu habis",Toast.LENGTH_SHORT).show()
//            }
//        }
//        waktu.start()



        _btnSubmit.setOnClickListener{
            AlertDialog.Builder(activity)
                .setTitle("Submit Ujian")
                .setMessage(
                    "!!PERHATIAN!!\nAnda tidak bisa mengubah jawaban setelah anda menekan tombol submit!"
                )
                .setPositiveButton(
                    "Submit",
                    DialogInterface.OnClickListener { dialog, which ->
                        submitUjian()
                    }
                )
                .setNegativeButton(
                    "Batal",
                    DialogInterface.OnClickListener { dialog, which ->
                    }
                ).show()



        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment isisoal.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            isisoal().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}