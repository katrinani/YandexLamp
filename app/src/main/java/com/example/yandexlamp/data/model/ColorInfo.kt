package com.example.yandexlamp.data.model

data class ColorInfo(
    val id: Int,
    val name: String,
    val type: String,
    val color: String
)

// для работы с hex цветами
object ColorMapper {
    private val colorNameToHex = mapOf(
        "red" to "#FF0000",
        "green" to "#00FF00",
        "blue" to "#0000FF",
        "white" to "#FFFFFF",
        "teal" to "#008080",
        "coral" to "#FF7F50",
        "peru" to "#CD853F",
        "brown" to "#A52A2A",
        "gold" to "#FFD700",
        "maroon" to "#800000",
        "mediumpurple" to "#9370DB",
        "tomato" to "#FF6347",
        "pink" to "#FFC0CB",
        "slateblue" to "#6A5ACD",
        "indigo" to "#4B0082",
        "cyan" to "#00FFFF",
        "seagreen" to "#2E8B57",
        "purple" to "#800080",
        "yellow" to "#FFFF00"
    )

    fun getHexColor(colorName: String): String {
        return colorNameToHex[colorName.lowercase()] ?: "#000000"
    }
}
