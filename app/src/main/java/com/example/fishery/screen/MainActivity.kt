package com.example.fishery.screen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fishery.component.LoadingDialog
import com.example.fishery.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private val userKey = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var loading: LoadingDialog
    private var phArray : MutableList<String>? = mutableListOf()
    private var turbArray : MutableList<String>? = mutableListOf()
    private var nh3Array : MutableList<String>? = mutableListOf()
    private var tempArray : MutableList<String>? = mutableListOf()
    private var waterArray : MutableList<String>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)



        loading = LoadingDialog(this@MainActivity)
        loading.startLoading()

        database = Firebase.database
        setData()


        NoInternetDialogSignal.Builder(
            this,
            lifecycle
        ).apply{}.build()

        mBinding.signout.setOnClickListener {
//            FirebaseAuth.getInstance().signOut()
            Firebase.auth.signOut()
            finishAffinity()
            finish()
        }

//        mBinding.waterGraph.setOnClickListener {
//            startActivity(Intent(this@MainActivity, GraphActivity::class.java))
//        }


    }

    private fun setData() {

        lifecycleScope.launch {
            reference = FirebaseDatabase.getInstance().getReference("Users").child(userKey!!)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").value.toString()
                    mBinding.userName.text = "Welcome $name"
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        lifecycleScope.launch {
            reference = FirebaseDatabase.getInstance().getReference("data")
            reference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.hasChildren()) {
//                        Log.e("data", snapshot.child("ph").getValue().toString())
                        phArray = snapshot.child("ph_values").getValue() as MutableList<String>?
                        turbArray = snapshot.child("turbidity_values").getValue() as MutableList<String>?
                        nh3Array = snapshot.child("nh3_values").getValue() as MutableList<String>?
                        tempArray = snapshot.child("temp_values").getValue() as MutableList<String>?
                       waterArray = snapshot.child("water_values").getValue() as MutableList<String>?


                        mBinding.phValue.text = phArray?.get(phArray?.size!! - 1).toString()
                        mBinding.turbidity.text = turbArray?.get(turbArray?.size!! - 1).toString()
                        mBinding.nh3Value.text = nh3Array?.get(nh3Array?.size!! - 1).toString()
                        mBinding.waterTemp.text = tempArray?.get(tempArray?.size!! - 1).toString()
                        mBinding.waterLevel.text =  waterArray?.get(waterArray?.size!! - 1).toString()

                        makeGraph()


                        loading.stopLoading()

//                        Log.e("data", snapshot.getValue().toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    loading.stopLoading()
                }

            })
        }
    }

    private fun makeGraph() {
        val set = ArrayList<ILineDataSet>()

        val phSet = LineDataSet(getPHdataSet(), "Ph")
        phSet.lineWidth = 3f
        phSet.circleRadius = 5f
        phSet.setColor(Color.MAGENTA);          //Line color
        phSet.setCircleColor(Color.MAGENTA);
        phSet.setValueTextSize(10f);

        val turbSet = LineDataSet(getTurbDataSet(), "Turbidity")
        turbSet.lineWidth = 3f
        turbSet.circleRadius = 5f
        turbSet.setColor(Color.RED);          //Line color
        turbSet.setCircleColor(Color.RED);
        turbSet.setValueTextSize(10f);

        val nh3Set = LineDataSet(getNH3DataSet(), "NH3")
        nh3Set.lineWidth = 3f
        nh3Set.circleRadius = 5f
        nh3Set.setColor(Color.CYAN);          //Line color
        nh3Set.setCircleColor(Color.CYAN);
        nh3Set.setValueTextSize(10f);

        val tempSet = LineDataSet(getTempDataSet(), "Temperature")
        tempSet.lineWidth = 3f
        tempSet.circleRadius = 5f
        tempSet.setColor(Color.BLACK);          //Line color
        tempSet.setCircleColor(Color.BLACK);
        tempSet.setValueTextSize(10f);

        val waterSet = LineDataSet(getWaterDataSet(), "Water Level")
        waterSet.lineWidth = 3f
        waterSet.circleRadius = 5f
        waterSet.setColor(Color.BLUE);          //Line color
        waterSet.setCircleColor(Color.BLUE);
        waterSet.setValueTextSize(10f);

        set.add(phSet)
        set.add(turbSet)
        set.add(nh3Set)
        set.add(tempSet)
        set.add(waterSet)


        val lineData = LineData(set)

        mBinding.chart.data = lineData
        mBinding.chart.invalidate() // refresh
        mBinding.chart.setNoDataText("Data not available")
        mBinding.chart.setDrawGridBackground(false)
        mBinding.chart.isDragEnabled = true
        mBinding.chart.setVisibleXRangeMaximum(5f)
        mBinding.chart.moveViewToX(phArray?.size?.toFloat()!!)
        mBinding.chart.description = null

    }

    private fun getWaterDataSet(): MutableList<Entry>? {
        val entries: MutableList<Entry> = ArrayList()
        for (item in waterArray?.indices!!){
            entries.add(Entry(item.toFloat(), waterArray!![item].toFloat()))
        }

        return entries

    }

    private fun getTempDataSet(): MutableList<Entry>? {
        val entries: MutableList<Entry> = ArrayList()
        for (item in tempArray?.indices!!){
            entries.add(Entry(item.toFloat(), tempArray!![item].toFloat()))
        }

        return entries
    }

    private fun getPHdataSet(): MutableList<Entry>? {
        val entries: MutableList<Entry> = ArrayList()
        for (item in phArray?.indices!!){
            entries.add(Entry(item.toFloat(), phArray!![item].toFloat()))
        }

        return entries
    }

    private fun getNH3DataSet(): MutableList<Entry>? {
        val entries: MutableList<Entry> = ArrayList()
        for (item in nh3Array?.indices!!){
            entries.add(Entry(item.toFloat(), nh3Array!![item].toFloat()))
        }

        return entries
    }

    private fun  getTurbDataSet() : MutableList<Entry>?{
        val entries = ArrayList<Entry>()

        for(i in turbArray?.indices!!){
            entries.add(Entry(i.toFloat(), turbArray!![i].toFloat()))
        }
//        var i = 0
//        while (i<turbArray.size){
//            entries.add(Entry(i.toFloat(), turbArray[i].toFloat()))
//            i++
//        }

        Log.e("sssss", entries.size.toString())
        return entries
    }
}