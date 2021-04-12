package com.example.Fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.Kalendar
import com.example.R
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.android.synthetic.main.fragment_hladina_pelov.*
import kotlinx.android.synthetic.main.fragment_hladina_pelov.view.*
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [KvalitaVzduchuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


 class KvalitaVzduchuFragment : Fragment(), LocationListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    private lateinit var stream: View
    private lateinit var userLocation: String


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
        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5f, this)
    }
   // @SuppressLint("SetTextI18n")
    override fun onLocationChanged(location: Location) {
        //userLocation = "lon=" + location.longitude + "&lat=" + location.latitude
        val ttLon = "17.58723"
        val ttLat = "48.37741"
        val baLon = "17.10674"
        val baLat = "48.14816"
        userLocation = "https://air-quality.p.rapidapi.com/current/airquality?lon="+baLon+"&lat="+baLat
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
          //             .url("https://air-quality.p.rapidapi.com/current/airquality?" + userLocation)
                        .url(userLocation)
                        .get()
                        .addHeader("x-rapidapi-key", "99e79c0f6bmshc123686311b75c7p12063ajsn680bf32ad0ca")
                        .addHeader("x-rapidapi-host", "air-quality.p.rapidapi.com")
                        .build()

                val response = client.newCall(request).execute()
                    val jsonData: String = response.body().string()
                    val jobject = JSONObject(jsonData)


                    stream.cityName.text = "Peľové správy pre okres " + jobject.getString("city_name")
                    val mold = jobject.getJSONArray("data").getJSONObject(0).getString("predominant_pollen_type")
                    if (mold == "Molds"){
                        stream.moods.text = "Hubové plesne"
                    } else {
                        stream.moods.text = "Ostatné alergény : " + mold
                    }
                    val tree =jobject.getJSONArray("data").getJSONObject(0).getInt("pollen_level_tree")
                    stream.level_tree.text = "Stromy úroveň "+tree.toString()
                    val grass =jobject.getJSONArray("data").getJSONObject(0).getInt("pollen_level_grass")
                    stream.level_grass.text = "Byliny úroveň "+ grass.toString()
                    val weed =jobject.getJSONArray("data").getJSONObject(0).getInt("pollen_level_weed")
                    stream.level_weed.text = "Trávy úroveň "+weed.toString()

                val db = FirebaseFirestore.getInstance()

                val user: MutableMap<String, Any> = HashMap()
                user.put("Molds", mold)
                user.put("tree", tree)
                user.put("grass", grass)
                user.put("weed", weed)

                        val document_ID = "bKZbPuXIa4CalyJFvHUI"
                db.collection("Pollen news").document(document_ID).update(user)


            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
            }
        }.start()
    }

//    fun notifikacia (){
//        val CHANNEL_ID = "1"
//        val channelId = "test"
//        val description = "Test notification"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // Create the NotificationChannel
//            val name = channelId
//            val descriptionText = description
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
//            mChannel.description = descriptionText
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            val notificationManager = getContext()?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(mChannel)
//        }
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
     stream = inflater.inflate(R.layout.fragment_hladina_pelov, container, false)

        return stream

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val kalendar: TextView? = stream.findViewById(R.id.linkKalendar)
        kalendar?.setOnClickListener {

            val intent = Intent(activity, Kalendar::class.java)
            activity?.startActivity(intent)
        }
        linkKalendar.paintFlags = linkKalendar.paintFlags or (Paint.UNDERLINE_TEXT_FLAG)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment KvalitaVzduchuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KvalitaVzduchuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}