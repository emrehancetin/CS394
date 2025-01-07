package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emrehancetin.cs394.R

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInput: EditText = view.findViewById(R.id.emailInput)
        val passwordInput: EditText = view.findViewById(R.id.passwordInput)
        val confirmPasswordInput: EditText = view.findViewById(R.id.confirmPasswordInput)
        val registerButton: Button = view.findViewById(R.id.registerButton2)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (email.isNotBlank() && password == confirmPassword) {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Passwords do not match or fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
