package id.zamzam.herbspedia.ui.scan

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    private val _selectedImageBitmap = MutableLiveData<Bitmap?>()
    val selectedImageBitmap: LiveData<Bitmap?> get() = _selectedImageBitmap

    private val _predictedLabel = MutableLiveData<String>()
    val predictedLabel: LiveData<String> get() = _predictedLabel

    fun setSelectedImageBitmap(bitmap: Bitmap?) {
        _selectedImageBitmap.value = bitmap
    }

    fun setPredictedLabel(label: String) {
        _predictedLabel.value = label
    }
}