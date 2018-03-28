package com.mealsmadeeasy.model

import android.os.Parcel
import android.os.Parcelable

data class MealPortion(
        val meal: Meal,
        val servings: Int
): Parcelable {
    companion object CREATOR : Parcelable.Creator<MealPortion> {
        override fun createFromParcel(source: Parcel?): MealPortion {
            return MealPortion(source!!)
        }

        override fun newArray(size: Int): Array<MealPortion?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel): this(parcel.readParcelable(null), parcel.readInt())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeParcelable(meal, flags)
            dest.writeInt(servings)
        }
    }

    override fun describeContents(): Int {
        return 0
    }
}