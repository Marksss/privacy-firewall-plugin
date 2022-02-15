package com.github.marksss.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.io.File

/**
 * Created by shenxl on 2022/2/11.
 */
class FirewallClassTransform(private val android: BaseExtension) : Transform() {
    var config: FirewallConfig? = null

    override fun getName() = "PrivacyFirewallClassTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
            TransformManager.CONTENT_CLASS

    override fun getScopes(): MutableSet<QualifiedContent.Scope> =
            TransformManager.SCOPE_FULL_PROJECT

    override fun isIncremental() = false

    override fun transform(transformInvocation: TransformInvocation?) {
        println("----------------进入transform了--------------")
        println("config="+config.toString())
        if (config?.executable() == true) {
            transformInvocation?.inputs?.forEach { input ->
                input.directoryInputs.forEach { directoryInput ->
                    inject(directoryInput.file.absolutePath)
                    val dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                    directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                    FileUtils.copyDirectory(directoryInput.file, dest)
                }

                input.jarInputs.forEach { jarInput ->
                    println("jar = " + jarInput.file.getAbsolutePath())
                    var jarName = jarInput.name
                    val md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length - 4)
                    }
                    val dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                    FileUtils.copyFile(jarInput.file, dest)
                }
            }
        }
        println("----------------退出transform了--------------")
    }

    private val pool = ClassPool.getDefault()

    private fun inject(path: String) {
        pool.appendClassPath(path)
        pool.appendClassPath(android.bootClasspath[0].toString())
        pool.importPackage("android.os.Bundle")

        val dir = File(path)
        if (dir.isDirectory) {
            dir.walkTopDown().forEach { file ->
                val filePath = file.absolutePath
                if (filePath.endsWith(".class") && !filePath.endsWith("R.class") && !filePath.endsWith("BuildConfig.class")) {
                    val packagePath = filePath.substring(path.length + 1)
                    val clazz = packagePath.replace("\\", ".")
                    val className = clazz.substring(0, packagePath.length - ".class".length)
                    println("className=$className")
                    val ctClass = pool.get(className)
                    if (ctClass.isFrozen) {
                        ctClass.defrost()
                    }
                    ctClass.instrument(object : ExprEditor() {
                        override fun edit(m: MethodCall?) {
                            if (m != null && config != null) {
                                for (value in PrivacyMethods.values()) {
                                    if (value.replceIfNeeded(m, config!!)) {
                                        break
                                    }
                                }
                            }
                        }
                    })
                    ctClass.writeFile(path)
                    ctClass.detach()
                }
            }
        }
    }
}