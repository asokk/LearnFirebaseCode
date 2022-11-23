package com.example.firebaselearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaselearn.databinding.ActivityMainBinding
import com.example.firebaselearn.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        getUserDetails()
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            var map = HashMap<String, Any>()
            map["name"] = binding.userNameTxt.text.toString().trim()
            map["email"] = binding.emailTxt.text.toString().trim()
            map["password"] = binding.passwordTxt.text.toString().trim()
            map["gender"] = binding.genderTxt.text.toString().trim()
            map["age"] = binding.ageTxt.text.toString().trim()

            FirebaseAuth.getInstance().currentUser?.let { user ->
                FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(user.uid)
                    .updateChildren(map)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "User SignIn Successful", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    private fun getUserDetails() {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            FirebaseDatabase.getInstance()
                .reference
                .child("Users")
               // .child(user.uid)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val model = snapshot.getValue(UserModel::class.java)
                        binding.userNameTxt.setText(model?.name)
                        binding.emailTxt.setText(model?.email)
                        binding.passwordTxt.setText(model?.password)
                        binding.genderTxt.setText(model?.gender)
                        binding.ageTxt.setText(model?.age)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}