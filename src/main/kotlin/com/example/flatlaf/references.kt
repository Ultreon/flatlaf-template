package com.example.flatlaf

import com.example.flatlaf.main.ProductJson
import com.google.gson.Gson
import java.net.URL

val gson = Gson()

private val productJson = gson.fromJson(ProductJson::class.java.getResourceAsStream("/product.json")!!.bufferedReader(), ProductJson::class.java)

// Resources
const val appIcon: String = "/icons/icon.png"
const val appBanner: String = "/images/banner.png"

// Resource references
val appIconRef: URL = ProductJson::class.java.getResource(appIcon)!!
val appBannerRef: URL = ProductJson::class.java.getResource(appBanner)!!

// Product properties.
val appId: String = productJson.id
val appName: String = productJson.name
val appVersion: String = productJson.version
val buildDate: String = productJson.buildDate
const val sourceUrl: String = "https://github.com/Ultreon/flatlaf-template"
const val issuesUrl: String = "https://github.com/Ultreon/flatlaf-template/issues"
const val newIssueUrl: String = "https://github.com/Ultreon/flatlaf-template/issues/new/choose"
