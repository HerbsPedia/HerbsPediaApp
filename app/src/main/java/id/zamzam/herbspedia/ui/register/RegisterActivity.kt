package id.zamzam.herbspedia.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import id.zamzam.herbspedia.databinding.ActivityRegisterBinding
import id.zamzam.herbspedia.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("RegisterActivity", "Register layout loaded")

        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val nama = binding.edtNamaRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()
            if (validateEmail(email)  && validateNama(nama) && validatePassword(password)) {
                checkIfEmailExists(email, nama, password)
            }
        }


        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }



    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            binding.edtEmailRegister.error = "Email is required"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailRegister.error = "Invalid email format"
            return false
        }
        return true
    }

    private fun validateNama(age: String): Boolean {
        if (age.isEmpty()) {
            binding.edtNamaRegister.error = "Nama is required"
            return false
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isEmpty()) {
            binding.edtPasswordRegister.error = "Password is required"
            return false
        } else if (password.length < 6) {
            binding.edtPasswordRegister.error = "Password must be at least 6 characters"
            return false
        }
        return true
    }
    private fun checkIfEmailExists(email: String, nama: String, password: String) {
        db.collection("user")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // Email already exists, show error message
                    binding.edtEmailRegister.error = "Email already exists"
                } else {
                    // Email doesn't exist, proceed with registration
                    registerUser(email, nama, password)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun registerUser(email: String, nama: String, password: String) {
        val user = hashMapOf(
            "Email" to email,
            "Nama" to nama,
            "Password" to password
        )

        db.collection("user")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}