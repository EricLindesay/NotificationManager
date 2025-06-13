package com.example.notificationmanager

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.notificationmanager.databinding.ActivityFigmaDataBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class FigmaDataActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityFigmaDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFigmaDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)

//        val navController = findNavController(R.id.nav_host_fragment_content_figma_data)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }

        val barChart: BarChart = findViewById(R.id.bar_chart)
        setupBarChart(barChart)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_figma_data)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private lateinit var barChart: BarChart
    private lateinit var barDataSet: BarDataSet
    private lateinit var barEntries: ArrayList<BarEntry>

    fun setupBarChart(barChart: BarChart) {
        // Working on DataSet
        val barDataSet : BarDataSet = BarDataSet(barEntriesList, "Data")
        barDataSet.setDrawValues(false)

        // Option 2: Set different colors for each bar
        barDataSet.setColors(
            ContextCompat.getColor(this, R.color.blocked),   // Blocked
            ContextCompat.getColor(this, R.color.silenced),  // Silenced
            ContextCompat.getColor(this, R.color.allowed)    // Allowed
        )
        barDataSet.stackLabels = arrayOf("Allowed", "Silenced", "Blocked")


        // Set the colors for the bars
//        barDataSet.colors = colors.toList()

//        barDataSet.valueTextSize = 11f

        // Working on BarChart
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.setFitBars(true) // Makes sure bars fit nicely
        barChart.animateY(2000)
        barChart.description.isEnabled = false
        barChart.isDragEnabled = true
        barChart.setVisibleXRangeMaximum(7f)
        barChart.legend.isEnabled = false
        barChart.setExtraOffsets(0f, 0f, 0f, 16f) // left, top, right, bottom

        // X-Axis Data
        barChart.xAxis.setAxisMinimum(-0.5f);

        val xAxisLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        val xAxis: XAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.textColor = ContextCompat.getColor(this, R.color.white)
        xAxis.textSize = 16f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                return if (index in xAxisLabels.indices) xAxisLabels[index] else ""
            }
        }

        // Enable grid lines for X-axis
//        xAxis.setDrawGridLines(true)

        // Set grid line color
//        xAxis.gridColor = Color.LTGRAY

        // Set grid line width
//        xAxis.gridLineWidth = 1f

        // Y-Axis Data
        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.setDrawGridLines(true)
        leftAxis.gridColor = Color.LTGRAY
        leftAxis.gridLineWidth = 1f
        leftAxis.textColor = Color.WHITE

        val rightAxis: YAxis = barChart.axisRight
        rightAxis.gridColor = Color.LTGRAY
        rightAxis.gridLineWidth = 1f
        rightAxis.textColor = Color.WHITE

        // Disable right Y-axis
        rightAxis.isEnabled = true

        barChart.animate()

        // Invalidate the chart to refresh
        barChart.invalidate()
    }

    // ArrayList for the first set of bar entries
    private val barEntriesList: ArrayList<BarEntry>
        get() {
            // Creating a new ArrayList
            barEntries = ArrayList()

            // Adding entries to the ArrayList for the first set
            barEntries.add(BarEntry(0f, floatArrayOf(4f, 2f, 2f)))
            barEntries.add(BarEntry(1f, floatArrayOf(5f, 5f, 1f)))
            barEntries.add(BarEntry(2f, floatArrayOf(7f, 7f, 1f)))
            barEntries.add(BarEntry(3f, floatArrayOf(4f, 6f, 7f)))
            barEntries.add(BarEntry(4f, floatArrayOf(4f, 4f, 1f)))
            barEntries.add(BarEntry(5f, floatArrayOf(2f, 2f, 0f)))
            barEntries.add(BarEntry(6f, floatArrayOf(9f, 2f, 1f)))

            return barEntries
        }
}
