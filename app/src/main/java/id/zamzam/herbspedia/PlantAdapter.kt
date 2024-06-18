package id.zamzam.herbspedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.zamzam.herbspedia.data.pref.Plant
import id.zamzam.herbspedia.databinding.ItemPlantBinding

// sebelum make api bisa
class PlantAdapter(private var plants: List<Plant>, private val itemClickListener: (Plant) -> Unit) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: Plant) {
            binding.tvTitle.text = plant.nama

            Glide.with(binding.root)
                .load(plant.gambar) // Menggunakan Glide untuk memuat gambar dari URL
                .placeholder(R.drawable.tanaman) // Gambar default jika URL kosong
                .error(R.drawable.tanaman2) // Gambar default jika terjadi error
                .into(binding.ivPlant)

            binding.root.setOnClickListener { itemClickListener(plant) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.bind(plant)
    }

    override fun getItemCount() = plants.size

    fun setData(newPlants: List<Plant>) {
        plants = newPlants
        notifyDataSetChanged()
    }
}

