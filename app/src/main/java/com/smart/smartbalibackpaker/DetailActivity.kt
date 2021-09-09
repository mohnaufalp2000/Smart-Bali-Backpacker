package com.smart.smartbalibackpaker

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ActivityDetailBinding
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import java.io.ByteArrayOutputStream


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var db: FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var imageUri : Uri
    private val detailPlaceViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(DetailPlaceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
//            setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.color.bg_primary, null))
            setDisplayHomeAsUpEnabled(true)
            title = "Detail"
        }
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE)
        showProgressBarDetails(true)
        if (id != 0 && type != null) {
            getDetails(id)
        }

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        db = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        createGroupChat()
    }

    private fun createGroupChat() {
        binding.btnJoinGroupChat.setOnClickListener {
            val title = binding.tvDetailTitle.text.toString()
            val time = "${System.currentTimeMillis()}"
            val createGroup : HashMap<String, String> = HashMap()
            val imageRef : StorageReference = storage.reference.child("images/$time")
            val imgBitmap = binding.imgDetailPoster.drawable.toBitmap()
            imageUri = getImageUriFromBitmap(imgBitmap)

            imageRef.putFile(imageUri)

            createGroup.apply {
                put("groupId", time)
                put("groupTitle", title)
                put("groupImage", imageUri.toString())
                put("time", time)
                put("createdBy", auth.uid.toString())
            }

            val ref : DatabaseReference = db.getReference("groups")
            ref.child(time).setValue(createGroup)
                .addOnSuccessListener {
                    val addMember : HashMap<String, String> = HashMap()
                    addMember["uid"] = auth.uid.toString()
                    addMember["role"] = "creator"
                    addMember["time"] = time

                    val refMember = db.getReference("groups")
                    refMember.child(time).child("member").child(auth.uid.toString())
                        .setValue(addMember)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Group Succesfully Created", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
//                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun getImageUriFromBitmap(bitmap : Bitmap) : Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bytes)
        val resolver = applicationContext.contentResolver
        val path = MediaStore.Images.Media.insertImage(
            resolver,
            bitmap,
            System.currentTimeMillis().toString(),
            null
        )
        return Uri.parse(path.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showProgressBarDetails(status: Boolean) {
        binding.apply {
            if (status) {
                progressbarDetail.visibility = View.VISIBLE
                container.visibility = View.GONE
            } else {
                progressbarDetail.visibility = View.GONE
                container.visibility = View.VISIBLE
            }
        }
    }

    private fun getDetails(id: Int) {
            detailPlaceViewModel.setSelectedPlace(id)
            detailPlaceViewModel.detailPlace.observe(this, {
                if (it != null) {
                    when (it.status) {
                        Status.LOADING -> showProgressBarDetails(true)
                        Status.SUCCESS -> {
                            populateContent(it.data as TourismDataEntity)
                            showProgressBarDetails(false)
                        }
                        Status.ERROR -> {
                            showProgressBarDetails(false)
                            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    private fun populateContent(content: TourismDataEntity) {
        Glide.with(this)
            .load("http://backpacker.igsindonesia.org/public/storage/pictures/thumbnail/${content.thumbnail}")
            .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
            .into(binding.imgDetailPoster)

        Glide.with(this)
            .load("http://backpacker.igsindonesia.org/public/storage/pictures/thumbnail/${content.thumbnail}")
            .apply(RequestOptions().centerCrop())
            .into(binding.imgDetailBackdrop)

        binding.apply {
            tvDetailTitle.text = content.title
            tvDetailLocation.text = content.address
            tvDetailPlaceDesc.text = content.desc

            supportActionBar?.title = content.title
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
    }
}