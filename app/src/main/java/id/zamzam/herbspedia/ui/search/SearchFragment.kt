package id.zamzam.herbspedia.ui.search

import ApiService
import RetrofitInstance
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import id.zamzam.herbspedia.PlantAdapter
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.data.pref.Plant
import id.zamzam.herbspedia.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val plantAdapter by lazy {
        PlantAdapter(ArrayList()) { plant ->
            val bundle = Bundle().apply {
                putString("plantName", plant.nama)
                putString("plantDescription", plant.deskripsi)
                putString("plantImageRes", plant.gambar)
                putString("plantUse", plant.kegunaan)
            }
            findNavController().navigate(R.id.action_searchFragment_to_detailPlantFragment, bundle)
        }
    }

    private val apiService: ApiService by lazy {
        RetrofitInstance.api
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlant.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlant.adapter = plantAdapter

//        binding.btnSearch.setOnClickListener {
//            val query = binding.etQuery.text.toString().trim()
//            if (query.isNotEmpty()) {
//                searchPlants(query)
//            } else {
//                Toast.makeText(requireContext(), "Please enter a search query", Toast.LENGTH_SHORT).show()
//            }
//        }

        binding.etQuery.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    searchPlants(query)
                } else {
                    plantAdapter.setData(emptyList())
                }
            }
        })
    }

    private fun searchPlants(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        apiService.getPlants().enqueue(object : Callback<List<Plant>> {
            override fun onResponse(call: Call<List<Plant>>, response: Response<List<Plant>>) {
                if (response.isSuccessful) {
                    val plants = response.body()
                    if (plants != null) {
                        val filteredPlants = plants.filter { it.nama.contains(query, ignoreCase = true) }
                        plantAdapter.setData(filteredPlants)
                    } else {
                        Toast.makeText(requireContext(), "No plants found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to search plants", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Plant>>, t: Throwable) {
                Toast.makeText(requireContext(), "Failed to search plants: ${t.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}