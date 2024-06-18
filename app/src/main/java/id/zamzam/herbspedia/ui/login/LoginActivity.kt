package id.zamzam.herbspedia.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import id.zamzam.herbspedia.data.pref.UserPreferences
import id.zamzam.herbspedia.databinding.ActivityLoginBinding
import id.zamzam.herbspedia.ui.main.MainActivity
import id.zamzam.herbspedia.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private lateinit var db: FirebaseFirestore
    private lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        userPreferences = UserPreferences.getInstance(this)

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()
//            loginUser(email, password)
            db.collection("user")
                .whereEqualTo("Email", email)
                .whereEqualTo("Password", password)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val userDoc = documents.documents[0]
                        val userEmail = userDoc.getString("Email") ?: email
                        val userName = userDoc.getString("Nama") ?: "Pengguna"
//                        val password = userDoc.getString("Password}") ?: ""
                        userPreferences.saveUserSession(userEmail, userName, password)
                        Toast.makeText(
                            baseContext,
                            "Login Successful.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            baseContext,
                            "Login failed: Invalid credentials.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(baseContext, "Error: ${exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }

    }
}
