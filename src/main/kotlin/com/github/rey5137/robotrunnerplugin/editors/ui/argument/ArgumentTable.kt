package com.github.rey5137.robotrunnerplugin.editors.ui.argument

import com.github.rey5137.robotrunnerplugin.editors.ui.openFile
import com.intellij.openapi.project.Project
import com.intellij.ui.table.JBTable
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTable


class ArgumentTable(project: Project, private val argumentModel: ArgumentModel) : JBTable(argumentModel) {

    init {
        cellSelectionEnabled = true
        autoResizeMode = JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        columnModel.getColumn(ArgumentModel.INDEX_ARGUMENT).apply {
            cellRenderer = ArgumentTableCellRenderer(argumentModel)
        }
        columnModel.getColumn(ArgumentModel.INDEX_INPUT).apply {
            cellRenderer = InputTableCellRenderer(argumentModel)
            cellEditor = InputTableCellEditor(argumentModel)
        }
        columnModel.getColumn(ArgumentModel.INDEX_VALUE).apply {
            cellRenderer = ValueTableCellRenderer(argumentModel)
            cellEditor = ValueTableCellEditor(project, this@ArgumentTable, argumentModel)
        }
        setDefaultEditor(Any::class.java, StringCellEditor())
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                e.isIconClicked { row, model, variableRow ->
                    if(model != null) {
                        val item = model.getItem(variableRow)
                        if (!item.isLeaf || item.isFilePath)
                            clearSelection()
                    }
                    else {
                        val item = argumentModel.getItem(row)
                        if(item.isFilePath)
                            clearSelection()
                    }
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                e.isIconClicked { row, model, variableRow ->
                    if(model != null) {
                        val item = model.getItem(variableRow)
                        if (!item.isLeaf) {
                            if (item.isExpanded)
                                model.collapseAt(variableRow)
                            else
                                model.expandAt(variableRow)
                            argumentModel.fireTableRowsUpdated(row, row)
                        } else if (item.isFilePath)
                            project.openFile(item.variable.value.toString())
                    }
                    else {
                        val item = argumentModel.getItem(row)
                        if(item.isFilePath)
                            project.openFile(item.argument.value.toString())
                    }
                }
            }

            private fun MouseEvent.isIconClicked(func: (rowIndex: Int, model: VariableModel?, variableRow: Int) -> Unit) {
                val p = Point(point)
                val row = rowAtPoint(p)
                val column = columnAtPoint(p)
                if(column == ArgumentModel.INDEX_VALUE) {
                    val variableModel = argumentModel.getVariableModel(row)
                    if(variableModel != null) {
                        val rect = getCellRect(row, column, false)
                        p.translate(-rect.x, -rect.y)
                        val variableRow = p.y * variableModel.rowCount / rect.height
                        val item = variableModel.getItem(variableRow)
                        if (VariableCellRender.isIconClicked(p.x, item.level))
                            func(row, variableModel, variableRow)
                    } else {
                        val rect = getCellRect(row, column, false)
                        p.translate(-rect.x, -rect.y)
                        if(ValueTableCellRenderer.isIconClicked(p.x))
                            func(row, null, 0)
                    }
                }
            }

        })
    }

    override fun scrollRectToVisible(aRect: Rectangle?) {
        //disable auto scroll
    }
}