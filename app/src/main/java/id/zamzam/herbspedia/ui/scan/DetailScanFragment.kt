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

        val namaTanaman = arguments?.getString("NAMA_TANAMAN")
        val deskripsiTanaman = arguments?.getString("DESKRIPSI_TANAMAN")
        val kegunaanTanaman = arguments?.getString("KEGUNAAN_TANAMAN")

        binding.textViewNameScan.text = namaTanaman
        binding.textViewDescriptionScan.text = deskripsiTanaman
        binding.textViewUseScan.text = kegunaanTanaman

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