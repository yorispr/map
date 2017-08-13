package com.deta.digitalteam

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.store_list_view.view.*

/**
 * Created by YORIS on 8/5/17.
 */

internal class ListAdapter(val StoreList: ArrayList<store>?) : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val txtstore_id: TextView
        val txtstore_name: TextView
        val btnViewMap: Button


        init {
            txtstore_id = view.txtStoreID
            txtstore_name = view.txtStoreName
            btnViewMap = view.btnViewMap
        }

        override fun onClick(v: View) {
            Toast.makeText(v.context, "Clicked...", Toast.LENGTH_SHORT).show()
        }

        override fun onLongClick(v: View): Boolean {
            Toast.makeText(v.context, "Long clicked...", Toast.LENGTH_SHORT).show()
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.store_list_view, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val msg = StoreList!![position]

        holder.txtstore_id.text = msg.store_id.toString()
        holder.txtstore_name.text = msg.store_name

        //Log.d("installed", app.isInstalled.toString())
    }

    override fun getItemCount(): Int {
        return StoreList!!.size
    }
}
