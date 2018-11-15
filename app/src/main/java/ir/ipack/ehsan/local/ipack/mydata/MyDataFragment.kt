package ir.ipack.ehsan.local.ipack.mydata

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.ipack.ehsan.local.ipack.R
import ir.ipack.ehsan.local.ipack.ViewModelFactory
import ir.ipack.ehsan.local.ipack.activities.MainActivity
import ir.ipack.ehsan.local.ipack.utils.RecyclerDivider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_data.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MyDataFragment : Fragment() {
    private lateinit var mDataAdapter: MyDataRecyclerAdapter
    private lateinit var mViewModel: MyDataViewModel
    private lateinit var mResources: Resources

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_my_data, container, false)
        val coorLayout  = (activity as MainActivity).coordinator_layout

        mResources = activity!!.resources

        mDataAdapter = MyDataRecyclerAdapter(context!!, coorLayout)

        rootView.my_data_recyclerview.adapter = mDataAdapter
        rootView.my_data_recyclerview.layoutManager = LinearLayoutManager(activity)

        return rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance(activity!!.application)).get(
            MyDataViewModel::class.java)

        subscribeToModels()
        initialDataRecyclerList()
    }

    private fun subscribeToModels() {
        mViewModel.getDataCycleStream()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mDataAdapter.setCycle(it)
            }

        mViewModel.getUsagesStream()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe {
                mDataAdapter.setAppUsage(it)
            }
    }

    private fun initialDataRecyclerList() {
        mDataAdapter.setDividerHeader(RecyclerDivider(mResources.getString(R.string.app_usage_header), -1))
    }
}