package id.zamzam.herbspedia.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import id.zamzam.herbspedia.data.pref.UserPreferences
import id.zamzam.herbspedia.databinding.FragmentProfileBinding
import id.zamzam.herbspedia.ui.login.LoginActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    lateinit var auth : FirebaseAuth
    private lateinit var userPreferences: UserPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        userPreferences = UserPreferences.getInstance(requireContext())

        val userEmail = userPreferences.getUserEmail()
        val userName = userPreferences.getUserName()
        val password = userPreferences.getUserPassword() ?: ""

        binding.tvUsernameProfile2.text = userName
        binding.tvEmailProfile2.text = userEmail
        binding.tvPasswordProfile2.text = getMaskedPassword(password)

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun getMaskedPassword(password: String): String {
        return "*".repeat(password.length)
    }

    private fun logout() {
        context?.let {
            UserPreferences.getInstance(it).clearUserSession()
            val intent = Intent(it, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}