package com.smart.smartbalibackpaker.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.dashboard.DashboardFragment
import com.smart.smartbalibackpaker.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var dialog: AlertDialog
    private var callbackManager = CallbackManager.Factory.create()
    private val binding by lazy {ActivityLoginBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginFacebook.setReadPermissions(listOf("email", "public_profile"))
        binding.loginFacebook.registerCallback(callbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                val graphRequest = GraphRequest.newMeRequest(result?.accessToken){`object`, response ->
                    getFacebookData(`object`)
                }
                val param = Bundle()
                param.putString("fields", "id,email,name")
                graphRequest.parameters = param
                graphRequest.executeAsync()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }
        })

        binding.apply{
            btnLogin.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            txtForgotPassword.setOnClickListener {
                showRecoverPassDialog()
            }
            txtSignUp.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }
        loginUser()
    }

    private fun getFacebookData(obj: JSONObject?){
        val profilePic = "https://graph.facebook.com/${obj?.getString("id")}/picture?width=200&height=200"
        val name = obj?.getString("name")
        val email = obj?.getString("email")

//        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//        intent.apply {
//            putExtra(DashboardFragment.FACEBOOK_NAME, name)
//            putExtra(DashboardFragment.FACEBOOK_IMAGE, profilePic)
//            putExtra(DashboardFragment.FACEBOOK_EMAIL, email)
//        }
//        startActivity(intent)
    }

    private fun showRecoverPassDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_reset_layout, null)

        dialogBuilder.apply {
            setTitle("Recover Password")
            setView(dialogLayout)
        }
        val emailEt = dialogLayout.findViewById<EditText>(R.id.edt_email)


        dialogBuilder.apply {
            setPositiveButton("Recover") { _,_ ->
                val email = emailEt.text.toString().trim()
                recoverEmail(email)
            }
            setNegativeButton("Cancel") {
                    dialog, _ -> dialog.dismiss()
            }
            create().show()
        }

    }

    private fun recoverEmail(email: String) {
        val dialogLoading = AlertDialog.Builder(this)
        val inflater = layoutInflater
        dialogLoading.apply{
            setView(inflater.inflate(R.layout.loading_dialog, null))
            setCancelable(true)
        }

        dialog = dialogLoading.create()
        dialog.show()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener {
                dialog.dismiss()
                if (it.isSuccessful){
                    Toast.makeText(this, "Please check your email to recover password", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Please try again later", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun loginUser() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if(email.isEmpty()){
                binding.etEmail.apply {
                    error = "Email Harus Diisi!"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.apply {
                    error = "Email Tidak Valid!"
                    requestFocus()
                }
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6){
                binding.etPassword.apply {
                    error = "Password harus lebih dari 6 karakter"
                    requestFocus()
                }
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Intent(this@LoginActivity, MainActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null){
            Intent(this@LoginActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}