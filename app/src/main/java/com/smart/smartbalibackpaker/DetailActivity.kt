package com.smart.smartbalibackpaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ActivityDetailBinding
import com.smart.smartbalibackpaker.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.vo.Status

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
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