package com.example.fishery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fishery.databinding.ActivityGraphBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.launch
import kotlin.random.Random

class GraphActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityGraphBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityGraphBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

//        val set = LineDataSet(dataVaues(), "Data Set 1")
//        val dataSet = ArrayList<ILineDataSet>()
//        dataSet.add(set)
//        set.lineWidth = 3f
//        set.circleRadius = 5f
//        set.color = R.color.primary
//
//        val lineData = LineData(dataSet)
//        mBinding.chart.data = lineData
//        mBinding.chart.invalidate() // refresh
//        mBinding.chart.setNoDataText("Data not available")
//        mBinding.chart.setDrawGridBackground(false)
//        mBinding.chart.isDragEnabled = true
//        mBinding.chart.setVisibleXRangeMaximum(5f)
//        mBinding.chart.moveViewToX(14f)
//        mBinding.chart.description = null
//
//        val set2 = LineDataSet(dataVaues(), "Data Set 2")
//        val dataSet2 = ArrayList<ILineDataSet>()
//        dataSet.add(set2)
//        set.lineWidth = 3f
//        set.circleRadius = 5f
//
//        val lineData2 = LineData(dataSet2)
//        mBinding.chart.data = lineData2
//        mBinding.chart.invalidate() // refresh
//        mBinding.chart.setNoDataText("Data not available")
//        mBinding.chart.setDrawGridBackground(false)
//        mBinding.chart.isDragEnabled = true
//        mBinding.chart.setVisibleXRangeMaximum(5f)
//        mBinding.chart.moveViewToX(14f)
//        mBinding.chart.description = null

        setUpListeners()

    }

    private fun setUpListeners() {
        mBinding.backIcon.setOnClickListener {
            finish()
        }

    }

//    private fun getPHValues() : ArrayList<Float>{
//        lifecycleScope.launch {
//            referrer
//        }
//    }

}