package id.zamzam.herbspedia.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.zamzam.herbspedia.data.pref.UserPreferences
import id.zamzam.herbspedia.databinding.ActivitySplashBinding
import id.zamzam.herbspedia.ui.login.LoginActivity
import id.zamzam.herbspedia.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periksa apakah pengguna sudah masuk
        if (UserPreferences.getInstance(this).isLoggedIn()) {
            // Jika sudah masuk, arahkan ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Jika belum masuk, tampilkan tombol "Get Started"
            binding.btnGetStarted.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}