package com.cats32.challenge

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cats32.challenge.repo.PointsRepo
import com.cats32.challenge.utils.saveImageToGallery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val pointsRepo: PointsRepo,
    val application: Application
) : ViewModel() {
    private val TAG = "MainViewModel"

    private val _navigation = Channel<NavDirection>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    private val _state = MutableStateFlow(ViewState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect: Flow<Effect> = _effect.receiveAsFlow()


    val pointsFlow = pointsRepo.pointsAsFlow()

    fun fetchPoints(count: Int) {
        _state.value = ViewState(busy = true)
        viewModelScope.launch {
            val result = pointsRepo.fetchPoints(count)

            _state.update {
                it.copy(
                    busy = false,
                    error = result.isFailure)
            }

            if (result.isFailure) {
                val errorMessage = result.exceptionOrNull()?.toString() ?: "Unknown network error"
                Log.e(TAG, errorMessage)
            } else {
                _navigation.send(NavDirection.ShowChart)
            }
        }
    }

    fun saveImage(image: Bitmap) {
        viewModelScope.launch {
            if (saveImageToGallery(image, application)) {
                _effect.send(Effect.ShowToast(application.getString(R.string.image_saved)))
            } else {
                _effect.send(Effect.ShowToast(application.getString(R.string.image_not_saved)))
            }
        }
    }

    sealed class Effect {
        class ShowToast(val message: String) : Effect()
    }

}