package io.github.hanihashemi.eggmaster.extensions

import android.content.Context
import android.content.res.Configuration

internal fun Context.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

internal fun Context.isTablet() =
    resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE