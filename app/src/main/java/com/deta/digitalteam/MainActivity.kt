package com.deta.digitalteam

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amartha.amarthalauncher.RecyclerTouchListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    var storeArray: ArrayList<store>? = ArrayList()

    internal var ListRecyclerAdapterAdapter: ListAdapter = ListAdapter(storeArray)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initList()
        initData()
    }


    private fun initList() {
        ListRecyclerAdapterAdapter = ListAdapter(storeArray)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        StoreListView.layoutManager = mLayoutManager
        StoreListView.itemAnimator = DefaultItemAnimator()
        StoreListView.adapter = ListRecyclerAdapterAdapter

        StoreListView!!.addOnItemTouchListener(RecyclerTouchListener(applicationContext, StoreListView, object : RecyclerItemClickListener {
            override fun onClick(view: View, position: Int) {
                val s = storeArray!![position]
                var intent:Intent = Intent(this@MainActivity, DetailStoreActivity::class.java)
                intent.putExtra("store",s)
                startActivity(intent)
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))
    }

    private fun initData()
    {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.101/deta/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val endpoint: Endpoint = retrofit.create(Endpoint::class.java)
        val call: Call<ArrayList<store>> = endpoint.getStore()

        call.enqueue(object : Callback<ArrayList<store>> {
            override fun onResponse(call: Call<ArrayList<store>>, response: Response<ArrayList<store>>) {
                if (!response!!.isSuccessful) {
                    Log.d("K", "No Success")
                }

                for (s in response.body().orEmpty()) {
                    storeArray!!.add(s)
                }

                ListRecyclerAdapterAdapter.notifyDataSetChanged()
                //Log.d("retrofit", postArray!!.size.toString())
            }

            override fun onFailure(call: Call<ArrayList<store>>, t: Throwable?) {
                Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_LONG).show()
            }
        })

    }

}
