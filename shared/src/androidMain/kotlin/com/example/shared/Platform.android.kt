package com.example.shared

import android.os.Build


private class AndroidPlatform: Platform {
    override val name: String = "Android"
    override val version: Double = Build.VERSION.SDK_INT.toDouble()
}

public actual fun platform(): Platform = AndroidPlatform()