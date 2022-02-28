package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
 * Use the [muridHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class muridHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val _btnCekNilai=view.findViewById<AppCompatButton>(R.id.btnCekNilai)
        val _btnLogout=view.findViewById<AppCompatButton>(R.id.btnLogout)
        val _btnKode=view.findViewById<AppCompatButton>(R.id.btnKode)
        val _tvNama=view.findViewById<TextView>(R.id._usernamemurid)
        val _etKodeSoal=view.findViewById<EditText>(R.id.etKodeSoal)

        //get user name
        val uid=auth.uid.toString()
        db.collection("tbUser").get()
            .addOnSuccessListener { result ->
                for (user in result) {
                    if (user.data.get("uid").toString() == uid) {
                        _tvNama.setText(user.data.get("nama").toString())
                    }
                }

            }






        Log.d("testmasuk","test"+_btnCekNilai.hint.toString())
        _btnCekNilai.setOnClickListener {
            Log.d("testmasuk","masuk gaes")
            val mCekNilai=nilaiujian()
            val mFragmentManager=parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameSiswaContainer,mCekNilai,nilaiujian::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        _btnLogout.setOnClickListener{
//            mFragmentManager.beginTransaction().remove(this).commit()
            auth.signOut()
            startActivity(Intent(activity,MainActivity::class.java))
            activity?.finish()
        }
        _btnKode.setOnClickListener{
            if(!_etKodeSoal.text.isEmpty()){
                db.collection("tbUjian").get()
                    .addOnSuccessListener { result ->
                        var nemu=false
                        for (ujian in result){
                            if(ujian.data.get("kode").toString()==_etKodeSoal.text.toString()){
                                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                                val deadline = LocalDateTime.parse(ujian.data.get("tanggalselesai").toString(), formatter);
                                val mulai = LocalDateTime.parse(ujian.data.get("tanggalmulai").toString(), formatter)
                                val todaydate = Calendar.getInstance().time
                                val sekarang = todaydate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                                nemu=true
                                    if(sekarang.isBefore(deadline)){
                                        val mRuangUjian=masukkodeujian()
                                        var mBundle=Bundle()
                                        mBundle.putString("kodeujian",_etKodeSoal.text.toString())
                                        mRuangUjian.arguments=mBundle
                                        val mFragmentManager=parentFragmentManager
                                        mFragmentManager.beginTransaction().apply {
                                            replace(R.id.frameSiswaContainer,mRuangUjian,masukkodeujian::class.java.simpleName)
                                            addToBackStack(null)
                                            commit()
                                        }
                                    }
                                    else{
                                        Toast.makeText(activity,"Deadline dari ujian sudah terlampaui!",Toast.LENGTH_SHORT).show()
                                    }

                                break;
                            }
                        }
                        if(!nemu){
                            Toast.makeText(activity,"Kode Ujian Tidak Valid\nMohon Cek Lagi Kode Yang Anda Ketik!",
                                Toast.LENGTH_LONG).show()
                        }
                    }

            }
            else{
                _etKodeSoal.error="Kode Ujian Tidak Boleh Kosong!"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_murid_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment muridHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            muridHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}