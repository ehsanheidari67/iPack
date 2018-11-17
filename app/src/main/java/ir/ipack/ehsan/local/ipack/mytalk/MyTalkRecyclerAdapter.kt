package ir.ipack.ehsan.local.ipack.mytalk

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import com.yqritc.recyclerviewmultipleviewtypesadapter.ListBindAdapter
import ir.ipack.ehsan.local.ipack.BaseViewModel
import ir.ipack.ehsan.local.ipack.commonbinders.CycleBinder
import ir.ipack.ehsan.local.ipack.commonbinders.UsageHeaderBinder
import ir.ipack.ehsan.local.ipack.data.Cycle
import ir.ipack.ehsan.local.ipack.data.Usage
import ir.ipack.ehsan.local.ipack.mydata.MyDataViewModel
import ir.ipack.ehsan.local.ipack.mytalk.binders.TalkIncomingOutgoingBinder
import ir.ipack.ehsan.local.ipack.utils.RecyclerDivider

class MyTalkRecyclerAdapter(context: Context, coordinatorLayout: CoordinatorLayout, baseViewModel: BaseViewModel) : ListBindAdapter() {
    init {
        val cycleBinder = CycleBinder(context, this, coordinatorLayout, baseViewModel)
        val talkUsageBinder = TalkIncomingOutgoingBinder(this)
        addAllBinder(cycleBinder, talkUsageBinder)
    }

    fun setCycle(cycle: Cycle) {
        getDataBinder<CycleBinder>(0).add(cycle)
    }


    fun setTalkUsage(talkUsage: Usage){
        getDataBinder<TalkIncomingOutgoingBinder>(1).add(talkUsage)
    }
}