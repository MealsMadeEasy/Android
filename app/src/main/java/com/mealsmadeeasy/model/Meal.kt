package com.mealsmadeeasy.model

import android.os.Parcel
import android.os.Parcelable

data class Meal (
        val id: String,
        val name: String,
        val description: String,
        val thumbnailUrl: String?
): Parcelable {
    companion object CREATOR : Parcelable.Creator<Meal> {
        override fun createFromParcel(source: Parcel?): Meal {
            return Meal(source!!)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel): this(parcel.readString(), parcel.readString(),
            parcel.readString(), parcel.readString())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeString(id)
            dest.writeString(name)
            dest.writeString(description)
            dest.writeString(thumbnailUrl)
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}
