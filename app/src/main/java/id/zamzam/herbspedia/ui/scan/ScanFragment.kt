package id.zamzam.herbspedia.ui.scan

import ApiService
import RetrofitInstance
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.zamzam.herbspedia.R
import id.zamzam.herbspedia.data.pref.Plant
import id.zamzam.herbspedia.databinding.FragmentScanBinding
import id.zamzam.herbspedia.ml.ModelHerbPedia
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
//    private var selectedImageBitmap: Bitmap? = null
    private val scanViewModel: ScanViewModel by viewModels()


    private val SELECT_IMAGE_REQUEST_CODE = 1
    private val CAPTURE_IMAGE_REQUEST_CODE = 2

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(context, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.captureBtn.setOnClickListener { openCamera() }
        scanViewModel.selectedImageBitmap.observe(viewLifecycleOwner) { bitmap ->
            if (bitmap != null) {
                binding.imageView.setImageBitmap(bitmap)
            }
        }

        scanViewModel.predictedLabel.observe(viewLifecycleOwner) { label ->
            binding.resView.text = "Predicted: $label"
            binding.showResultBtn.visibility = if (label.isNotEmpty()) View.VISIBLE else View.GONE
        }

        binding.captureBtn.setOnClickListener { checkCameraPermissionAndOpenCamera() }

        binding.selectBtn.setOnClickListener {
            selectImageFromGallery()
        }

        binding.predictBtn.setOnClickListener {
            scanViewModel.selectedImageBitmap.value?.let {
                runModel(it)
            } ?: Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
        }

        binding.showResultBtn.setOnClickListener {
            scanViewModel.predictedLabel.value?.let { label ->
                fetchPlantDescription(label)
            }
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(context, "Camera permission is required to use this feature", Toast.LENGTH_SHORT).show()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE)
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                SELECT_IMAGE_REQUEST_CODE -> {
                    data?.data?.let { uri ->
                        val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
//                        selectedImageBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                        val scaledBitmap  = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                        scanViewModel.setSelectedImageBitmap(scaledBitmap)
//                        binding.imageView.setImageBitmap(bitmap)
                    }
                }
                CAPTURE_IMAGE_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
//                    selectedImageBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
                    val scaledBitmap  = Bitmap.createScaledBitmap(bitmap, 150, 150, true)
//                    binding.imageView.setImageBitmap(bitmap)
                    scanViewModel.setSelectedImageBitmap(scaledBitmap)
                }
            }
        }
    }

    private fun runModel(bitmap: Bitmap) {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)

        val model = ModelHerbPedia.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val result = outputFeature0.floatArray
        val predictedLabel = interpretResults(result)
        scanViewModel.setPredictedLabel(predictedLabel)

        binding.resView.text = "Predicted: $predictedLabel"
        binding.resView.text = "Daun: $predictedLabel"

        binding.showResultBtn.visibility = View.VISIBLE


        binding.showResultBtn.setOnClickListener {
            fetchPlantDescription(predictedLabel)
        }

        model.close()
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 150 * 150 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(150 * 150)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until 150) {
            for (j in 0 until 150) {
                val value = intValues[pixel++]
                byteBuffer.putFloat(((value shr 16 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value shr 8 and 0xFF) / 255.0f))
                byteBuffer.putFloat(((value and 0xFF) / 255.0f))
            }
        }
        return byteBuffer
    }

    private fun interpretResults(result: FloatArray): String {
        val labels = arrayOf("Belimbing Wuluh"
            , "Jambu Biji"
            , "Jeruk Nipis"
            , "Kemangi"
            , "Lidah Buaya"
            , "Nangka"
            , "Pandan"
            , "Pepaya"
            , "Seledri"
            , "Sirih")
        val maxIndex = argMax(result)
        return labels.getOrElse(maxIndex) { "Unknown" }
    }

    private fun fetchPlantDescription(predictedLabel: String) {
        val apiService = RetrofitInstance.instance.create(ApiService::class.java)
        apiService.getPlants().enqueue(object : Callback<List<Plant>> {
            override fun onResponse(call: Call<List<Plant>>, response: Response<List<Plant>>) {
                if (response.isSuccessful && response.body() != null) {
                    val plant = response.body()!!.find { it.nama == predictedLabel }
                    plant?.let {

                        val bundle = Bundle().apply {
                            putString("NAMA_TANAMAN", it.nama)
                            putString("DESKRIPSI_TANAMAN", it.deskripsi)
                            putString("KEGUNAAN_TANAMAN", it.kegunaan)
                            putString("GAMBAR_TANAMAN", it.gambar)
                        }
                        findNavController().navigate(R.id.action_scanFragment_to_detailScanFragment, bundle)
                    } ?: run {
                        Toast.makeText(context, "Plant not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to get plant description", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plant>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun argMax(array: FloatArray): Int {
        return array.indices.maxByOrNull { array[it] } ?: -1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}