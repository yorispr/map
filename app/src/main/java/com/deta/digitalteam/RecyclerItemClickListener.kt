package com.deta.digitalteam

import android.view.View

/**
 * Created by YORIS on 7/13/17.
 */

interface RecyclerItemClickListener {
    fun onClick(view: View, position: Int)
    fun onLongClick(view: View, position: Int)
}
