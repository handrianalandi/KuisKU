package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [liatnilaimurid.newInstance] factory method to
 * create an instance of this fragment.
 */
class liatnilaimurid : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var arNilaiMurid:MutableList<ujiansiswaclass> = mutableListOf()
    private lateinit var adapterG:adapterNilaiViewGuru
    private lateinit var kodeujian : String

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
        return inflater.inflate(R.layout.fragment_liatnilaimurid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val _buttonback = view.findViewById<AppCompatButton>(R.id.buttonBack)
        var _rvNilaiMurid = view.findViewById<RecyclerView>(R.id.rvNilaiMurid)
        _buttonback.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
        adapterG= adapterNilaiViewGuru(arNilaiMurid)

        _rvNilaiMurid.layoutManager= LinearLayoutManager(activity)
        _rvNilaiMurid.adapter=adapterG

        if (arguments != null) {
            var kode = arguments?.getString("kodeujian").toString()
            if (kode != null) {
                kodeujian = kode
            }
        }
        readData()
    }

    fun readData(){
        db.collection("tbUjianSiswa").get()
            .addOnSuccessListener { result ->
                for (ujian in result) {
                    if (ujian.data.get("uuidujian").toString() == kodeujian) {
                        db.collection("tbUser").get()
                            .addOnSuccessListener { result1 ->
                                for (user in result1) {
                                    if (user.data.get("uid") == ujian.data.get("uuidsiswa")) {
                                        val newujian = ujiansiswaclass(
                                            ujian.data.get("uuid").toString(),
                                            " ",
                                            user.data.get("nama").toString(),
                                            ujian.data.get("nilai").toString()
                                        )
                                        arNilaiMurid.add(newujian)
                                        adapterG.notifyDataSetChanged()
                                        break
                                    }
                                }
                            }
                    }
                }
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment liatnilaimurid.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            liatnilaimurid().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}