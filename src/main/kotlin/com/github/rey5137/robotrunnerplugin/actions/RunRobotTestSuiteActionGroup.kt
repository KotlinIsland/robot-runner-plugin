package com.github.rey5137.robotrunnerplugin.actions

import com.github.rey5137.robotrunnerplugin.configurables.RobotRunProjectSettingsState
import com.github.rey5137.robotrunnerplugin.runconfigurations.RobotRunConfigurationType
import com.intellij.execution.RunManager
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class RunRobotTestSuiteActionGroup : ActionGroup() {

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        val project = e?.project ?: return emptyArray()
        val configurationMap = RunManager.getInstance(project).getConfigurationSettingsList(RobotRunConfigurationType::class.java)
            .map { it.uniqueID to it }
            .toMap()
        return RobotRunProjectSettingsState.getInstance(project).settingMap.entries.filter { it.value.testSuiteEnable }
            .mapNotNull { configurationMap[it.key] }
            .map { RunRobotTestSuiteAction(it) }
            .toTypedArray()
    }

}