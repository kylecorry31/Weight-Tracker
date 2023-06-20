package com.kylecorry.weight_tracker.ui

import android.content.Context
import com.kylecorry.ceres.list.ListItem
import com.kylecorry.ceres.list.ListItemMapper
import com.kylecorry.ceres.list.ListMenuItem
import com.kylecorry.sol.time.Time.toZonedDateTime
import com.kylecorry.sol.units.Reading
import com.kylecorry.weight_tracker.R
import com.kylecorry.weight_tracker.domain.LoggedWeight
import com.kylecorry.weight_tracker.infrastructure.preferences.UserSettings

class WeightListItemMapper(
    private val context: Context,
    private val settings: UserSettings,
    private val formatter: FormatService,
    private val onDelete: (Reading<LoggedWeight>) -> Unit
) : ListItemMapper<Reading<LoggedWeight>> {

    override fun map(value: Reading<LoggedWeight>): ListItem {
        return ListItem(
            value.value.id,
            formatter.formatWeight(value.value.weight.convertTo(settings.units)),
            formatter.formatDate(value.time.toZonedDateTime()),
            menu = listOf(
                ListMenuItem(context.getString(R.string.delete)) { onDelete(value) }
            )
        )
    }
}