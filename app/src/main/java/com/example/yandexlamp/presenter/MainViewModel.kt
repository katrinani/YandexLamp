package com.example.yandexlamp.presenter

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexlamp.data.model.BrightnessLevel
import com.example.yandexlamp.data.model.ColorInfo
import com.example.yandexlamp.domain.GetLampUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getLampUseCase: GetLampUseCase
): ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
        private const val TAG_DETAILED = "MainViewModel-Detailed"
    }

    private val _brightness = MutableLiveData<Int?>()
    val brightness: LiveData<Int?>
        get() = _brightness

    private val _brightnessLevel = MutableLiveData<BrightnessLevel?>()
    val brightnessLevel: LiveData<BrightnessLevel?>
        get() = _brightnessLevel

    private val _currentColor = MutableLiveData<ColorInfo?>()
    val currentColor: LiveData<ColorInfo?>
        get() = _currentColor

    private val _colors = MutableLiveData<List<ColorInfo>?>()
    val colors: LiveData<List<ColorInfo>?>
        get() = _colors

    private val _colorNames = MutableLiveData<List<String>?>()
    val colorNames: LiveData<List<String>?>
        get() = _colorNames

    private val _isOn = MutableLiveData<Boolean>()
    val isOn: LiveData<Boolean>
        get() = _isOn

    private val _status = MutableLiveData("Загрузка...")
    val status: LiveData<String>
        get() = _status

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    init {
        loadAllData()
    }

    fun loadAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                Log.i(TAG, "Начало загрузки всех данных")

                _brightnessLevel.value = getLampUseCase.getBrightnessLevel()
                Log.d(TAG_DETAILED, "Яркость загружена: ${_brightnessLevel.value}")

                _brightness.value = getLampUseCase.getCurrentLevel()
                Log.d(TAG_DETAILED, "Текущий уровень: ${_brightness.value}")

                _colors.value = getLampUseCase.getColors()
                Log.d(TAG_DETAILED, "Цвета загружены: ${_colors.value?.size ?: 0} шт.")

                _colorNames.value = getLampUseCase.getColors()?.map { it.color }
                Log.d(TAG_DETAILED, "Названия цветов: ${_colorNames.value}")

                _currentColor.value = getLampUseCase.getCurrentColor()
                Log.d(TAG_DETAILED, "Текущий цвет: ${_currentColor.value?.color}")

                _isOn.value = getLampUseCase.getState()
                Log.d(TAG_DETAILED, "Состояние лампы: ${(_isOn.value == true)}")

                updateStatusText()
                Log.i(TAG, "Данные успешно загружены")
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                _status.value = "Ошибка подключения: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setBrightness(level: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = getLampUseCase.setBrightnessLevel(level)
                if (success) {
                    _brightness.value = level
                    updateStatusText()
                }
            } catch (e: Exception) {
                _error.value = "Ошибка установки яркости: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setColor(color: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = getLampUseCase.setColor(color)
                if (success) {
                    getLampUseCase.getCurrentColor()?.let {
                        _currentColor.value = it
                    }
                    updateStatusText()
                }
            } catch (e: Exception) {
                _error.value = "Ошибка установки цвета: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun togglePower() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val currentState = _isOn.value
                val success = if (currentState == true) {
                    getLampUseCase.turnOff()
                } else {
                    getLampUseCase.turnOn()
                }

                if (success) {
                    _isOn.value = !(currentState ?: false)
                    updateStatusText()
                }
            } catch (e: Exception) {
                _error.value = "Ошибка переключения питания: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setBrightnessToMin() {
        brightnessLevel.value?.min?.let { min ->
            setBrightness(min)
        }
    }

    fun setBrightnessTo50() {
        setBrightness(50)
    }

    fun setBrightnessToMax() {
        brightnessLevel.value?.max?.let { max ->
            setBrightness(max)
        }
    }

    private fun updateStatusText() {
        val brightnessText = _brightness.value?.let { "$it%" } ?: "--"
        val colorText = _currentColor.value?.name ?: "--"
        val stateText = if (_isOn.value == true) "ВКЛ" else "ВЫКЛ"

        _status.value = "Яркость: $brightnessText, Цвет: $colorText, Состояние: $stateText"
    }
}