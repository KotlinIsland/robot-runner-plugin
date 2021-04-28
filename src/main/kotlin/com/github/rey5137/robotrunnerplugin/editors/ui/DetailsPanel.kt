package com.github.rey5137.robotrunnerplugin.editors.ui

import com.github.rey5137.robotrunnerplugin.editors.ui.argument.*
import com.github.rey5137.robotrunnerplugin.editors.xml.*
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.migLayout.createLayoutConstraints
import com.intellij.ui.table.JBTable
import icons.MyIcons
import net.miginfocom.layout.CC
import net.miginfocom.swing.MigLayout
import javax.swing.JPanel
import javax.swing.JTable


class DetailsPanel(private val robotElement: RobotElement)
    : JPanel(MigLayout(createLayoutConstraints(10, 10))) {

    private val nameField = JBTextField()
    private val statusLabel = JBLabel()
    private val tagsField = JBTextField()
    private val tabPane = JBTabbedPane()
    private val argumentModel = ArgumentModel()
    private val argumentTable =  JBTable(argumentModel)
    private val messagePanel = JPanel()

    init {
        add(statusLabel, CC().cell(0, 0).minWidth("32px"))
        add(nameField, CC().cell(0, 0).growX().pushX(1F))
        nameField.isEditable = false

        add(JBLabel("Tags"), CC().cell(0, 1).minWidth("32px"))
        add(tagsField, CC().cell(0, 1).growX().pushX(1F))
        tagsField.isEditable = false

        add(tabPane, CC().newline().grow().push(1F, 1F))
        argumentTable.cellSelectionEnabled = true
        argumentTable.autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        argumentTable.columnModel.getColumn(ArgumentModel.INDEX_ARGUMENT).apply {
            cellRenderer = ArgumentTableCellRenderer(argumentModel)
        }
        argumentTable.columnModel.getColumn(ArgumentModel.INDEX_INPUT).apply {
            cellRenderer = InputTableCellRenderer(argumentModel)
            cellEditor = InputTableCellEditor(argumentModel)
        }
        argumentTable.columnModel.getColumn(ArgumentModel.INDEX_VALUE).apply {
            cellRenderer = ValueTableCellRenderer(argumentModel)
            cellEditor = ValueTableCellEditor(argumentModel)
        }
        argumentTable.setDefaultEditor(Any::class.java, StringCellEditor())
    }

    fun showDetails(element: Element) {
        if (element is HasCommonField) {
            nameField.text = element.name
            nameField.select(0, 0)
            statusLabel.icon = if (element.status.isPassed) MyIcons.StatusPass else MyIcons.StatusFail
        }

        if(element is HasTagsField) {
            tagsField.text = element.tags.joinToString(separator = ", ")
        }
        else {
            tagsField.text = ""
        }

        tabPane.removeAll()
        if(element is KeywordElement) {
            val messagePanel = ToolbarDecorator.createDecorator(argumentTable)
                .disableUpAction()
                .disableDownAction()
                .disableRemoveAction()
                .createPanel()
            tabPane.add("Arguments", messagePanel)
            argumentModel.populateModel(element)
            argumentTable.adjustColumn(ArgumentModel.INDEX_ARGUMENT)
            argumentTable.adjustColumn(ArgumentModel.INDEX_INPUT)

            tabPane.add("Messages", this.messagePanel)
        }
    }

    private fun ArgumentModel.populateModel(element: KeywordElement) {
        val message = element.messages.asSequence()
            .filter { it.level == "TRACE"}
            .mapNotNull { robotElement.messageMap[it.valueIndex] }
            .find { it.isArgumentMessage() }
        if(message == null)
            setArguments(emptyList(), emptyList())
        else {
            try {
                val arguments = message.parseArguments()
                val inputArguments = arguments.parseArgumentInputs(element.arguments)
                setArguments(arguments, inputArguments)
            }
            catch (ex: Exception) {
                ex.printStackTrace()
                setArguments(emptyList(), emptyList())
            }
        }
    }

}