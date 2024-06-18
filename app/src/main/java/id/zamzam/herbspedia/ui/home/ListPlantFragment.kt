package id.zamzam.herbspedia.ui.home

import RetrofitInstance
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.zamzam.herbspedia.PlantAdapter
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.data.pref.Plant
import id.zamzam.herbspedia.databinding.FragmentListPlantBinding
import kotlinx.coroutines.launch

class ListPlantFragment : Fragment() {

    private var _binding: FragmentListPlantBinding? = null
    private val binding get() = _binding!!

    private var currentCategory: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString("category")

        Log.d("ListPlantFragment", "Received category: $category")

        if (!category.isNullOrEmpty()) {
            currentCategory = category
            fetchPlantsByCategoryFromApi(category)
        } else {
            Log.e("ListPlantFragment", "Category is null or empty.")
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun fetchPlantsByCategoryFromApi(category: String) {
        binding.progressBar.visibility = View.VISIBLE
        val service = RetrofitInstance.api
        lifecycleScope.launch {
            try {
                val response = service.getPlantsByCategory(category)
                if (response.isSuccessful) {
                    val plants = response.body()
                    plants?.let {
                        setupRecyclerView(it)
                    }
                } else {
                    Log.e("ListPlantFragment", "Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ListPlantFragment", "Error: ${e.message}")
            } finally {
            binding.progressBar.visibility = View.GONE // Sembunyikan ProgressBar setelah permintaan selesai
        }
        }
    }

    private fun setupRecyclerView(plants: List<Plant>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = PlantAdapter(plants) { plant ->
            Log.d("ListPlantFragment", "Clicked plant: ${plant.nama}")
            val bundle = Bundle().apply {
                putString("plantName", plant.nama)
                putString("plantDescription", plant.deskripsi)
                putString("plantImageRes", plant.gambar)
                putString("plantUse", plant.kegunaan)
            }
            findNavController().navigate(R.id.action_listPlantFragment_to_detailPlantFragment, bundle)
        }

        setCategoryTitle(currentCategory)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setCategoryTitle(category: String?) {
        binding.tvTitle.text = "Tanaman $category"
    }
}