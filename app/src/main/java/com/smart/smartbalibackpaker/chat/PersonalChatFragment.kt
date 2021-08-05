package com.smart.smartbalibackpaker.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.FragmentPersonalChatBinding
import com.smart.smartbalibackpaker.databinding.FragmentStreamChatBinding
import com.smart.smartbalibackpaker.model.DataUser
import com.smart.smartbalibackpaker.model.UserAdapter

class PersonalChatFragment : Fragment() {

    private var _binding: FragmentPersonalChatBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: UserAdapter

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private var user: FirebaseUser? = null
    private lateinit var dbReference: DatabaseReference

    private var listUser = ArrayList<DataUser>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPersonalChatBinding.inflate(layoutInflater, container, false)

        binding?.rvChatPersonal?.layoutManager = LinearLayoutManager(context)
        binding?.rvChatPersonal?.setHasFixedSize(true)

        getUser()

        return binding?.root
    }


    private fun showRecycler(){
        binding?.rvChatPersonal?.apply {

            adapter = adapter

        }
    }

    private fun getUser() {
        user = FirebaseAuth.getInstance().currentUser
        dbReference = FirebaseDatabase.getInstance().getReference("users")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                for (ds in snapshot.children) {
                    val dataUser = ds.getValue(DataUser::class.java)

                    if (!dataUser?.uid.equals(user?.uid)) {
                        if (dataUser != null) {
                            listUser.add(dataUser)

                        }
                    }
                    adapter = context?.let { UserAdapter(listUser) }!!
                    binding?.rvChatPersonal?.adapter = adapter


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
            }

        })
    }

}