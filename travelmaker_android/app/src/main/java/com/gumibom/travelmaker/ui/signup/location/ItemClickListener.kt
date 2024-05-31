package com.gumibom.travelmaker.ui.signup.location

import android.view.View

interface ItemClickListener {
    fun onClick(view : View, address : String, position : Int)
}