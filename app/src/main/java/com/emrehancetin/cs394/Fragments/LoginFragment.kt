package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import retrofit2.http.POST

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = Firebase.auth
        db = Firebase.firestore
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val loginButton: Button = view.findViewById(R.id.loginButton)
        val registerButton:Button = binding.registerButton

        loginButton.setOnClickListener { login(it) }
        registerButton.setOnClickListener { register(it) }
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            //kullanıcı daha önceden giriş yapmış
            val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
            Navigation.findNavController(view).navigate(action)
        }


    }

    private fun register(view: View){
        val email = binding.emailLoginText.text.toString()
        val password = binding.passwordLoginText.text.toString()
        val initalWallet = 2000000.00
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // user will add db
                        val postMap = hashMapOf<String,Any>()
                        postMap.put("email",email)
                        postMap.put("wallet",initalWallet)

                        db.collection("Wallets").add(postMap).addOnSuccessListener { documentReference ->
                            println("db wallet is done")
                        }.addOnFailureListener {exception->
                            println(exception.localizedMessage)
                        }

                        val postMap2 = hashMapOf<String,Any>()
                        postMap2.put("email",email)
                        postMap2.put("BTC",0)
                        postMap2.put("ETH",0)
                        postMap2.put("SOL",0)
                        db.collection("OwnedCrypto").add(postMap2).addOnSuccessListener { documentReference ->
                            println("db OwnedCrypto is done")
                        }.addOnFailureListener {exception->
                            println(exception.localizedMessage)
                        }


                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun login(view: View){
        val email = binding.emailLoginText.text.toString()
        val password = binding.passwordLoginText.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener {exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

