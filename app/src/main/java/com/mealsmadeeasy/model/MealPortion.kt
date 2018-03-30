package com.mealsmadeeasy.model

import android.os.Parcel
import android.os.Parcelable

data class MealPortion(
        val meal: Meal,
        val servings: Int
): Parcelable {
    companion object CREATOR : Parcelable.Creator<MealPortion> {
        override fun createFromParcel(source: Parcel): MealPortion {
            return MealPortion(source)
        }

        override fun newArray(size: Int) = arrayOfNulls<MealPortion>(size)
    }

    constructor(parcel: Parcel): this(
            meal = parcel.readParcelable(Meal::class.java.classLoader),
            servings = parcel.readInt()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(meal, flags)
        dest.writeInt(servings)
    }

    override fun describeContents() = 0
}