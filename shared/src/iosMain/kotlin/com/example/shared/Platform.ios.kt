package com.example.shared

import platform.UIKit.UIDevice


private class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName()
    override val version: Double = UIDevice.currentDevice.systemVersion().toDouble()
}

public actual fun platform(): Platform = IOSPlatform()