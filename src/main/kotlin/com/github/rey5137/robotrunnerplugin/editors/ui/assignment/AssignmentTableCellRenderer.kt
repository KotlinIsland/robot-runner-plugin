package com.github.rey5137.robotrunnerplugin.editors.ui.assignment

import com.github.rey5137.robotrunnerplugin.editors.xml.*
import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.SimpleTextAttributes
import javax.swing.JTable

class AssignmentTableCellRenderer(private val model: AssignmentModel) : ColoredTableCellRenderer() {

    override fun customizeCellRenderer(
        table: JTable?,
        value: Any?,
        selected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ) {
        val assignment = model.getAssignment(row)
        if(assignment.name.isNotEmpty()) {
            val sign = when (assignment.assignmentType) {
                AssignmentType.SINGLE -> ARG_SINGLE
                AssignmentType.ARRAY -> ARG_ARRAY
            }.toString()
            if (sign.isNotEmpty()) {
                append(sign, SimpleTextAttributes.GRAY_ATTRIBUTES)
                append(ARG_NAME_START.toString(), SimpleTextAttributes.GRAY_ATTRIBUTES)
            }
            append(assignment.name, SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES)
            if (sign.isNotEmpty())
                append(ARG_NAME_END.toString(), SimpleTextAttributes.GRAY_ATTRIBUTES)
        }
    }

}