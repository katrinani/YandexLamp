package com.example.yandexlamp.presenter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yandexlamp.R
import com.example.yandexlamp.appComponent
import com.example.yandexlamp.databinding.MainFragmentBinding
import com.example.yandexlamp.di.viewModel.ViewModeFactory
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class MainFragment: Fragment(R.layout.main_fragment) {
    private val binding: MainFragmentBinding by viewBinding(
        MainFragmentBinding::bind
    )

    @Inject
    lateinit var viewModeFactory: ViewModeFactory

    private val viewModel: MainViewModel by viewModels{ viewModeFactory }
    private var adapter: ColorPaletteAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.colorRecycler.layoutManager = LinearLayoutManager(requireContext())
        adapter = ColorPaletteAdapter().apply {
            onColorClick = { colorInfo ->
                viewModel.setColor(colorInfo)
            }
        }
        binding.colorRecycler.adapter = this.adapter
        // TODO цвета в ряд
        viewModel.colors.observe(viewLifecycleOwner) { colors ->
            colors?.let {
                adapter?.submitList(it)
            }
        }
        // наполнение данными
        viewModel.status.observe(viewLifecycleOwner) { status ->
            binding.statusText.text = status ?: ""
            Toast.makeText(
                requireContext(),
                status,
                Toast.LENGTH_LONG
            ).show()
        }
        // TODO текущая яркость в 0, текущий цвет null
        // ползунок работает,кнопки тоже работают
        // TODO обложить тостами все

        viewModel.isOn.observe(viewLifecycleOwner) { isOn ->
            binding.switchLight.isEnabled = isOn == true
        }

        viewModel.brightness.observe(viewLifecycleOwner) { brightness ->
            if (brightness != null) {
                binding.currentBrightnessValue.text = "Текущая: $brightness"
                binding.brightnessSeekbar.progress = brightness
            }
        }

        viewModel.currentColor.observe(viewLifecycleOwner) { color ->
            binding.currentColorValue.text = "Текущий: ${color?.name}"
        }

        // Включение/выключение
        binding.switchLight.setOnClickListener {
            viewModel.togglePower()
        }

        // изменение ползунка яркости
        binding.brightnessSeekbar.setOnClickListener {
            binding.brightnessSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        viewModel.setBrightness(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }

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