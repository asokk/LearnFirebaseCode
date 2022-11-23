package com.example.firebaselearn

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaselearn.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Objects

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Please wait...")
            progressDialog.setMessage("We are creating account")
            progressDialog.setCancelable(false)
            progressDialog.show()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                binding.emailTxt.text.toString().trim(),
                binding.passwordTxt.text.toString().trim()
            ).addOnCompleteListener{ task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    var map = HashMap<String, Any>()
                    map["name"] = binding.nameTxt.text.toString().trim()
                    map["email"] = binding.emailTxt.text.toString().trim()
                    map["password"] = binding.passwordTxt.text.toString().trim()

                    val databaseReference = FirebaseDatabase.getInstance()
                        .reference
                        .child("Users")


                    val id = databaseReference.push().key

                    if (id != null) {
                        databaseReference.child("name").setValue(binding.nameTxt.text.toString().trim())
                        databaseReference.child("email").setValue(binding.emailTxt.text.toString().trim())
                        databaseReference.child("password").setValue(binding.passwordTxt.text.toString().trim())
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    progressDialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                            }
                    }

                    Toast.makeText(this, "User Signup Successful", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.signInButton.setOnClickListener {
            var intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}