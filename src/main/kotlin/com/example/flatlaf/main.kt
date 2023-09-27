package com.example.flatlaf

import com.example.flatlaf.lang.LanguageManager
import com.example.flatlaf.main.AppPrefs
import com.example.flatlaf.main.MainFrame
import com.formdev.flatlaf.FlatLightLaf
import org.oxbow.swingbits.dialog.task.TaskDialogs
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess

fun main() {
    AppPrefs.init("me.qboi.texteditor")
    AppPrefs.setupLaf(arrayOf())

    Thread.setDefaultUncaughtExceptionHandler { _, ex ->
        crash(ex)
    }

    LanguageManager.registerDefaults()
    LanguageManager.freeze()

    do {
        MainFrame.isRunning = true
        SwingUtilities.invokeAndWait {
            MainFrame.start()
        }
        while (MainFrame.isRunning) {
            Thread.sleep(50)
        }
    } while (isRestart)
}

@Throws(Exception::class)
fun crash(e: Throwable) {
    try {
        MainFrame.instance.dispose()
    } catch (e: Exception) {
        // ignore
    }
    SwingUtilities.invokeLater {
        val lookAndFeel = UIManager.getLookAndFeel()
        UIManager.setLookAndFeel(FlatLightLaf())
        e.printStackTrace()
        TaskDialogs.showException(e)
        UIManager.setLookAndFeel(lookAndFeel)
        exitProcess(1)
    }
}
