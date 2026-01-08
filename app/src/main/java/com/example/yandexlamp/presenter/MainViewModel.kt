package com.example.yandexlamp.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yandexlamp.data.model.ColorInfo
import com.example.yandexlamp.domain.GetLampUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getLampUseCase: GetLampUseCase
): ViewModel() {
    private val _brightness = MutableLiveData<Int?>()
    val brightness: LiveData<Int?>
        get() = _brightness

    private val _brightnessLevel = MutableLiveData<com.example.yandexlamp.data.model.BrightnessLevel?>()
    val brightnessLevel: LiveData<com.example.yandexlamp.data.model.BrightnessLevel?>
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
                _brightnessLevel.value = getLampUseCase.getBrightnessLevel()
                _brightness.value = getLampUseCase.getCurrentLevel()
                _colors.value = getLampUseCase.getColors()
                _colorNames.value = getLampUseCase.getColors()?.map { it.name }
                _currentColor.value = getLampUseCase.getCurrentColor()
                _isOn.value = getLampUseCase.getState()

                updateStatusText()
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                _status.value = "Ошибка подключения"
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

    fun setColor(color: ColorInfo) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = getLampUseCase.setColor(color.name)
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