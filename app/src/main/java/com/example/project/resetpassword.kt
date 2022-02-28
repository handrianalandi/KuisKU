package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [resetpassword.newInstance] factory method to
 * create an instance of this fragment.
 */
class resetpassword : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resetpassword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        var _etEmail=view.findViewById<EditText>(R.id.etEmail)
        var _btnReset=view.findViewById<AppCompatButton>(R.id.btnReset)
        var _btnBacktoHome=view.findViewById<AppCompatButton>(R.id.btnbacktoHome)

        _btnBacktoHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        _btnReset.setOnClickListener {
            if(!_etEmail.text.isEmpty()){
                var email=_etEmail.text.toString()
                auth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Toast.makeText(activity,"Silahkan Cek E-mail Anda!",Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener{
                        Toast.makeText(activity,it.message.toString(),Toast.LENGTH_SHORT).show()
                    }
            }
            else{
                _etEmail.error="Email Tidak Boleh Kosong!"
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
         * @return A new instance of fragment resetpassword.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            resetpassword().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}