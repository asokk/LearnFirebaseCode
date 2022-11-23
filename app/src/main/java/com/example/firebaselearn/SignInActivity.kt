package com.example.firebaselearn

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaselearn.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signInButton.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Please wait...")
            progressDialog.setMessage("We are creating account")
            progressDialog.setCancelable(false)
            progressDialog.show()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.emailTxt.text.toString().trim(),
                binding.passwordTxt.text.toString().trim()
            ).addOnCompleteListener{ task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "User SignIn Successful", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.signUpButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}