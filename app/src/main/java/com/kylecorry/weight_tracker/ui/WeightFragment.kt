package com.kylecorry.weight_tracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.andromeda.fragments.inBackground
import com.kylecorry.andromeda.fragments.observe
import com.kylecorry.andromeda.pickers.Pickers
import com.kylecorry.ceres.chart.Chart
import com.kylecorry.ceres.chart.data.LineChartLayer
import com.kylecorry.sol.units.Reading
import com.kylecorry.sol.units.Weight
import com.kylecorry.weight_tracker.R
import com.kylecorry.weight_tracker.databinding.FragmentWeightBinding
import com.kylecorry.weight_tracker.domain.LoggedWeight
import com.kylecorry.weight_tracker.domain.WeightRepo
import com.kylecorry.weight_tracker.infrastructure.preferences.UserSettings
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@AndroidEntryPoint
class WeightFragment : BoundFragment<FragmentWeightBinding>() {

    @Inject
    lateinit var formatter: FormatService

    @Inject
    lateinit var prefs: UserSettings

    @Inject
    lateinit var repo: WeightRepo

    private val mapper by lazy {
        WeightListItemMapper(requireContext(), prefs, formatter, ::onDelete)
    }

    private val lineChart by lazy {
        LineChartLayer(
            emptyList(),
            Resources.androidTextColorPrimary(requireContext())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weightChart.plot(lineChart)
        binding.weightChart.configureYAxis(
            labelCount = 5,
            drawGridLines = true
        )

        observe(repo.getWeights()) {
            binding.weightList.setItems(it, mapper)
            val currentWeight = it.firstOrNull()?.value?.weight?.convertTo(prefs.units)
            if (currentWeight != null) {
                binding.weightTitle.title.text = formatter.formatWeight(currentWeight)
            }
            updateChart(it)
        }

        binding.weightTitle.rightButton.setOnClickListener {
            Pickers.number(
                requireContext(),
                getString(R.string.weight),
                allowDecimals = true,
                allowNegative = false,
            ) { weight ->
                if (weight != null) {
                    inBackground {
                        repo.addWeight(
                            Reading(
                                LoggedWeight(0, Weight(weight.toFloat(), prefs.units)),
                                Instant.now()
                            )
                        )
                    }
                }
            }
        }

    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeightBinding {
        return FragmentWeightBinding.inflate(layoutInflater, container, false)
    }

    private fun onDelete(weight: Reading<LoggedWeight>) {
        inBackground {
            repo.deleteWeight(weight)
        }
    }

    private fun updateChart(weights: List<Reading<LoggedWeight>>) {
        val oldestTime = Instant.now().minus(Duration.ofDays(30))
        lineChart.data = Chart.getDataFromReadings(weights.filter { it.time >= oldestTime }, null) {
            it.weight.convertTo(prefs.units).weight
        }
    }
}