package com.example.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [lihatdaftarujian.newInstance] factory method to
 * create an instance of this fragment.
 */
class lihatdaftarujian : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var arSoalGuru:MutableList<ujianclass> = mutableListOf()
    private lateinit var adapterG:adapterViewSoalGuru
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
        return inflater.inflate(R.layout.fragment_lihatdaftarujian, container, false)
    }

    private fun getUjian(){
        arSoalGuru.clear()
        db.collection("tbUjian").get()
            .addOnSuccessListener { result ->
                for(ujian in result){
//                    Log.d("masukguru",auth.uid.toString())
                    if(ujian.data.get("uidguru").toString()==auth.uid.toString()){
                        Log.d("masukguru",auth.uid.toString())
                        val newujian=ujianclass(ujian.data.get("kode").toString(),
                            ujian.data.get("nama").toString(),
                            ujian.data.get("uidguru").toString(),ujian.data.get("tanggalmulai").toString(),
                            ujian.data.get("tanggalselesai").toString())
                        arSoalGuru.add(newujian)
                    }
                }
                adapterG.notifyDataSetChanged()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val _rvListSoal=view.findViewById<RecyclerView>(R.id.rvListSoal)
        val _btnBack= view.findViewById<AppCompatButton>(R.id.buttonBack)
        _btnBack.setOnClickListener {
            val mBack=homeguru()
            val mFragmentManager=parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameGuruContainer,mBack,homeguru::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
        adapterG= adapterViewSoalGuru(arSoalGuru)
        adapterG.setOnItemClickCallback(object: adapterViewSoalGuru.OnItemClickCallback
        {
            override fun onEditClicked(data: ujianclass) {
                val mView=liatnilaimurid()
                var mBundle=Bundle()
                mBundle.putString("kodeujian",data.kode)
                mView.arguments=mBundle
                val mFragmentManager=parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameGuruContainer,mView,liatnilaimurid::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }

            override fun editUjian(data: ujianclass) {
                val mView=addKodeUjian()
                var mBundle=Bundle()
                mBundle.putString("kodeujian",data.kode)
                mView.arguments=mBundle
                val mFragmentManager=parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameGuruContainer,mView,addKodeUjian::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        })
        _rvListSoal.layoutManager= LinearLayoutManager(activity)
        _rvListSoal.adapter=adapterG

        getUjian()


//        for(i in 1..4){
//            arSoalGuru.add(listsoal(i.toString()+"KODE"+i.toString(), i.toString()+"UJIAN: UJIAN KE-"+i.toString()))
//        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            lihatdaftarujian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}