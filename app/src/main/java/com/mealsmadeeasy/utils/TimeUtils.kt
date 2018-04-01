package com.mealsmadeeasy.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * Forces a DateTime to be converted to UTC, ignoring the timezone offset. So for example,
 * 08:00 on March 31, 2018 EST becomes 0:800 on March 31, 2018 GMT. This is useful for when you
 * hate timezones and just want JodaTime to treat you like an adult who knows what they're doing,
 * despite the fact that you're neither of those things.
 */
fun DateTime.forceUtc(): DateTime
        = this.toDateTime(DateTimeZone.UTC) + DateTimeZone.getDefault().getOffset(this).toLong()
