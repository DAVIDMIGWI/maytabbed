package com.davyy.mytabbed

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.davyy.mytabbed.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        GPS()

        /*
        Add a marker in Sydney and move the camera
        retrieve all latitudes and longitudes from databases
        server will run
        host online
        */

        val client = AsyncHttpClient(true, 80, 443)
        client.get("https://modcom.pythonanywhere.com/locations", object : JsonHttpResponseHandler()
        //client.get("http://127.0.0.1:5000/location", object : JsonHttpResponseHandler()

        {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONArray
            ) {
                // WE USE FOR LOOP
                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val lat = jsonObject.optString("lat").toDouble()
                    val lon = jsonObject.optString("lon").toDouble()
                    val name = jsonObject.optString("name").toString()
                    //val phone=jsonObject.optInt("phone").toInt()
                    //val descc=jsonObject.optString("descc").toBooleanStrict()
                    //map the car coordinates and loop again
                    val boma = LatLng(lat, lon)
                    mMap.addMarker(
                        MarkerOptions().position(boma)
                            .title(name)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(boma, 15f))

                }
            }// end on-success


            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                super.onFailure(statusCode, headers, responseString, throwable)
            }// end on-failure

        })


/*
* we need internet permissions added to manifest
* for android 10, 11 we need to allow cleartext
* above are from in manifest file
* */

    }

            //.snippet("open 8.00AM - 5.00PM")
            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
  fun GPS(){
      //check permissions
      if(ActivityCompat.checkSelfPermission(
              this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                      PackageManager.PERMISSION_GRANTED)

          {
          ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
          }//end if

                //get user position
          val fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
                mMap.isMyLocationEnabled=true
                fusedLocationClient.lastLocation.addOnSuccessListener (this) {
                    location->
                    if (location!=null){
                       val currentLocation=LatLng(location.latitude,location.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(currentLocation)
                            .title("IM HERE")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    )
                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15f))
                }
                   /* else {
                        Toast.makeText(applicationContext,"No location,Activate GPS", Toast.LENGTH_LONG
                    }*/
                }//end


  }// END FUN GPS


}