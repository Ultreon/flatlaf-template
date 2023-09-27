package com.example.flatlaf.main

import com.example.flatlaf.appId
import com.example.flatlaf.intellijthemes.IJThemesPanel
import com.example.flatlaf.lang.LanguageManager
import com.formdev.flatlaf.FlatLaf
import com.formdev.flatlaf.FlatLightLaf
import com.formdev.flatlaf.FlatPropertiesLaf
import com.formdev.flatlaf.IntelliJTheme
import com.formdev.flatlaf.util.LoggingFacade
import com.formdev.flatlaf.util.StringUtils
import org.apache.commons.lang.SystemUtils
import java.awt.Font
import java.beans.PropertyChangeEvent
import java.io.File
import java.io.FileInputStream
import java.util.*
import java.util.prefs.Preferences
import javax.swing.UIManager

/**
 * @author Karl Tauber
 */
object AppPrefs {
    val KEY_LAF = "$appId.laf"
    val KEY_LAF_THEME = "$appId.lafTheme"
    val KEY_WORD_WRAP = "$appId.wordWrap"
    val KEY_FONT_NAME = "$appId.fontName"
    val KEY_FONT_SIZE = "$appId.fontSize"
    val KEY_FONT_STYLE = "$appId.fontStyle"
    val KEY_LANGUAGE = "$appId.language"
    val KEY_RECENT_FILES = "$appId.recentFiles"
    const val RESOURCE_PREFIX = "res:"
    const val FILE_PREFIX = "file:"
    val THEME_UI_KEY = "$appId.theme"
    var fontName: String
        get() = Preferences.userRoot().get(KEY_FONT_NAME, "SansSerif")
        set(value) = Preferences.userRoot().put(KEY_FONT_NAME, value)
    var fontSize: Int
        get() = Preferences.userRoot().getInt(KEY_FONT_SIZE, 12)
        set(value) = Preferences.userRoot().putInt(KEY_FONT_SIZE, value)
    var fontStyle: Int
        get() = Preferences.userRoot().getInt(KEY_FONT_STYLE, Font.PLAIN)
        set(value) = Preferences.userRoot().putInt(KEY_FONT_STYLE, value)
    var wordWrap: Boolean
        get() = Preferences.userRoot().getBoolean(KEY_WORD_WRAP, true)
        set(value) = Preferences.userRoot().putBoolean(KEY_WORD_WRAP, value)
    var language: Locale
        get() {
            return try {
                Locale.forLanguageTag(Preferences.userRoot().get(KEY_LANGUAGE, "en-US"))
            } catch (e: Exception) {
                Preferences.userRoot().put(KEY_LANGUAGE, LanguageManager.fallbackId)
                Locale.forLanguageTag(LanguageManager.fallbackId)
            }
        }
        set(value) = Preferences.userRoot().put(KEY_LANGUAGE, value.toLanguageTag())
    var recentFiles: List<File>
        get() = Preferences.userRoot()
            .get(KEY_RECENT_FILES, "")
            .split(SystemUtils.PATH_SEPARATOR)
            .filter { it.isNotEmpty() }
            .map { File(it) }
            .filter { it.exists() && it.isFile }
        set(value) = Preferences.userRoot()
            .put(KEY_RECENT_FILES, value.joinToString(SystemUtils.PATH_SEPARATOR) { it.absolutePath })
    lateinit var state: Preferences
        private set

    fun init(rootPath: String?) {
        state = Preferences.userRoot().node(rootPath)
    }

    fun setupLaf(args: Array<String?>) {
        // set look and feel
        try {
            if (args.isNotEmpty()) UIManager.setLookAndFeel(args[0]) else {
                val lafClassName = state[KEY_LAF, FlatLightLaf::class.java.name]
                if (IntelliJTheme.ThemeLaf::class.java.name == lafClassName) {
                    val theme = state[KEY_LAF_THEME, ""]
                    if (theme.startsWith(RESOURCE_PREFIX)) IntelliJTheme.setup(
                        IJThemesPanel::class.java.getResourceAsStream(
                            IJThemesPanel.THEMES_PACKAGE + theme.substring(
                                RESOURCE_PREFIX.length
                            )
                        )
                    ) else if (theme.startsWith(FILE_PREFIX)) FlatLaf.setup(
                        IntelliJTheme.createLaf(
                            FileInputStream(
                                theme.substring(
                                    FILE_PREFIX.length
                                )
                            )
                        )
                    ) else FlatLightLaf.setup()
                    if (theme.isNotEmpty()) UIManager.getLookAndFeelDefaults()[THEME_UI_KEY] = theme
                } else if (FlatPropertiesLaf::class.java.name == lafClassName) {
                    val theme = state[KEY_LAF_THEME, ""]
                    if (theme.startsWith(FILE_PREFIX)) {
                        val themeFile = File(theme.substring(FILE_PREFIX.length))
                        val themeName = StringUtils.removeTrailing(themeFile.name, ".properties")
                        FlatLaf.setup(FlatPropertiesLaf(themeName, themeFile))
                    } else FlatLightLaf.setup()
                    if (theme.isNotEmpty()) UIManager.getLookAndFeelDefaults()[THEME_UI_KEY] = theme
                } else UIManager.setLookAndFeel(lafClassName)
            }
        } catch (ex: Throwable) {
            LoggingFacade.INSTANCE.logSevere(null, ex)

            // fallback
            FlatLightLaf.setup()
        }

        // remember active look and feel
        UIManager.addPropertyChangeListener { e: PropertyChangeEvent ->
            if ("lookAndFeel" == e.propertyName) state.put(
                KEY_LAF, UIManager.getLookAndFeel().javaClass.name
            )
        }
    }
}