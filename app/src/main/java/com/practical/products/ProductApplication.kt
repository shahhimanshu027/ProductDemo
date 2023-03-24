package com.practical.products

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base class within an Android application that contains
 * all other components,
 * such as activities and services
 */

@HiltAndroidApp
class ProductApplication : Application()
