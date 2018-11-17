package ir.ipack.ehsan.local.ipack.mydata

import android.app.Application
import ir.ipack.ehsan.local.ipack.BaseViewModel
import ir.ipack.ehsan.local.ipack.data.Cycle
import ir.ipack.ehsan.local.ipack.data.source.Repository

class MyDataViewModel(
    override val context: Application,
    private val repository: Repository
) : BaseViewModel(context) {

    fun getDataCycleStream() = repository.getDataCycleStream()

    fun getUsagesStream() = repository.getUsagesStream(context)

    fun updateDataCycle(cycle: Cycle) = repository.updateDataCycle(cycle)

    override fun updateBaseCost(changeAmount: Int) {
        repository.updateBaseCost(changeAmount)
    }
}