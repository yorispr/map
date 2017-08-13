package com.deta.digitalteam

import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import android.location.Geocoder
import android.location.Criteria
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import java.io.IOException
import java.util.*
import com.google.maps.android.ui.IconGenerator
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.StyleSpan
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_detail_store.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailStoreActivity : AppCompatActivity(), OnMapReadyCallback {

    var touch_counter: Int = 0

    lateinit var gMap: GoogleMap
    var id_store: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_store)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)


        buttonSave.setOnClickListener {
            if(id_store != -1)
                updateData( store(id_store, inputNama.text.toString(), inputLatitude.text.toString().toDouble(), inputLongitude.text.toString().toDouble()))
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        gMap.uiSettings.isZoomControlsEnabled = true;
        gMap.uiSettings.isMyLocationButtonEnabled = true;
        if(intent.getSerializableExtra("store") != null){
            val data = intent.getSerializableExtra("store")
            if(data is store){
                    val s: store = data
                    val cphbusiness = LatLng(s.latitude, s.longitude)

                id_store = s.store_id

                setText(s)

                val iconFactory = IconGenerator(this)

                var init_marker = addIcon(iconFactory, s.store_name, cphbusiness, false)

                var touch_marker = addIcon(iconFactory, s.store_name, cphbusiness, true)

                gMap.moveCamera(CameraUpdateFactory.newLatLng(cphbusiness))
                gMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

                gMap.setOnMarkerDragListener(object : OnMarkerDragListener {
                    override fun onMarkerDragStart(arg0: Marker) {
                        // TODO Auto-generated method stub
                        //Log.d("System out", "onMarkerDragStart..." + arg0.position.latitude + "..." + arg0.position.longitude)
                    }

                    override fun onMarkerDragEnd(arg0: Marker) {
                        // TODO Auto-generated method stub
                        Log.d("System out", "onMarkerDragEnd..." + arg0.position.latitude + "..." + arg0.position.longitude)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.position))

                        var coord: LatLng = LatLng(arg0.position.latitude, arg0.position.longitude)
                        touch_marker.remove()
                        touch_marker = addIcon(iconFactory, getPlaceName(coord), coord, true)

                        setText(store(0, inputNama.text.toString(), coord.latitude, coord.longitude))
                    }

                    override fun onMarkerDrag(arg0: Marker) {
                        // TODO Auto-generated method stub
                        //Log.i("System out", "onMarkerDrag...")
                    }
                })


                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return
                    }
                    googleMap.isMyLocationEnabled = true
            }
        }
    }

    private fun addIcon(iconFactory: IconGenerator, text: CharSequence, position: LatLng, draggable: Boolean): Marker {
        val markerOptions = MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).position(position).anchor(iconFactory.anchorU, iconFactory.anchorV).draggable(draggable)
        return gMap.addMarker(markerOptions)
    }

    fun getPlaceName(location: LatLng): String{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        val providerList = locationManager.allProviders

        if ( null != providerList && providerList.size > 0) {
            val longitude = location.longitude
            val latitude = location.latitude
            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            try {
                val listAddresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (null != listAddresses && listAddresses.size > 0) {
                    Log.d("lok", listAddresses.toString())
                    return listAddresses[0].thoroughfare + ", " + listAddresses[0].subAdminArea
                }else{
                    return "not found"
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return "not found"
    }

    private fun makeCharSequence(text: CharSequence): CharSequence {

        val prefix = "Mixing "
        val suffix = "different fonts"
        val sequence = prefix + suffix
        val ssb = SpannableStringBuilder(sequence)
        ssb.setSpan(StyleSpan(android.graphics.Typeface.ITALIC), 0, prefix.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.setSpan(StyleSpan(android.graphics.Typeface.BOLD), prefix.length, sequence.length, SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }

    fun updateData(s: store){

        var gson: Gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.101/deta/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        val endpoint: Endpoint = retrofit.create(Endpoint::class.java)
        val call: Call<JsonElement> = endpoint.editStore(s)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (!response!!.isSuccessful) {
                    Log.d("K", "No Success")
                }

                Log.d("retrofit", response.body().toString())
                val jsonObj = response.body()!!.asJsonObject

                Toast.makeText(this@DetailStoreActivity, jsonObj.get("message").asString,Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
                Log.d("error", t.toString())
                Toast.makeText(this@DetailStoreActivity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setText(data: store){
        txtAlamat.text = getPlaceName(LatLng(data.latitude, data.longitude))
        inputNama.setText(data.store_name)
        inputLatitude.setText(data.latitude.toString())
        inputLongitude.setText(data.longitude.toString())
    }
}
