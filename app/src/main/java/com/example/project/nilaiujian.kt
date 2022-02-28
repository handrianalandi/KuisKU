package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Use the [nilaiujian.newInstance] factory method to
 * create an instance of this fragment.
 */
class nilaiujian : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var adapterN:adapterNilaiViewSiswa
    private var arNilai:MutableList<ujiansiswaclass> = mutableListOf()
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterN = adapterNilaiViewSiswa(arNilai)
        auth = Firebase.auth

        val _rvNilai = view.findViewById<RecyclerView>(R.id.rvNilaiSaya)
        val _btnBack = view.findViewById<AppCompatButton>(R.id.btnBack)

        _rvNilai.layoutManager = LinearLayoutManager(activity)
        _rvNilai.adapter = adapterN

        //testing TODO::get all data from firestore
            db.collection("tbUjianSiswa").get()
                .addOnSuccessListener { result ->
                    for (ujian in result) {
                        if (ujian.data.get("uuidsiswa").toString() == auth.uid.toString()) {
                            db.collection("tbUjian").get()
                                .addOnSuccessListener { result1 ->
                                    for (user in result1){
                                        if (user.data.get("kode") == ujian.data.get("uuidujian")){
                                            val newujian = ujiansiswaclass(
                                                ujian.data.get("uuid").toString(),
                                                user.data.get("nama").toString(),
                                                ujian.data.get("uuidujian").toString(),
                                                ujian.data.get("nilai").toString()
                                            )
                                            arNilai.add(newujian)
                                            adapterN.notifyDataSetChanged()
                                            break
                                        }
                                    }
                                }
                            Log.d("masuksiswa", auth.uid.toString())

                        }
                    }

                    /*adapterN.refreshdata()*/
            //testing


            _btnBack.setOnClickListener {
                val mHome = muridHomeFragment()
                val mFragmentManager = parentFragmentManager
                mFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.frameSiswaContainer,
                        mHome,
                        muridHomeFragment::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nilaiujian, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment nilaiujian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            nilaiujian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}