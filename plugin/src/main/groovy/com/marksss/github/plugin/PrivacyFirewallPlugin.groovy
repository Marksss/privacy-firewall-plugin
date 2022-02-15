package com.marksss.github.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.github.marksss.plugin.FirewallClassTransform
import com.github.marksss.plugin.FirewallConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by shenxl on 2022/2/11.
 */
public class PrivacyFirewallPlugin implements Plugin<Project> {

    void apply(Project project) {
        println("这是我们的自定义插件!")

        def android = project.extensions.getByType(AppExtension)
        project.extensions.create("privacyFirewallConfig", FirewallConfig)
        def classTransform = new FirewallClassTransform(android)
        android.registerTransform(classTransform)

        if (project.plugins.hasPlugin(AppPlugin)) {
            android.applicationVariants.all { variant ->
                def config = project.extensions.findByName("privacyFirewallConfig")
                if (config != null) {
                    FirewallConfig configData = new FirewallConfig(
                            coverAll: config.coverAll,
                            coverIMEI: config.coverIMEI,
                            valueIMEI: config.valueIMEI,
                            coverIMSI: config.coverIMSI,
                            valueIMSI: config.valueIMSI,
                            coverMEID: config.coverMEID,
                            valueMEID: config.valueMEID,
                            coverSerial: config.coverSerial,
                            valueSerial: config.valueSerial,
                            coverAndroidId: config.coverAndroidId,
                            coverLocation: config.coverLocation,
                    )

                    classTransform.config = configData
                }
            }
        }
    }
}