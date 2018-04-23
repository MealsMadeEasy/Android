package com.mealsmadeeasy.model

import android.os.Parcel
import android.os.Parcelable

data class Filter(
        val name: String,
        val id: String
) : Parcelable {
    companion object CREATOR : Parcelable.Creator<Filter> {
        override fun createFromParcel(source: Parcel): Filter {
            return Filter(source)
        }

        override fun newArray(size: Int) = arrayOfNulls<Filter>(size)
    }

    constructor(parcel: Parcel): this(parcel.readString(), parcel.readString())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(id)
    }

    override fun describeContents() = 0
}
