package id.zamzam.herbspedia.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.data.pref.UserPreferences
import id.zamzam.herbspedia.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var userPreferences: UserPreferences
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = UserPreferences.getInstance(requireContext())

        val userName = userPreferences.getUserName() ?: "Pengguna"

        binding.tvHello.text = userName

        binding.itemPlant.setOnClickListener {
            navigateToListPlantFragment("Akar")
        }

        binding.itemPlant2.setOnClickListener {
            navigateToListPlantFragment("Daun")
        }

        binding.itemPlant3.setOnClickListener {
            navigateToListPlantFragment("Batang")
        }
    }

    private fun navigateToListPlantFragment(category: String) {
        val bundle = Bundle().apply {
            putString("category", category)
        }
        findNavController().navigate(R.id.action_homeFragment_to_listPlantFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}