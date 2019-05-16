package ir.ipack.ehsan.local.ipack.mydata

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ir.ipack.ehsan.local.ipack.BaseViewModel
import ir.ipack.ehsan.local.ipack.core.domain.basecost.UpdateBaseCostUseCase
import ir.ipack.ehsan.local.ipack.core.domain.cycle.GetCycleByTypeUseCase
import ir.ipack.ehsan.local.ipack.core.domain.cycle.UpdateCycleUseCase
import ir.ipack.ehsan.local.ipack.core.domain.usage.GetDataUsageUseCase
import ir.ipack.ehsan.local.ipack.core.exception.Failure
import ir.ipack.ehsan.local.ipack.core.result.Result
import ir.ipack.ehsan.local.ipack.data.db.entity.CycleEntity
import ir.ipack.ehsan.local.ipack.data.db.entity.UsageEntity
import ir.ipack.ehsan.local.ipack.utils.CycleTypeEnum

class MyDataViewModel(
    override val context: Application,
    getDataUsageUseCase: GetDataUsageUseCase,
    getCycleByTypeUseCase: GetCycleByTypeUseCase,
    private val updateCycleUseCase: UpdateCycleUseCase,
    private val updateBaseCostUseCase: UpdateBaseCostUseCase
) : BaseViewModel(context) {

    private val _failure = MediatorLiveData<Failure>()
    val failure: LiveData<Failure>
        get() = _failure

    private val _usages = MediatorLiveData<List<UsageEntity>>()
    val usages: LiveData<List<UsageEntity>>
        get() = _usages

    private val _cycle = MediatorLiveData<CycleEntity>()
    val cycle: LiveData<CycleEntity>
        get() = _cycle

    init {
        getDataUsageUseCase.execute(Unit)
        _usages.addSource(getDataUsageUseCase.observe()) {
            (it as? Result.Success)?.let { result ->
                _usages.value = result.data
            }
        }

        getCycleByTypeUseCase.execute(CycleTypeEnum.INTERNET)
        _cycle.addSource(getCycleByTypeUseCase.observe()) {
            (it as? Result.Success)?.let { result ->
                _cycle.value = result.data
            }
        }
    }

    fun updateDataCycle(cycle: CycleEntity) = updateCycleUseCase.execute(cycle)

    override fun updateBaseCost(changeAmount: Int) = updateBaseCostUseCase.execute(changeAmount)
}