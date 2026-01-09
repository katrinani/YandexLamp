package com.example.yandexlamp.presenter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.yandexlamp.R
import com.example.yandexlamp.appComponent
import com.example.yandexlamp.databinding.MainFragmentBinding
import com.example.yandexlamp.di.viewModel.ViewModeFactory
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class MainFragment: Fragment(R.layout.main_fragment) {
    companion object {
        private const val TAG = "MainFragment"
        private const val TAG_DETAILED = "MainFragment-Detailed"
    }

    private val binding: MainFragmentBinding by viewBinding(
        MainFragmentBinding::bind
    )

    @Inject
    lateinit var viewModeFactory: ViewModeFactory

    private val viewModel: MainViewModel by viewModels{ viewModeFactory }
    private var adapter: ColorPaletteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.colorRecycler.layoutManager = GridLayoutManager(requireContext(), 4)

        adapter = ColorPaletteAdapter({ colorInfo ->
            Log.d(TAG_DETAILED, "Установлен цвет: ${colorInfo.color}")
            viewModel.setColor(colorInfo.color)
        })
        binding.colorRecycler.adapter = this.adapter

        viewModel.colors.observe(viewLifecycleOwner) { colors ->
            colors?.let {
                adapter?.submitList(it)
            }
        }
        // -------- наполнение данными
        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.statusText.text = status ?: ""
        }

        viewModel.isOn.observe(viewLifecycleOwner) { isOn ->
            Log.d(TAG_DETAILED, "Установлен свитч: $isOn")
            binding.switchLight.isEnabled = isOn == true
            binding.switchLight.isChecked = isOn == true
        }

        viewModel.brightness.observe(viewLifecycleOwner) { brightness ->
            if (brightness != null) {
                binding.currentBrightnessValue.text = "Текущая: $brightness"
                binding.brightnessSeekbar.progress = brightness
            }
        }

        viewModel.currentColor.observe(viewLifecycleOwner) { color ->
            binding.currentColorValue.text = "Текущий: ${color?.color}"
        }

        // Включение/выключение
        binding.switchLight.setOnClickListener {
            viewModel.togglePower()
        }

        // изменение ползунка яркости
        binding.brightnessSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    Log.d(TAG_DETAILED, "Передвинут ползунок яркости: $progress")
                    viewModel.setBrightness(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        // установка минимальной яркости
        binding.btnBrightnessMin.setOnClickListener{
            viewModel.setBrightnessToMin()
        }

        // установка 50%
        binding.btnBrightness50.setOnClickListener {
            viewModel.setBrightnessTo50()
        }

        // установка максимальной яркости
        binding.btnBrightnessMax.setOnClickListener {
            viewModel.setBrightnessToMax()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        val component = context.appComponent
        component.inject(this)
        super.onAttach(context)
    }
}