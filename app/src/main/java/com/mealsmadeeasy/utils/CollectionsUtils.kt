package com.mealsmadeeasy.utils

fun <T> List<T>.replace(old: T, new: T) = map { if (it == old) new else it }