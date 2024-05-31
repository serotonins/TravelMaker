package com.gumibom.travelmaker.ui.signup.location

import androidx.recyclerview.widget.DiffUtil
import com.gumibom.travelmaker.model.Address
import com.gumibom.travelmaker.model.KakaoAddress
import com.gumibom.travelmaker.model.NaverAddress

class SignupLocationDiffUtil : DiffUtil.ItemCallback<Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
        return oldItem == newItem
    }

}