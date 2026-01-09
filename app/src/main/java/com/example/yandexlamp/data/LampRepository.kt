package com.example.yandexlamp.data


import android.util.Log
import com.example.yandexlamp.data.model.BrightnessLevel
import com.example.yandexlamp.data.model.ColorInfo
import javax.inject.Inject

interface LampRepository {
    // уровни яркости
    suspend fun getBrightnessLevel(): BrightnessLevel?
    suspend fun setBrightnessLevel(level: Int): Boolean
    suspend fun getCurrentLevel(): Int?

    // цвета
    suspend fun getColors(): List<ColorInfo>?
    suspend fun setColor(color: String): Boolean
    suspend fun getCurrentColor(): ColorInfo?
    suspend fun getColorNamesOnly(): List<String>?

    // вкл/выкл
    suspend fun getState(): Boolean?
    suspend fun turnOn(): Boolean
    suspend fun turnOff(): Boolean
}

class LampRepositoryImpl @Inject constructor(
    private val service: LampService
): LampRepository {
    companion object {
        private const val TAG = "LampRepository"
        private const val TAG_RESPONSE = "LampRepository-Response"
        private const val TAG_ERROR = "LampRepository-Error"
    }

    override suspend fun getBrightnessLevel(): BrightnessLevel? {
        Log.i(TAG, "getBrightnessLevel() запрос к API")
        return try {
            val response = service.getBrightnessLevel()

            logResponse("getBrightnessLevel", response)

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(TAG, "Уровни яркости получены: min=${body?.min}, max=${body?.max}")
                body
            } else {
                Log.w(TAG, "Ошибка получения уровней яркости")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении уровней яркости: ${e.message}", e)
            null
        }
    }

    override suspend fun setBrightnessLevel(level: Int): Boolean {
        Log.i(TAG, "setBrightnessLevel($level) запрос к API")
        return try {
            val response = service.setBrightnessLevel(level)

            logResponse("setBrightnessLevel", response)

            if (response.isSuccessful) {
                Log.i(TAG, "Яркость установлена на $level успешно")
                true
            } else {
                Log.w(TAG, "Не удалось установить яркость $level")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при установке яркости: ${e.message}", e)
            false
        }
    }

    override suspend fun getCurrentLevel(): Int? {
        Log.i(TAG, "getCurrentLevel() запрос к API")
        return try {
            val response = service.getCurrentLevel()

            logResponse("getCurrentLevel", response)

            if (response.isSuccessful) {
                val level = response.body()
                Log.d(TAG, "Текущий уровень яркости: $level")
                level
            } else {
                Log.w(TAG, "Ошибка получения текущего уровня яркости, возвращаем 0")
                0
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении текущего уровня: ${e.message}", e)
            0
        }
    }

    override suspend fun getColors(): List<ColorInfo>? {
        Log.i(TAG, "getColors() запрос к API")
        return try {
            val response = service.getColors()

            logResponse("getColors", response)

            if (response.isSuccessful) {
                val colors = response.body()
                Log.d(TAG, "Получено цветов: ${colors?.size ?: 0}")
                colors?.forEachIndexed { index, color ->
                    Log.v(TAG, "Цвет #$index: name=${color.name}")
                }
                colors
            } else {
                Log.w(TAG, "Ошибка получения списка цветов")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении цветов: ${e.message}", e)
            null
        }
    }

    override suspend fun setColor(color: String): Boolean {
        Log.i(TAG, "setColor($color) запрос к API")
        return try {
            val response = service.setColor(color)

            logResponse("setColor", response)

            if (response.isSuccessful) {
                Log.i(TAG, "Цвет '$color' установлен успешно")
                true
            } else {
                Log.w(TAG, "Не удалось установить цвет '$color'")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при установке цвета: ${e.message}", e)
            false
        }
    }

    override suspend fun getCurrentColor(): ColorInfo? {
        Log.i(TAG, "getCurrentColor() запрос к API")
        return try {
            val response = service.getCurrentColor()

            logResponse("getCurrentColor", response)

            if (response.isSuccessful) {
                val color = response.body()
                Log.d(TAG, "Текущий цвет: name=${color?.name}")
                color
            } else {
                Log.w(TAG, "Ошибка получения текущего цвета")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении текущего цвета: ${e.message}", e)
            null
        }
    }

    override suspend fun getColorNamesOnly(): List<String>? {
        Log.i(TAG, "getColorNamesOnly() запрос к API")
        return try {
            val response = service.getColorNamesOnly()

            logResponse("getColorNamesOnly", response)

            if (response.isSuccessful) {
                val names = response.body()
                Log.d(TAG, "Получено названий цветов: ${names?.size ?: 0}")
                names?.forEach { Log.v(TAG, "Название цвета: $it") }
                names
            } else {
                Log.w(TAG, "Ошибка получения названий цветов")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении названий цветов: ${e.message}", e)
            null
        }
    }

    override suspend fun getState(): Boolean? {
        Log.i(TAG, "getState() запрос к API")
        return try {
            val response = service.getState()

            logResponse("getState", response)

            if (response.isSuccessful) {
                val state = response.body()
                Log.d(TAG, "Текущее состояние лампы: ${if (state == true) "ВКЛ" else "ВЫКЛ"}")
                state
            } else {
                Log.w(TAG, "Ошибка получения состояния лампы")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при получении состояния: ${e.message}", e)
            null
        }
    }

    override suspend fun turnOn(): Boolean {
        Log.i(TAG, "turnOn() запрос к API")
        return try {
            val response = service.turnOn()

            logResponse("turnOn", response)

            if (response.isSuccessful) {
                Log.i(TAG, "Лампа включена успешно")
                true
            } else {
                Log.w(TAG, "Не удалось включить лампу")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при включении лампы: ${e.message}", e)
            false
        }
    }

    override suspend fun turnOff(): Boolean {
        Log.i(TAG, "turnOff() запрос к API")
        return try {
            val response = service.turnOff()

            logResponse("turnOff", response)

            if (response.isSuccessful) {
                Log.i(TAG, "Лампа выключена успешно")
                true
            } else {
                Log.w(TAG, "Не удалось выключить лампу")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Исключение при выключении лампы: ${e.message}", e)
            false
        }

    }


private fun <T> logResponse(methodName: String, response: retrofit2.Response<T>) {
    val statusCode = response.code()
    val isSuccess = response.isSuccessful

    // Логируем статус код и успешность
    if (isSuccess) {
        Log.i(TAG_RESPONSE, "$methodName -> Status: $statusCode (SUCCESS)")
    } else {
        Log.w(TAG_RESPONSE, "$methodName -> Status: $statusCode (ERROR)")
    }

    // Логируем тело ответа (для успешных запросов)
    if (isSuccess) {
        val body = response.body()
        Log.d(TAG_RESPONSE, "$methodName -> Body: $body")

        // Детальное логирование для определенных типов данных
        when (body) {
            is List<*> -> {
                Log.v(TAG_RESPONSE, "$methodName -> List size: ${body.size}")
            }
            is BrightnessLevel -> {
                Log.v(TAG_RESPONSE, "$methodName -> BrightnessLevel: min=${body.min}, max=${body.max}")
            }
            is ColorInfo -> {
                Log.v(TAG_RESPONSE, "$methodName -> ColorInfo: name=${body.name}")
            }
            is Boolean -> {
                Log.v(TAG_RESPONSE, "$methodName -> Boolean: $body")
            }
            is Int -> {
                Log.v(TAG_RESPONSE, "$methodName -> Int: $body")
            }
            else -> {
                Log.v(TAG_RESPONSE, "$methodName -> Body type: ${body?.toString() ?: "null"}")
            }
        }
    } else {
        // Логируем ошибки
        val errorBody = response.errorBody()?.string()
        Log.e(TAG_ERROR, "$methodName -> Error $statusCode: $errorBody")

        // Логируем заголовки при ошибке
        response.headers().forEach { header ->
            Log.d(TAG_ERROR, "$methodName -> Header: ${header.first}=${header.second}")
        }
    }

    // Логируем заголовки запроса (опционально)
    Log.v(TAG_RESPONSE, "$methodName -> Headers:")
    response.headers().forEach { header ->
        Log.v(TAG_RESPONSE, "  ${header.first}: ${header.second}")
    }

    // Логируем время выполнения (можно добавить если нужно)
    Log.d(TAG_RESPONSE, "$methodName -> Raw response: $response")
}
}
