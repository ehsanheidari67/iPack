package ir.ipack.ehsan.local.ipack.myplan

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBindAdapter
import com.yqritc.recyclerviewmultipleviewtypesadapter.DataBinder
import ir.ipack.ehsan.local.ipack.R
import ir.ipack.ehsan.local.ipack.data.BasePlan
import ir.ipack.ehsan.local.ipack.utils.Currency
import ir.ipack.ehsan.local.ipack.views.RobotoTextView
import kotlinx.android.synthetic.main.base_plan_info.view.*


class BasePlanBinder(dataBindAdapter: DataBindAdapter) : DataBinder<BasePlanBinder.BasePlanHolder>(dataBindAdapter) {
    var mBasePlan : BasePlan? = null
    override fun bindViewHolder(holder: BasePlanHolder?, position: Int) {
        Log.i("ETest", "bindViewHolder " + mBasePlan?.baseCost)
        mBasePlan?.let {
            holder?.baseCost?.text = Currency.localize(it.baseCost, false)
            holder?.addonCost?.text = Currency.localize(it.addonCost, false)
        }
    }

    override fun getItemCount(): Int = 1

    override fun newViewHolder(parent: ViewGroup?): BasePlanHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.base_plan_info, parent,false)
        return BasePlanHolder(view)
    }

    fun add(basePlan: BasePlan){
        mBasePlan = basePlan
        Log.i("ETest", "add " + mBasePlan?.baseCost)
        notifyBinderDataSetChanged()
    }

    class BasePlanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val baseCost = itemView.base_cost
        val addonCost = itemView.addon_cost
    }



}