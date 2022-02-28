package com.example.project

import android.R.attr
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.R.attr.password
import android.text.InputType
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [registerpage.newInstance] factory method to
 * create an instance of this fragment.
 */
class registerpage : Fragment() {
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
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
        return inflater.inflate(R.layout.fragment_registerpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack=view.findViewById<AppCompatButton>(R.id.btnbacktoHome)
        auth = Firebase.auth


        val _etUsername=view.findViewById<EditText>(R.id._username)
        val _etEmail=view.findViewById<EditText>(R.id._email)
        val _etPassword=view.findViewById<EditText>(R.id._password)
        val _rbMurid=view.findViewById<RadioButton>(R.id.rbMurid)
        val _btnSignUp=view.findViewById<AppCompatButton>(R.id.btnSignUp)
        _etPassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

        btnBack.setOnClickListener {
            val mLogin=loginpage()
            val mFragmentManager=parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frameContainer,mLogin,loginpage::class.java.simpleName)
                commit()
            }
        }

        _btnSignUp.setOnClickListener {
            var lengkap=true
            if(_etUsername.text.isEmpty()){
                _etUsername.error="Nama Tidak Boleh Kosong!"
                lengkap=false
            }
            if(_etEmail.text.isEmpty()){
                _etEmail.error="Email Tidak Boleh Kosong!"
                lengkap=false
            }
            if(_etPassword.text.isEmpty()){
                _etPassword.error="Password Tidak Boleh Kosong!"
                lengkap=false
            }
            if(lengkap){
                val nama=_etUsername.text.toString()
                val email=_etEmail.text.toString()
                val password=_etPassword.text.toString()
                var isTeacher="1"
                if(_rbMurid.isChecked){
                    isTeacher="0"
                }

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        val uid=it.user?.uid.toString()
                        var newacc= userclass(uid,nama,isTeacher,email)
                        db.collection("tbUser").document(uid)
                            .set(newacc)
                            .addOnSuccessListener {
                                Toast.makeText(activity,"Pendaftaran Berhasil\nSilahkan Login!",Toast.LENGTH_SHORT).show()
                                parentFragmentManager.popBackStack()
                            }
                    }
                    .addOnFailureListener{
                        Toast.makeText(activity,it.message.toString(),Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment registerpage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            registerpage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}