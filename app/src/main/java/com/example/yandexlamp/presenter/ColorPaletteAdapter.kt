package com.example.yandexlamp.presenter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yandexlamp.data.model.ColorInfo
import com.example.yandexlamp.data.model.ColorMapper
import com.example.yandexlamp.databinding.ItemColorBinding
import androidx.core.graphics.toColorInt

class ColorPaletteAdapter: RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder>() {
    private var listColorInfos: List<ColorInfo> = listOf()
    var onColorClick: ((ColorInfo) -> Unit)? = null

    class ViewHolder(
        private val binding: ItemColorBinding,
        private val onColorClick: ((ColorInfo) -> Unit)?
    ): RecyclerView.ViewHolder(binding.root) {

        private lateinit var currentColorInfo: ColorInfo

        init {
            binding.root.setOnClickListener {
                if (::currentColorInfo.isInitialized) {
                    onColorClick?.invoke(currentColorInfo)
                }
            }
        }

        fun bind(item: ColorInfo) {
            // устанавливаем данные
            binding.itemColorName.text = item.color

            // меняем цвет кружка
            val hexColor = ColorMapper.getHexColor(item.color)
            val drawable = binding.itemColorCircle.background as? GradientDrawable
            drawable?.setStroke(2, hexColor.toColorInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ctx = parent.context
        val layoutInflater = LayoutInflater.from(ctx)
        val binding = ItemColorBinding.inflate(
            layoutInflater,
            parent,
            false
        )

        return ViewHolder(binding, onColorClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listColorInfos[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = listColorInfos.size

    fun submitList(list: List<ColorInfo>) {
        listColorInfos = list
        notifyDataSetChanged()
    }
}