package ir.ipack.ehsan.local.ipack.data.source

import android.content.Context
import ir.ipack.ehsan.local.ipack.data.BasePlan
import ir.ipack.ehsan.local.ipack.data.Cycle
import ir.ipack.ehsan.local.ipack.data.Usage
import ir.ipack.ehsan.local.ipack.data.source.local.LocalDataSource
import rx.Observable

class Repository(private val localDataSource: LocalDataSource) : DataSource {

    override fun getBasePlanStreams(): Observable<BasePlan> = localDataSource.getBasePlanStreams()

    override fun getDataCycleStream(): Observable<Cycle> = localDataSource.getDataCycleStream()
    override fun getTalkCycleStream(): Observable<Cycle> = localDataSource.getTalkCycleStream()
    override fun getTextCycleStream(): Observable<Cycle> = localDataSource.getTextCycleStream()

    override fun updateDataCycle(cycle: Cycle) {
        localDataSource.updateDataCycle(cycle)
    }

    override fun updateTalkCycle(cycle: Cycle) {
        localDataSource.updateTalkCycle(cycle)
    }

    override fun updateTextCycle(cycle: Cycle) {
        localDataSource.updateTextCycle(cycle)
    }

    override fun getUsagesStream(context: Context): Observable<Usage> = localDataSource.getUsagesStream(context)
    override fun getTalkUsageStream(): Observable<Usage> = localDataSource.getTalkUsageStream()
    override fun getTextUsageStream(): Observable<Usage> = localDataSource.getTextUsageStream()

    override fun updateBaseCost(changeAmount: Int) = localDataSource.updateBaseCost(changeAmount)
}