package com.kylecorry.weight_tracker.ui

import android.content.Context
import android.text.format.DateUtils
import com.kylecorry.andromeda.core.math.DecimalFormatter
import com.kylecorry.sol.units.Weight
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZonedDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormatService @Inject constructor(@ApplicationContext private val context: Context) {
    fun formatDate(
        date: ZonedDateTime,
        includeWeekDay: Boolean = true,
        abbreviateMonth: Boolean = false
    ): String {
        return DateUtils.formatDateTime(
            context,
            date.toEpochSecond() * 1000,
            DateUtils.FORMAT_SHOW_DATE or (if (includeWeekDay) DateUtils.FORMAT_SHOW_WEEKDAY else 0) or DateUtils.FORMAT_SHOW_YEAR or (if (abbreviateMonth) DateUtils.FORMAT_ABBREV_MONTH else 0)
        )
    }

    fun formatWeight(weight: Weight): String {
        // TODO: Get weight symbol
        return "${DecimalFormatter.format(weight.weight, 1)} ${weight.units.name}"
    }

}