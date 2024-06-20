package id.zamzam.herbspedia.ui.home

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.databinding.FragmentDetailPlantBinding


class DetailPlantFragment : Fragment() {

    private var _binding: FragmentDetailPlantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plantName = arguments?.getString("plantName") ?: ""
        val plantImageUrl = arguments?.getString("plantImageRes") ?: ""
        val plantDescription = arguments?.getString("plantDescription") ?: ""
        val plantUse = arguments?.getString("plantUse") ?: ""

        binding.progressBar.visibility = View.VISIBLE

        binding.textViewName.text = plantName
        binding.textViewDescription.text = plantDescription
        binding.textViewUse.text = plantUse

        Glide.with(this)
            .load(plantImageUrl)
            .placeholder(R.drawable.tanaman)
            .error(R.drawable.tanaman2)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.imageViewPlant)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShare.setOnClickListener {
            sharePlant()
        }
    }

    private fun sharePlant() {
        val plantName = binding.textViewName.text.toString()
        val plantDescription = binding.textViewDescription.text.toString()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Plant Information")
            putExtra(Intent.EXTRA_TEXT, "Check out this plant:\n$plantName\n$plantDescription")
        }

        val shareIntentChooser = Intent.createChooser(shareIntent, "Share Plant Information")
        startActivity(shareIntentChooser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}