package com.smart.smartbalibackpaker.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smart.smartbalibackpaker.R

class GroupChatFragment : Fragment() {
    private lateinit var dbReference: DatabaseReference
    private var myUid: String? = null
    private var hashMap: HashMap<String, Any> = HashMap()


    override fun onStart() {
        checkOnlineState("online")
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        checkOnlineState(System.currentTimeMillis().toString())

    }

    override fun onResume() {
        checkOnlineState("online")
        super.onResume()
    }

    override fun onDestroy() {
        checkOnlineState(System.currentTimeMillis().toString())
        super.onDestroy()
    }

    private fun checkOnlineState(onlineState: String) {
        myUid = FirebaseAuth.getInstance().currentUser?.uid
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(myUid.toString())

        hashMap.put("onlineState", onlineState)
        dbReference.updateChildren(hashMap)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false)
    }

}