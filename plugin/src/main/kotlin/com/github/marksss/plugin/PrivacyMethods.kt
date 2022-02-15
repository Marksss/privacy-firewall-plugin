package com.github.marksss.plugin

import javassist.expr.MethodCall

/**
 * Created by shenxl on 2022/2/14.
 */
enum class PrivacyMethods {
    SERIAL {
        override fun replceIfNeeded(m: MethodCall, config: FirewallConfig): Boolean {
            if (m.className == "android.os.Build" && m.methodName == "getSerial"
                    && (config.coverAll || config.coverSerial)) {
                val defaultValue = if (config.valueSerial != null)
                    "\"${config.valueSerial}\"" else null
                m.replace("{ \$_ = $defaultValue; android.widget.Toast.makeText(com.marksss.plugin.demo.MyApplication.instance,\"javassist success for getSerial:$defaultValue\",android.widget.Toast.LENGTH_SHORT).show();}")
                return true
            }
            return false
        }
    },
    IMEI {
        override fun replceIfNeeded(m: MethodCall, config: FirewallConfig): Boolean {
            if (m.className == "android.telephony.TelephonyManager" && m.methodName == "getDeviceId"
                    && (config.coverAll || config.coverIMEI)) {
                val defaultValue = if (config.valueIMEI != null)
                    "\"${config.valueIMEI}\"" else null
                m.replace("{ \$_ = $defaultValue; android.widget.Toast.makeText(com.marksss.plugin.demo.MyApplication.instance,\"javassist success for getDeviceId:$defaultValue\",android.widget.Toast.LENGTH_SHORT).show();}")
                return true
            }
            return false
        }
    },
    IMSI {
        override fun replceIfNeeded(m: MethodCall, config: FirewallConfig): Boolean {
            if (m.className == "android.telephony.TelephonyManager" && m.methodName == "getSubscriberId"
                    && (config.coverAll || config.coverIMSI)) {
                val defaultValue = if (config.valueIMSI != null)
                    "\"${config.valueIMSI}\"" else null
                m.replace("{ \$_ = $defaultValue; android.widget.Toast.makeText(com.marksss.plugin.demo.MyApplication.instance,\"javassist success for getSubscriberId:$defaultValue\",android.widget.Toast.LENGTH_SHORT).show();}")
                return true
            }
            return false
        }
    },
    MEID {
        override fun replceIfNeeded(m: MethodCall, config: FirewallConfig): Boolean {
            if (m.className == "android.telephony.TelephonyManager" && m.methodName == "getMeid"
                    && (config.coverAll || config.coverMEID)) {
                val defaultValue = if (config.valueMEID != null)
                    "\"${config.valueMEID}\"" else null
                m.replace("{ \$_ = $defaultValue; android.widget.Toast.makeText(com.marksss.plugin.demo.MyApplication.instance,\"javassist success for getMeid:$defaultValue\",android.widget.Toast.LENGTH_SHORT).show();}")
                return true
            }
            return false
        }
    },
    ;

    abstract fun replceIfNeeded(m: MethodCall, config: FirewallConfig): Boolean
}