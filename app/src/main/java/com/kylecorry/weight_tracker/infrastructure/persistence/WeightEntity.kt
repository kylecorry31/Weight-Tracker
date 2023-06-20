package com.kylecorry.weight_tracker.infrastructure.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kylecorry.sol.units.Reading
import com.kylecorry.sol.units.Weight
import com.kylecorry.sol.units.WeightUnits
import com.kylecorry.weight_tracker.domain.LoggedWeight
import java.time.Instant

@Entity(
    tableName = "weight"
)
data class WeightEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id: Long = 0,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "time") val time: Instant
) {

    fun toReading(): Reading<LoggedWeight> {
        return Reading(
            LoggedWeight(id, Weight(weight, WeightUnits.Kilograms)),
            time
        )
    }

    companion object {
        fun fromReading(reading: Reading<LoggedWeight>): WeightEntity {
            return WeightEntity(
                reading.value.id,
                reading.value.weight.convertTo(WeightUnits.Kilograms).weight,
                reading.time
            )
        }
    }

}