package com.dooze.recognition

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.base.extensions.lazyFast
import com.google.mlkit.vision.text.*
import com.google.mlkit.vision.common.*
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.lang.RuntimeException
import kotlin.coroutines.resumeWithException

class ImportImageRecognitionViewModel : ViewModel() {

    private val _recognitionState = MutableLiveData<LoadingState>(Done)
    val recognitionState: LiveData<LoadingState> = _recognitionState

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> = _imageUri

    private val _recognitionResult = MutableStateFlow<Text?>(null)
    val recognitionResult by lazyFast {
        val flow = MutableStateFlow<Text?>(null)
        viewModelScope.launch {
            _recognitionResult.collect {
                flow.value = it
            }
        }
        flow.asStateFlow()
    }


    private val client by lazy {
        TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())
    }

    fun reRecognition(context: Context) {
        _imageUri.value?.let {
            recognition(context, it)
        }
    }

    fun recognitionImage(context: Context, uri: Uri) {
        _imageUri.value = uri
        recognition(context, uri)
    }

    fun recognition(context: Context, uri: Uri) {
        if (recognitionState.value == Loading) {
            return
        }
        viewModelScope.launch {
            _recognitionState.value = Loading
            val text = suspendCancellableCoroutine<Text> { continuation ->
                client.detectorType
                client.process(InputImage.fromFilePath(context, uri))
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
                    .addOnCanceledListener {
                        continuation.resumeWithException(RuntimeException("task was cancelled"))
                    }.addOnSuccessListener {
                        continuation.resumeWith(Result.success(it))
                    }
            }
            _recognitionResult.emit(text)

        }.invokeOnCompletion {
            _recognitionState.value = Done
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}