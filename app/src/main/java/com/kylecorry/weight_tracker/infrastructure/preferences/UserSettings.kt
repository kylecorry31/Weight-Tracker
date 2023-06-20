package com.kylecorry.weight_tracker.infrastructure.preferences

import android.content.Context
import com.kylecorry.sol.units.WeightUnits
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettings @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val units = WeightUnits.Pounds

}