package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.OrderHistoryAdapter
import com.emrehancetin.cs394.Model.OrderHistoryModel
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.AppViewModel
import com.emrehancetin.cs394.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding:FragmentProfileBinding? = null
    private val binding get()= _binding!!

    private lateinit var appViewModel: AppViewModel
    private lateinit var adapter: OrderHistoryAdapter

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val recyclerViewOrderHistory: RecyclerView = binding.recyclerViewOrderHistory
        recyclerViewOrderHistory.layoutManager = LinearLayoutManager(requireContext())

        val adapter = OrderHistoryAdapter(mutableListOf())
        recyclerViewOrderHistory.adapter = adapter

        appViewModel.orderHistory.observe(viewLifecycleOwner) { updatedHistory ->
            adapter.updateOrders(updatedHistory)
        }

        val username = auth.currentUser?.email.toString().substringBefore("@")
        binding.textViewPhoto.setText("- "+username + " - ")


        val creationTimestamp = auth.currentUser?.metadata?.creationTimestamp
        val date = creationTimestamp?.let { Date(it) }
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        binding.accountCreationDateTextView.setText(date?.let { dateFormat.format(it) })
        val signOutButton = binding.signOutButton
        signOutButton.setOnClickListener { signOut(it) }
    }

    private fun signOut(view: View){
        auth.signOut()

        val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
