@file:Suppress("RedundantSemicolon",
               "ComponentNotRegistered",
               "unused",
               "ClassName",
               "FunctionName")

package net.ddns.rkdawenterprises.brief4ijidea.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.undo.UndoManager
import net.ddns.rkdawenterprises.brief4ijidea.do_action
import net.ddns.rkdawenterprises.brief4ijidea.get_undo_manager

class Undo_action(text: String?,
                  description: String?) : Plugin_action(text,
                                                        description)
{
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent)
    {
        val dataContext: DataContext = e.dataContext
        val editor = PlatformDataKeys.FILE_EDITOR.getData(dataContext)
        val project = CommonDataKeys.PROJECT.getData(dataContext)
        val undoManager: UndoManager = get_undo_manager(project)
        if(undoManager.isUndoAvailable(editor))
        {
            do_action("\$Undo",
                      e,
                      this);
        }
    }
}