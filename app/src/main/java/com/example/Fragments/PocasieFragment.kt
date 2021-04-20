package com.example.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.R
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.fragment_pocasie.*
import kotlinx.android.synthetic.main.fragment_pocasie.view.*
import org.json.JSONObject
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [KalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var test: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    object DateUtils {
        fun fromMillisToTimeString(millis: Long) : String {
            val format = SimpleDateFormat("dd, MMM yyyy, E hh:mm a", Locale.getDefault())
            return format.format(millis)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Glide.with(this).load("https://evolon.freudenberg-pm.com/-/media/Images/evolon,-d-,freudenbergpm,-d-,com/Icons/08_FPM_evolon_breathable_600x500.gif").into(huminidy_sky)
        Glide.with(this).load("https://www.hollandaviation.nl/app/uploads/2018/03/gif-windzak.gif").into(air_sky)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        test = inflater.inflate(R.layout.fragment_pocasie, container, false)
        Thread {
            try {
        val client = OkHttpClient()

        val request = Request.Builder()
                .url("https://weatherbit-v1-mashape.p.rapidapi.com/current?lon=17.58723&lat=48.37741")
                .get()
                .addHeader("x-rapidapi-key", "99e79c0f6bmshc123686311b75c7p12063ajsn680bf32ad0ca")
                .addHeader("x-rapidapi-host", "weatherbit-v1-mashape.p.rapidapi.com")
                .build()

        val response = client.newCall(request).execute()
        val jsonData: String = response.body().string()
        val jObjectData = JSONObject(jsonData).getJSONArray("data").getJSONObject(0)

                requireActivity().runOnUiThread {

                    val city = jObjectData.getString("city_name")
                    test.City_pocasie.text = city

                    val pressure = jObjectData.getString("pres").toDouble()
                    val decimal_pressure = java.math.BigDecimal(pressure).setScale(0, RoundingMode.HALF_EVEN)
                    test.pressure_value.text = decimal_pressure.toString() +" hPa"
                    Glide.with(this).load("https://www.syncfusion.com/products/aspnet/control/images/circulargauge/aspnet-circular-gauge-with-animation.gif").into(air_pressure)

                    val temperature = jObjectData.getString("app_temp").toString()
                    test.temperature.text = temperature+ " °C"

                    val huminidy  = jObjectData.getString("rh").toDouble()
                    val decimal = java.math.BigDecimal(huminidy).setScale(0, RoundingMode.HALF_EVEN)
                    test.huminidy_value.text = decimal.toString() + " %"

                    val wind_speed  = jObjectData.getString("wind_spd").toDouble()
                    val decimal1 = java.math.BigDecimal(wind_speed).setScale(0, RoundingMode.HALF_EVEN)
                    test.wind_value.text = decimal1.toString() + " m/s"

                    val sky = jObjectData.getJSONObject("weather").getString("description")
                    println("pocasie ........................." + sky)
                    if (sky.equals("Overcast clouds" )){
                    test.sky_pocasie.text = "Zamračené"
                        Glide.with(this).load("https://onacloud.co.za/wp-content/uploads/2018/10/cloud.gif").into(cloudy_sky)
                    }
                    else if (sky.equals("Overcast Clouds" )){
                        test.sky_pocasie.text = "Zamračené"
                        Glide.with(this).load("https://onacloud.co.za/wp-content/uploads/2018/10/cloud.gif").into(cloudy_sky)
                    }
                    else if (sky == "Mix snow/rain"){
                        test.sky_pocasie.text = "Dážď so snehom"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193445/10_rainsnowmix.gif").into(mix_snow_rain_sky)
                    }
                    else if (sky == "snow"){
                        test.sky_pocasie.text = "Sneženie"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193458/13_snow.gif").into(snow_sky)
                    } else if (sky == "Light Snow"){
                        test.sky_pocasie.text = "Sneženie"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193458/13_snow.gif").into(snow_sky)
                    }
                    else if (sky == "Light rain"){
                        test.sky_pocasie.text ="Daždivé počasie"
                                Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193512/11_rain.gif").into(rain_sky)
                    } else if (sky == "Moderate rain"){
                        test.sky_pocasie.text ="Mierny dážď"
                                Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193512/11_rain.gif").into(rain_sky)
                    }
                    else if (sky == "Clear sky"){
                        test.sky_pocasie.text = "Jasno až slnečno"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193524/19_mostlysunny.gif").into(sunny_sky)
                    }
                    else if (sky == "parttialy clouds"){
                        test.sky_pocasie.text = "Mierne zamračené"
                        Glide.with(this).load("https://cdn0.microscopesinternational.com/images/weather/v2/@224/day/cloudy.gif").into(parttialy_sky)
                    }
                    else if (sky == "Scattered clouds"){
                        test.sky_pocasie.text ="Vyjasnené"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193524/19_mostlysunny.gif").into(parttialy_sky)
                    }
                    else if (sky == "Scattered Clouds"){
                        test.sky_pocasie.text ="Vyjasnené"
                        Glide.with(this).load("https://cdn.dribbble.com/users/2120934/screenshots/6193524/19_mostlysunny.gif").into(parttialy_sky)
                    }

            }
            } catch (ex: java.lang.Exception) {
                println("Error in try")
                ex.printStackTrace()
            }
        }.start()

        return test
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}