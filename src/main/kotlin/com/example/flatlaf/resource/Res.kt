package com.example.flatlaf.resource

import com.google.gson.JsonElement
import com.example.flatlaf.gson
import org.springframework.core.GenericTypeResolver
import java.awt.Image
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URL
import javax.imageio.ImageIO
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

@OptIn(ExperimentalStdlibApi::class)
object Res {
    private fun exception(path: String): FileNotFoundException {
        return FileNotFoundException("Resource not found: $path")
    }
    fun getResource(path: String): URL? {
        return javaClass.getResource(path)
    }

    fun openResource(path: String): InputStream {
        return javaClass.getResourceAsStream(path) ?: throw exception(path)
    }

    fun loadImage(path: String): Image {
        return openResource(path).use { ImageIO.read(it) ?: throw IOException("Unable to read image at $path") }
    }

    @Suppress("unused")
    inline fun <reified T : JsonElement> loadJson(path: String): T {
        return openResource(path).use { gson.fromJson(it.reader(), GenericTypeResolver.resolveType(typeOf<T>().javaType, javaClass)) }
    }

    fun require(path: String): String {
        return getResource(path)?.toString()?.also {
            println("it = $it")
        } ?: throw exception(path)
    }
}
