package com.gumibom.travelmaker.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class GoogleUser(
    val uId : String,
    val email : String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString()?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uId)
        parcel.writeString(email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoogleUser> {
        override fun createFromParcel(parcel: Parcel): GoogleUser {
            return GoogleUser(parcel)
        }

        override fun newArray(size: Int): Array<GoogleUser?> {
            return arrayOfNulls(size)
        }
    }
}
