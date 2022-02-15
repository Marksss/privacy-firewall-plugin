package com.github.marksss.plugin

import com.google.gson.Gson

/**
 * Created by shenxl on 2022/2/11.
 */
open class FirewallConfig {
    var coverAll: Boolean = false
    var coverIMEI: Boolean = false
    var valueIMEI: String? = null
    var coverIMSI: Boolean = false
    var valueIMSI: String? = null
    var coverMEID: Boolean = false
    var valueMEID: String? = null
    var coverSerial: Boolean = false
    var valueSerial: String? = null
    var coverAndroidId: Boolean = false
    var coverLocation: Boolean = false

    fun executable() = coverAll || coverIMEI || coverIMSI
            || coverMEID || coverSerial || coverAndroidId || coverLocation

    override fun toString(): String {
        return Gson().toJson(this)
    }
}