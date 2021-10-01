package com.smart.smartbalibackpaker.chat

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.model.groupchat.GroupChatAdapter
import com.smart.smartbalibackpaker.core.model.groupchat.ModelGroupChat
import com.smart.smartbalibackpaker.core.utils.Constant.ALL_GROUP
import com.smart.smartbalibackpaker.databinding.FragmentPublicGroupBinding


class PublicGroupFragment : Fragment() {
    private var binding : FragmentPublicGroupBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var adapter: GroupChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPublicGroupBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        loadMessage()
        binding?.btnSendGroupChat?.setOnClickListener {
            val message = binding?.etGroupChatBox?.text.toString()
            validateMessage(message)
        }
    }


    private fun validateMessage(message: String) {
        if(TextUtils.isEmpty(message)){
            Toast.makeText(context, "Can't send empty message", Toast.LENGTH_SHORT).show()
        } else {
            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {
        val timeStamp = System.currentTimeMillis().toString()

        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.apply {
            put("sender", "${auth.currentUser?.uid}")
            put("message", message)
            put("time", timeStamp)
            put("type", "" + "text")
        }

        val ref = db.getReference("groups")
        ref.child(ALL_GROUP).child("messages").child(timeStamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                binding?.etGroupChatBox?.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadMessage() {
        val ref = db.getReference("groups")
        ref.child(ALL_GROUP).child("messages").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupChatList = ArrayList<ModelGroupChat>()
                groupChatList.clear()

                for(ds in snapshot.children){
                    val model = ds.getValue(ModelGroupChat::class.java)
                    if (model != null) {
                        val groupChat = ModelGroupChat(
                            model.message,
                            model.sender,
                            model.time,
                            model.type
                        )
                        groupChatList.add(groupChat)
                    }
                }
                adapter = context?.let { GroupChatAdapter(it, groupChatList) }!!
                setRecyclerView(adapter)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setRecyclerView(groupChatAdapter: GroupChatAdapter) {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        binding?.rvChatAct?.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = groupChatAdapter
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}