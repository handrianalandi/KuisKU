package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [pembuatanUjian.newInstance] factory method to
 * create an instance of this fragment.
 */
class pembuatanUjian : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var arSoal:MutableList<soalGuru> = mutableListOf()
    private lateinit var adapterS:adapterAddSoalGuru
    private lateinit var kodesoal:String

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
        return inflater.inflate(R.layout.fragment_pembuatan_ujian, container, false)
    }

    //func get soal
    private fun getSoal(){
        db.collection("tbSoal").get()
            .addOnSuccessListener { result ->
                arSoal.clear()
                for(doc in result){
                    val kodeujian=doc.data.get("kodeujian").toString()
                    if(kodeujian==kodesoal){
                        val idsoal = doc.data.get("idsoal").toString()
                        val soal = doc.data.get("soal").toString()
                        val jawaban1=doc.data.get("jawaban1").toString()
                        val jawaban2=doc.data.get("jawaban2").toString()
                        val jawaban3=doc.data.get("jawaban3").toString()
                        val jawaban4=doc.data.get("jawaban4").toString()
                        val jawabanbenar=doc.data.get("jawabanbenar").toString()
                        val newsoal=soalGuru(idsoal,soal,jawaban1,jawaban2,jawaban3,jawaban4,
                            jawabanbenar,kodeujian)
                        arSoal.add(newsoal)
                    }
                }
                adapterS.notifyDataSetChanged()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var _tvKodeSoal = view.findViewById<TextView>(R.id.tvKodeSoal)
        var _btnAddSoal = view.findViewById<AppCompatButton>(R.id.btnAddSoal)
        var _rvSoal = view.findViewById<RecyclerView>(R.id.rvSoalGuru)
        var _btnBack=view.findViewById<AppCompatButton>(R.id.btnBack)


        adapterS= adapterAddSoalGuru(arSoal)
        adapterS.setOnClickCallback(object :adapterAddSoalGuru.OnItemClickCallback
        {
            override fun editSoal(data: soalGuru) {
                val mBundle = Bundle()
                mBundle.putString("kodesoal", data.idsoal)
                mBundle.putString("kodeujian",kodesoal)

                val mEditSoal = pembuatansoalujian()
                mEditSoal.arguments = mBundle

                val mFragmentManager = parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.frameGuruContainer,
                        mEditSoal,
                        pembuatansoalujian::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        })
        _rvSoal.layoutManager=LinearLayoutManager(activity)
        _rvSoal.adapter=adapterS

//        for(i in 1..5){
//            arSoal.add(soalGuru(i.toString(),"soal "+i.toString(),
//            "jawaban1 "+i.toString(),
//                "jawaban2 "+i.toString(),
//                "jawaban3 "+i.toString(),
//                "jawaban4 "+i.toString(),
//                "jawabanbenar "+i.toString()
//                ))
//        }
        _btnBack.setOnClickListener {

            val mBuatSoal = homeguru()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(
                    R.id.frameGuruContainer,
                    mBuatSoal,
                    homeguru::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }


        if (arguments != null) {
            var kode = arguments?.getString("kodeujian").toString()
            _tvKodeSoal.setText("Kode Ujian :" + kode)
            if (kode != null) {
                kodesoal = kode
            }
        }
        getSoal()
        _btnAddSoal.setOnClickListener {
            val mBundle = Bundle()
            mBundle.putString("kodeujian", kodesoal)

            val mBuatSoal = pembuatansoalujian()
            mBuatSoal.arguments = mBundle

            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(
                    R.id.frameGuruContainer,
                    mBuatSoal,
                    pembuatansoalujian::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        getSoal()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment pembuatanUjian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            pembuatanUjian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}