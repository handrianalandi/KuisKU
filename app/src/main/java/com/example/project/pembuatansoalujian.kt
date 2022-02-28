package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [pembuatansoalujian.newInstance] factory method to
 * create an instance of this fragment.
 */
class pembuatansoalujian : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var _etSoal:EditText
    private lateinit var _etJawaban1:EditText
    private lateinit var _etJawaban2:EditText
    private lateinit var _etJawaban3:EditText
    private lateinit var _etJawaban4:EditText
    private lateinit var _rbA:RadioButton
    private lateinit var _rbB:RadioButton
    private lateinit var _rbC:RadioButton
    private lateinit var _rbD:RadioButton
    private lateinit var _rgJawabanBenar:RadioGroup
    private lateinit var _btnAddSoal:AppCompatButton
    var kodesoal = ""

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
        return inflater.inflate(R.layout.fragment_pembuatansoalujian, container, false)
    }
    private fun setSoal(idSoal:String){
        db.collection("tbSoal").get()
            .addOnSuccessListener { result ->
                for (soal in result){
                    if(soal.data.get("idsoal").toString()==idSoal){
                        _etSoal.setText(soal.data.get("soal").toString())
                        _etJawaban1.setText(soal.data.get("jawaban1").toString())
                        _etJawaban2.setText(soal.data.get("jawaban2").toString())
                        _etJawaban3.setText(soal.data.get("jawaban3").toString())
                        _etJawaban4.setText(soal.data.get("jawaban4").toString())
                        val jawabanbenar=soal.data.get("jawabanbenar").toString()
                        //1=A,2=B,3=C,4=F
                        if(jawabanbenar=="A"){
                            _rgJawabanBenar.check(_rbA.id)
                        }
                        else if(jawabanbenar=="B"){
                            _rgJawabanBenar.check(_rbB.id)
                        }
                        else if(jawabanbenar=="C"){
                            _rgJawabanBenar.check(_rbC.id)
                        }
                        else if(jawabanbenar=="D"){
                            _rgJawabanBenar.check(_rbD.id)
                        }
                        _btnAddSoal.setHint("Edit Soal")
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var _tvNomerSoal=view.findViewById<TextView>(R.id.tvNomorSoal)
        var _btnCancel=view.findViewById<AppCompatButton>(R.id.btnCancel)
        _btnAddSoal=view.findViewById(R.id.buttonAddSoal)
        _etSoal=view.findViewById(R.id._soal)
        _etJawaban1=view.findViewById(R.id.jawaban1)
        _etJawaban2=view.findViewById(R.id.jawaban2)
        _etJawaban3=view.findViewById(R.id.jawaban3)
        _etJawaban4=view.findViewById(R.id.jawaban4)
        _rbA=view.findViewById(R.id.rbA)
        _rbB=view.findViewById(R.id.rbB)
        _rbC=view.findViewById(R.id.rbC)
        _rbD=view.findViewById(R.id.rbD)
        _rgJawabanBenar=view.findViewById(R.id.rgJawaban)



        var kodeujian=""
        if (arguments != null) {
            //jika adalah edit
            kodesoal = arguments?.getString("kodesoal").toString()
            kodeujian=arguments?.getString("kodeujian").toString()
            setSoal(kodesoal)
        }
        if(kodesoal=="null"){
            //jika adalah buat
            kodesoal= UUID.randomUUID().toString()
        }

        _btnAddSoal.setOnClickListener {
            var lengkap=true
            if(_etSoal.text.isEmpty()){
                _etSoal.error="Soal tidak boleh kosong!"
                lengkap=false
            }
            if(_etJawaban1.text.isEmpty()){
                _etJawaban1.error="Jawaban A tidak boleh kosong!"
                lengkap=false
            }
            if(_etJawaban2.text.isEmpty()){
                _etJawaban2.error="Jawaban B tidak boleh kosong!"
                lengkap=false
            }
            if(_etJawaban3.text.isEmpty()){
                _etJawaban3.error="Jawaban C tidak boleh kosong!"
                lengkap=false
            }
            if(_etJawaban4.text.isEmpty()){
                _etJawaban4.error="Jawaban D tidak boleh kosong!"
                lengkap=false
            }
            if(lengkap){
                val soal=_etSoal.text.toString()
                val jawaban1=_etJawaban1.text.toString()
                val jawaban2=_etJawaban2.text.toString()
                val jawaban3=_etJawaban3.text.toString()
                val jawaban4=_etJawaban4.text.toString()
                var jawabanbenar="A"
                if(_rbB.isChecked){
                    jawabanbenar="B"
                }
                if(_rbC.isChecked){
                    jawabanbenar="C"
                }
                if(_rbD.isChecked){
                    jawabanbenar="D"
                }
                val newsoal=soalGuru(kodesoal,soal,jawaban1,jawaban2,jawaban3,jawaban4,jawabanbenar,kodeujian)
                db.collection("tbSoal").document(kodesoal)
                    .set(newsoal)
                    .addOnSuccessListener {
                        parentFragmentManager.popBackStack()
                    }

            }
        }

        _btnCancel.setOnClickListener {
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
         * @return A new instance of fragment pembuatansoalujian.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            pembuatansoalujian().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}