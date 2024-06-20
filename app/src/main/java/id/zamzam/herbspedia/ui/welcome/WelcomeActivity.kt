package id.zamzam.herbspedia.ui.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.zamzam.herbspedia.data.pref.UserPreferences
import id.zamzam.herbspedia.databinding.ActivityWelcomeBinding
import id.zamzam.herbspedia.ui.login.LoginActivity
import id.zamzam.herbspedia.ui.main.MainActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (UserPreferences.getInstance(this).isLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding.button.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}