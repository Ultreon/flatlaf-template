package com.example.flatlaf.lang

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.util.*

class Language(val locale: Locale) {
    private var props: Properties = Properties()

    fun load() {
        props = PropertiesLoaderUtils.loadProperties(ClassPathResource("/lang/${locale.toLanguageTag()}.properties").also {
            println(
                it
            )})
    }

    operator fun get(key: String): String? {
        return props[key]?.toString()
    }

    fun format(key: String, args: Array<out Any?>): String? {
        return props[key]?.toString()?.format(args)
    }

    override fun toString(): String {
        return locale.getDisplayName(locale)
    }
}
