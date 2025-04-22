package com.example.fitnessapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var ref: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().getReference("users")

        val etUserId = findViewById<EditText>(R.id.etUserId)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        val btnAddUser = findViewById<Button>(R.id.btnAddUser)
        val btnViewUsers = findViewById<Button>(R.id.btnViewUsers)
        val btnRemoveUser = findViewById<Button>(R.id.btnRemoveUser)
        val btnUpdateUser = findViewById<Button>(R.id.btnUpdateUser)
        val textViewUsers = findViewById<TextView>(R.id.textViewUsers)

        // **Add User to Firebase Authentication & Database**
        btnAddUser.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(this@AdminLoginActivity, "Email already exists!", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                                    val user = User(userId, email, password)

                                    ref.child(userId).setValue(user).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Toast.makeText(this@AdminLoginActivity, "User Added Successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@AdminLoginActivity, "Failed to add user to database", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    Toast.makeText(this@AdminLoginActivity, "User registration failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminLoginActivity, "Failed to check email", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // **View Users**
        btnViewUsers.setOnClickListener {
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usersList = StringBuilder()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null) {
                            usersList.append("ID: ${user.id}, Email: ${user.email}\n")
                        }
                    }
                    textViewUsers.text = usersList.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminLoginActivity, "Failed to load users", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // **Remove User by Email**
        btnRemoveUser.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userId = snapshot.children.first().key ?: return
                        ref.child(userId).removeValue().addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                val user = auth.currentUser
                                if (user?.email == email) {
                                    user.delete().addOnCompleteListener { authTask ->
                                        if (authTask.isSuccessful) {
                                            Toast.makeText(this@AdminLoginActivity, "User Removed Successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(this@AdminLoginActivity, "Failed to remove user from Firebase Authentication", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, "password")
                                        .addOnCompleteListener { signInTask ->
                                            if (signInTask.isSuccessful) {
                                                val userToDelete = FirebaseAuth.getInstance().currentUser
                                                userToDelete?.delete()?.addOnCompleteListener { deleteTask ->
                                                    if (deleteTask.isSuccessful) {
                                                        Toast.makeText(this@AdminLoginActivity, "User Removed Successfully", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(this@AdminLoginActivity, "Failed to remove user from Firebase Authentication", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(this@AdminLoginActivity, "Failed to sign in for user removal", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                }
                            } else {
                                Toast.makeText(this@AdminLoginActivity, "Failed to remove user from database", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@AdminLoginActivity, "No user found with this email", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AdminLoginActivity, "Failed to check email", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // **Update User Password**
        btnUpdateUser.setOnClickListener {
            val userId = etUserId.text.toString()
            val password = etPassword.text.toString()

            if (userId.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter User ID and New Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            ref.child(userId).child("password").setValue(password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.updatePassword(password)?.addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(this, "User Password Updated Successfully in Database and Authentication", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update password in Firebase Authentication", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Failed to update password in database", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    data class User(val id: String = "", val email: String = "", val password: String = "")
}
