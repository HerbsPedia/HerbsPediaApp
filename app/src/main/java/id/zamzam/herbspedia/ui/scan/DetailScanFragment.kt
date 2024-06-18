package id.zamzam.herbspedia.ui.scan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.zamzam.herbspedia.databinding.FragmentDetailScanBinding

class DetailScanFragment : Fragment() {

    private var _binding: FragmentDetailScanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val resultText = arguments?.getString("PREDICTED_LABEL")
//        binding.textView.text = resultText

        val namaTanaman = arguments?.getString("NAMA_TANAMAN")
        val deskripsiTanaman = arguments?.getString("DESKRIPSI_TANAMAN")
        val kegunaanTanaman = arguments?.getString("KEGUNAAN_TANAMAN")
        val gambarTanaman = arguments?.getString("GAMBAR_TANAMAN")

        binding.textViewNameScan.text = namaTanaman
        binding.textViewDescriptionScan.text = deskripsiTanaman
        binding.textViewUseScan.text = kegunaanTanaman
        // Load image using a library like Glide or Picasso
//        Glide.with(this).load(gambarTanaman).into(binding.imageView_Plant)
//        Glide.with(this)
//            .load(gambarTanaman)
//            .placeholder(R.drawable.tanaman)
//            .error(R.drawable.tanaman2)
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    // Hide progress bar when image loading failed
//                    binding.progressBar.visibility = View.GONE
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    // Hide progress bar when image loaded successfully
//                    binding.progressBar.visibility = View.GONE
//                    return false
//                }
//            })
//            .into(binding.imageViewPlant)
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnShare.setOnClickListener {
            sharePlant()
        }
    }

    private fun sharePlant() {
        val plantName = binding.textViewNameScan.text.toString()
        val plantDescription = binding.textViewDescriptionScan.text.toString()
        val plantUse = binding.textViewUseScan.text.toString()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Plant Information")
            putExtra(Intent.EXTRA_TEXT, "Check out this plant:\n$plantName\n$plantDescription\n$plantUse")
        }

        val shareIntentChooser = Intent.createChooser(shareIntent, "Share Plant Information")
        startActivity(shareIntentChooser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}