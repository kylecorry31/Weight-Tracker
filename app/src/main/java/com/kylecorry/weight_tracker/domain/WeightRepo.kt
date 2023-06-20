package com.kylecorry.weight_tracker.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kylecorry.andromeda.core.coroutines.onIO
import com.kylecorry.sol.units.Reading
import com.kylecorry.weight_tracker.infrastructure.persistence.WeightDao
import com.kylecorry.weight_tracker.infrastructure.persistence.WeightEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightRepo @Inject constructor(private val dao: WeightDao) {

    fun getWeights(): LiveData<List<Reading<LoggedWeight>>> {
        return dao.getAll()
            .map { weights -> weights.map { it.toReading() }.sortedByDescending { it.time } }
    }

    suspend fun addWeight(weight: Reading<LoggedWeight>): Long = onIO {
        if (weight.value.id == 0L) {
            dao.insert(WeightEntity.fromReading(weight))
        } else {
            dao.update(WeightEntity.fromReading(weight))
            weight.value.id
        }
    }

    suspend fun deleteWeight(weight: Reading<LoggedWeight>) = onIO {
        dao.delete(WeightEntity.fromReading(weight))
    }

}