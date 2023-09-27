package com.example.flatlaf.dialog.settings

import com.example.flatlaf.dialog.StandardDialog
import java.awt.BorderLayout
import java.awt.Dialog
import java.awt.Dimension
import java.awt.Frame
import javax.swing.JPanel
import javax.swing.JTabbedPane

@Suppress("unused")
class SettingsDialog : StandardDialog {
    private lateinit var languageTab: LanguageTab
    private lateinit var themesTab: ThemesTab

    constructor(owner: Frame?, title: String?, modal: Boolean) : super(owner, title, modal) {
        contentPane = createContent()
    }

    constructor(owner: Dialog?, title: String?, modal: Boolean) : super(owner, title, modal) {
        contentPane = createContent()
    }

    private fun createContent(): JPanel {
        minimumSize = Dimension(400, 400)

        val content = JPanel(BorderLayout())
        val tabs = JTabbedPane(JTabbedPane.LEFT)

        val themesTab = JPanel(BorderLayout())
        this.themesTab = ThemesTab()
        themesTab.add(this.themesTab, BorderLayout.CENTER)
        tabs.addTab("Themes", themesTab)

        val languageTab = JPanel(BorderLayout())
        this.languageTab = LanguageTab()
        languageTab.add(this.languageTab, BorderLayout.CENTER)
        tabs.addTab("Language", languageTab)

        content.add(tabs)
        content.add(createButtonPanel(), BorderLayout.SOUTH)

        return content
    }
}