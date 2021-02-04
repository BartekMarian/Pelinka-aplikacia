package com.example.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ModelClasess.Data
import com.example.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.fragment_kvalita_ovzdusia.*
import kotlinx.android.synthetic.main.fragment_kvalita_ovzdusia.view.*
import org.json.JSONObject
import java.math.RoundingMode


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PollenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


    class PollenFragment : Fragment(), LocationListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var stream1: View
    private lateinit var userLocation: String
    private lateinit var pieChart : PieChart
      var data : MutableList<Data>?=null





            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        getLocation()

    }


    private fun getLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        userLocation = "lon=" + location.longitude + "&lat=" + location.latitude


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        stream1=   inflater.inflate(R.layout.fragment_kvalita_ovzdusia, container, false)
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                        .url("https://air-quality.p.rapidapi.com/current/airquality?lon=17.58723&lat=48.37741")
                        .get()
                        .addHeader(
                            "x-rapidapi-key",
                            "99e79c0f6bmshc123686311b75c7p12063ajsn680bf32ad0ca"
                        )
                        .addHeader("x-rapidapi-host", "air-quality.p.rapidapi.com")
                        .build()

                val response = client.newCall(request).execute()

                val jsonData: String = response.body().string()
                val jObjectData = JSONObject(jsonData).getJSONArray("data").getJSONObject(0)

                requireActivity().runOnUiThread {
                    pieChart = piechart

                    val grid1 = jObjectData.getString("co")
                    if (grid1.toFloat()<1000){
                        CO2.setTextColor(Color.parseColor("#006400"))
                        val co_rounded = java.math.BigDecimal(grid1).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.CO2.text = co_rounded.toString()+" µg"
                        CO2_level.setText("veľmi dobrá") }
                    else if(grid1.toFloat()<2000){
                        CO2.setTextColor(Color.parseColor("#00FF00"))
                        val co_rounded1 = java.math.BigDecimal(grid1).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.CO2.text = co_rounded1.toString()+" µg"
                        CO2_level.setText("dobrá") }
                    else if(grid1.toFloat()<10000){
                        CO2.setTextColor(Color.parseColor("#CCCC00"))
                        val co_rounded2 = java.math.BigDecimal(grid1).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.CO2.text = co_rounded2.toString()+" µg"
                        CO2_level.setText("zhoršená") }
                    else if(grid1.toFloat()<30000){
                        CO2.setTextColor(Color.parseColor("#FF8000"))
                        val co_rounded3 = java.math.BigDecimal(grid1).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.CO2.text = co_rounded3.toString()+" µg"
                        CO2_level.setText("zlá") }
                    else if(grid1.toFloat()>30000){
                        CO2.setTextColor(Color.parseColor("#FF0000"))
                        val co_rounded4 = java.math.BigDecimal(grid1).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.CO2.text = co_rounded4.toString()+" µg"
                        CO2_level.setText("veľmi zlá") }

                    val grid2 = jObjectData.getString("o3")
                    if (grid2.toFloat()<33){
                       O3.setTextColor(Color.parseColor("#006400"))
                        val o3_rounded = java.math.BigDecimal(grid2).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.O3.text = o3_rounded.toString()+" µg"
                        O3_level.setText("veľmi dobrá") }
                    else if(grid2.toFloat()<65){
                        O3.setTextColor(Color.parseColor("#00FF00"))
                        val o3_rounded1 = java.math.BigDecimal(grid2).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.O3.text = o3_rounded1.toString()+" µg"
                        O3_level.setText("dobrá") }
                    else if(grid2.toFloat()<180){
                        O3.setTextColor(Color.parseColor("#CCCC00"))
                        val o3_rounded2 = java.math.BigDecimal(grid2).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.O3.text = o3_rounded2.toString()+" µg"
                        O3_level.setText("zhoršená") }
                    else if(grid2.toFloat()<240){
                        O3.setTextColor(Color.parseColor("#FF8000"))
                        val o3_rounded3 = java.math.BigDecimal(grid2).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.O3.text = o3_rounded3.toString()+" µg"
                        O3_level.setText("zlá") }
                    else if(grid2.toFloat()>240){
                        O3.setTextColor(Color.parseColor("#FF0000"))
                        val o3_rounded4 = java.math.BigDecimal(grid2).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.O3.text = o3_rounded4.toString()+" µg"
                        O3_level.setText("veľmi zlá") }


                    val grid3 = jObjectData.getString("no2")
                    if (grid3.toFloat()<20){
                        No2.setTextColor(Color.parseColor("#006400"))
                        val no2_rounded = java.math.BigDecimal(grid3).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.No2.text = no2_rounded.toString()+" µg"
                        No2_level.setText("veľmi dobrá") }
                    else if(grid3.toFloat()<40){
                        No2.setTextColor(Color.parseColor("#00FF00"))
                        val no2_rounded1 = java.math.BigDecimal(grid3).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.No2.text = no2_rounded1.toString()+" µg"
                        No2_level.setText("dobrá") }
                    else if(grid3.toFloat()<200){
                        No2.setTextColor(Color.parseColor("#CCCC00"))
                        val no2_rounded2 = java.math.BigDecimal(grid3).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.No2.text = no2_rounded2.toString()+" µg"
                        No2_level.setText("zhoršená") }
                    else if(grid3.toFloat()<400){
                        No2.setTextColor(Color.parseColor("#FF8000"))
                        val no2_rounded3 = java.math.BigDecimal(grid3).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.No2.text = no2_rounded3.toString()+" µg"
                        No2_level.setText("zlá") }
                    else if(grid3.toFloat()>400){
                        No2.setTextColor(Color.parseColor("#FF0000"))
                        val no2_rounded4 = java.math.BigDecimal(grid3).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.No2.text = no2_rounded4.toString()+" µg"
                        No2_level.setText("veľmi zlá") }

                    val grid4 = jObjectData.getString("pm10")
                    if (grid4.toFloat()<20){
                        pm10.setTextColor(Color.parseColor("#006400"))
                        val pm10_rounded = java.math.BigDecimal(grid4).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm10.text = pm10_rounded.toString()+" µg"
                        pm10_level.setText("veľmi dobrá") }
                    else if(grid4.toFloat()<40){
                        pm10.setTextColor(Color.parseColor("#00FF00"))
                        val pm10_rounded1 = java.math.BigDecimal(grid4).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm10.text = pm10_rounded1.toString()+" µg"
                        pm10_level.setText("dobrá") }
                    else if(grid4.toFloat()<100){
                        pm10.setTextColor(Color.parseColor("#CCCC00"))
                        val pm10_rounded2 = java.math.BigDecimal(grid4).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm10.text = pm10_rounded2.toString()+" µg"
                        pm10_level.setText("zhoršená") }
                    else if(grid4.toFloat()<180){
                        pm10.setTextColor(Color.parseColor("#FF8000"))
                        val pm10_rounded3 = java.math.BigDecimal(grid4).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm10.text = pm10_rounded3.toString()+" µg"
                        pm10_level.setText("zlá") }
                    else if(grid4.toFloat()>180){
                        pm10.setTextColor(Color.parseColor("#FF0000"))
                        val pm10_rounded4 = java.math.BigDecimal(grid4).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm10.text = pm10_rounded4.toString()+" µg"
                        pm10_level.setText("veľmi zlá") }

                    val grid5 = jObjectData.getString("pm25")
                    if (grid5.toFloat()<14){
                        pm25.setTextColor(Color.parseColor("#006400"))
                        val pm25_rounded = java.math.BigDecimal(grid5).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm25.text = pm25_rounded.toString()+" µg"
                        pm25_level.setText("veľmi dobrá") }
                    else if(grid5.toFloat()<25){
                        pm25.setTextColor(Color.parseColor("#00FF00"))
                        val pm25_rounded1 = java.math.BigDecimal(grid5).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm25.text = pm25_rounded1.toString()+" µg"
                        pm25_level.setText("dobrá") }
                    else if(grid5.toFloat()<70){
                        pm25.setTextColor(Color.parseColor("#CCCC00"))
                        val pm25_rounded2 = java.math.BigDecimal(grid5).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm25.text = pm25_rounded2.toString()+" µg"
                        pm25_level.setText("zhoršená") }
                    else if(grid5.toFloat()<140){
                        pm25.setTextColor(Color.parseColor("#FF8000"))
                        val pm25_rounded3 = java.math.BigDecimal(grid5).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm25.text = pm25_rounded3.toString()+" µg"
                        pm25_level.setText("zlá") }
                    else if(grid5.toFloat()>140){
                        pm25.setTextColor(Color.parseColor("#FF0000"))
                        val pm25_rounded4 = java.math.BigDecimal(grid5).setScale(0, RoundingMode.HALF_EVEN)
                        stream1.pm25.text = pm25_rounded4.toString()+" µg"
                        pm25_level.setText("veľmi zlá") }

                    val grid6 = jObjectData.getString("aqi")
                    if (grid6.toFloat()<50){
                        aqi.setTextColor(Color.parseColor("#006400"))
                        stream1.aqi.text = grid6.toString()+" µg"
                        Aqi_level.setText("veľmi dobrá") }
                    else if(grid6.toFloat()<100){
                        aqi.setTextColor(Color.parseColor("#00FF00"))
                        stream1.aqi.text = grid6.toString()+" µg"
                        Aqi_level.setText("dobrá") }
                    else if(grid6.toFloat()<150){
                        aqi.setTextColor(Color.parseColor("#CCCC00"))
                        stream1.aqi.text = grid6.toString()+" µg"
                        Aqi_level.setText("zhoršená") }
                    else if(grid6.toFloat()<200){
                        aqi.setTextColor(Color.parseColor("#FF8000"))
                        stream1.aqi.text = grid6.toString()+" µg"
                        Aqi_level.setText("zlá") }
                    else if(grid6.toFloat()<300){
                        aqi.setTextColor(Color.parseColor("#FF0000"))
                        stream1.aqi.text = grid6.toString()+" µg"
                        Aqi_level.setText("veľmi zlá") }


                    val air = ArrayList<PieEntry>()
                   // air.add(PieEntry(grid1.toFloat(),"CO"))
                    air.add(PieEntry(grid2.toFloat(),"O3"))
                    air.add(PieEntry(grid3.toFloat(),"NO2"))
                    air.add(PieEntry(grid4.toFloat(),"PM10"))
                    air.add(PieEntry(grid5.toFloat(),"PM2,5"))
                    air.add(PieEntry(grid6.toFloat(),"AQI"))

                    val dataSet = PieDataSet(air,"")
                    dataSet.setDrawIcons(true)
                    dataSet.sliceSpace = 5f
                    dataSet.iconsOffset = MPPointF(0F, 30F)
                    dataSet.selectionShift = 1f
                    dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

                    val data = PieData(dataSet)
                    data.setValueTextSize(14f)
                    data.setValueTextColor(Color.WHITE)
                    pieChart = piechart
                    pieChart.setCenterText("Kvalita ovzdušia v µg/m3 (nanogramoch na meter kubický)")
                    pieChart.data = data
                    pieChart.highlightValues(null)
                    pieChart.invalidate()
                    pieChart.animateXY(3500, 3500)

                }
            } catch (ex: java.lang.Exception) {
                println("Error in try")
                ex.printStackTrace()
            }
        }.start()
return stream1


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PollenFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PollenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}