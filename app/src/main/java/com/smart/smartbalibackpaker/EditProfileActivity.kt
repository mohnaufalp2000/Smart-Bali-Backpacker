package com.smart.smartbalibackpaker

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.auth.AddPhotoActivity
import com.smart.smartbalibackpaker.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityEditProfileBinding.inflate(layoutInflater) }
    private lateinit var dialog: AlertDialog
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dbReference: DatabaseReference
    private var userId: String? = null
    private lateinit var imageUri : Uri
    private lateinit var bitmap: Bitmap
    private lateinit var removeBtn : ImageView
    private lateinit var removeTxt : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        userId = auth.currentUser?.uid

        val query = dbReference.orderByChild("email").equalTo(user.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val username = ""+ ds.child("username").value
                    val image = ds.child("image").value

                    binding.etUsername.setText(username)

                    Glide.with(applicationContext)
                        .load(image)
                        .into(binding.imgPhoto)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }
        })

        setupToolbar()

        binding.imgEditProfile.setOnClickListener {
            changePhoto()
        }

        binding.btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ChangePassActivity::class.java))
        }

        binding.btnUpdateProfile.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == AddPhotoActivity.REQUEST_TAKE_PICTURE && resultCode == RESULT_OK -> {
                bitmap = data?.extras?.get("data") as Bitmap
                imageUri = data.data!!

                binding.imgPhoto.setImageBitmap(bitmap)
                removeBtn.visibility = View.VISIBLE
                removeTxt.visibility = View.VISIBLE
            }
            requestCode == AddPhotoActivity.REQUEST_UPLOAD_PICTURE && resultCode == RESULT_OK -> {
                binding.imgPhoto.setImageURI(data?.data)
                imageUri = data?.data!!

                removeBtn.visibility = View.VISIBLE
                removeTxt.visibility = View.VISIBLE
            }
        }
    }

    private fun changePhoto() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_photo, null)


        dialogBuilder.apply {
            setTitle(getString(R.string.choose))
            setView(dialogLayout)
            setNegativeButton("Cancel") {
                    dialog, _ -> dialog.dismiss()
            }
        }

        dialog = dialogBuilder.create()
        dialog.show()

        val galleryBtn = dialogLayout.findViewById<ImageView>(R.id.img_gallery)
        removeBtn = dialogLayout.findViewById<ImageView>(R.id.img_remove)
        val cameraBtn = dialogLayout.findViewById<ImageView>(R.id.img_camera)
        removeTxt = dialogLayout.findViewById<TextView>(R.id.txt_remove)

        if (binding.imgPhoto.drawable.constantState?.equals(resources.getDrawable(R.drawable.account).constantState) == true){
            removeBtn.visibility = View.GONE
            removeTxt.visibility = View.GONE
        }

        cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, AddPhotoActivity.REQUEST_TAKE_PICTURE)
            dialog.dismiss()
        }

        galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/"
            startActivityForResult(intent, AddPhotoActivity.REQUEST_UPLOAD_PICTURE)
            dialog.dismiss()
        }

        removeBtn.setOnClickListener {
            binding.imgPhoto.setImageDrawable(resources.getDrawable(R.drawable.account))
            dialog.dismiss()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.tbEditProfile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tbEditProfile.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.tbEditProfile.setNavigationOnClickListener {
            finish()
        }
    }
}