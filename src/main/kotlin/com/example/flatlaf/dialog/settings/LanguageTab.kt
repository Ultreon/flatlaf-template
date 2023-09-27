package com.example.flatlaf.dialog.settings

import com.example.flatlaf.lang.Language
import com.example.flatlaf.lang.LanguageManager
import com.example.flatlaf.main.MainFrame
import java.awt.BorderLayout
import java.awt.Window
import javax.swing.JList
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.ListSelectionModel

class LanguageTab : JPanel(BorderLayout()) {
    init {
        name = "Language"

        add(LanguageList(), BorderLayout.CENTER)
    }

    inner class LanguageList : JList<Language>(LanguageManager.registry) {
        init {
            selectionMode = ListSelectionModel.SINGLE_SELECTION
            selectedIndex = LanguageManager.registry.indexOf(LanguageManager.current)

            addListSelectionListener {
                val language = selectedValue!!
                println("Language set to: $language")
                val text = LanguageManager["dialog.language.restart"]
                val title = LanguageManager["dialog.language.restart.title"]
                LanguageManager.current = language
                for (window in Window.getWindows()) {
//                    if (window is Translatable) {
//                        window.onLanguageChanged(language)
//                    }
                    window.invalidate()
                    window.validate()
                    window.revalidate()
                    window.repaint()
                }

                if (JOptionPane.showConfirmDialog(this, text, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    MainFrame.instance.restart()
                }
            }
        }
    }
}
