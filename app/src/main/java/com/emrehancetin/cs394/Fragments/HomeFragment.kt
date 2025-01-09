package com.emrehancetin.cs394.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emrehancetin.cs394.Adapter.CryptoAdapter
import com.emrehancetin.cs394.Adapter.OwnedCryptoAdapter
import com.emrehancetin.cs394.R
import com.emrehancetin.cs394.ViewModel.AppViewModel
import com.emrehancetin.cs394.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var appViewModel: AppViewModel
    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var ownedCryptoAdapter: OwnedCryptoAdapter


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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        val cashTextView: TextView = view.findViewById(R.id.cashBalanceTextView)
        val ownedRecyclerView: RecyclerView = view.findViewById(R.id.ownedRecyclerView)
        val cryptoRecyclerView: RecyclerView = view.findViewById(R.id.cryptoRecyclerView)

        // Observe cash balance
        appViewModel.ownedCryptoList.observe(viewLifecycleOwner) { ownedList ->
            ownedCryptoAdapter.updateOwnedCryptoList(ownedList)

        }

        // To get wallet from firestore
        appViewModel.fetchWalletFromFirestore()


        // Setup Owned Cryptos RecyclerView
        ownedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ownedCryptoAdapter = OwnedCryptoAdapter(mutableListOf())
        ownedRecyclerView.adapter = ownedCryptoAdapter

        // Setup Cryptos RecyclerView
        cryptoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cryptoAdapter = CryptoAdapter(mutableListOf()) {}
        cryptoRecyclerView.adapter = cryptoAdapter

        appViewModel.cashBalance.observe(viewLifecycleOwner) { cash ->
            cashTextView.text = "Cash: $${String.format("%.2f", cash)}"
        }

        appViewModel.ownedCryptoList.observe(viewLifecycleOwner) { ownedList ->
            ownedCryptoAdapter.updateOwnedCryptoList(ownedList)
        }

        appViewModel.cryptoList.observe(viewLifecycleOwner) { cryptoList ->
            cryptoAdapter.updateCryptoList(cryptoList)
        }




        appViewModel.loadCryptos()
    }

    private fun getWalletDataFromFirestore(view: View){

            db.collection("Wallets").whereEqualTo("email", auth.currentUser!!.email.toString()).addSnapshotListener{value,error ->
                if(error != null) Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
                else{
                    if(value!= null){
                        if (!value.isEmpty){
                            val documents = value.documents
                            for (document in documents){
                                val userWallet = document.get("wallet")
                                binding.cashBalanceTextView.setText(userWallet.toString()+".00")
                            }
                        }
                    }
                }
            }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
