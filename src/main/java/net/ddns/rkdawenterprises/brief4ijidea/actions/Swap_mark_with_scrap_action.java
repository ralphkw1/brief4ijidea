
package net.ddns.rkdawenterprises.brief4ijidea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;

import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.do_action;
import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.stop_all_marking_modes;
import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.has_selection;

@SuppressWarnings({ "ComponentNotRegistered", "unused" })
public class Swap_mark_with_scrap_action
        extends Plugin_action
{
    public Swap_mark_with_scrap_action( String text,
                                        String description )
    {
        super( text,
               description );
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed( @NotNull AnActionEvent e )
    {
        Editor editor = e.getData( CommonDataKeys.EDITOR );
        Project project = e.getData( CommonDataKeys.PROJECT );
        PsiFile file = e.getData( CommonDataKeys.PSI_FILE );
        if( ( editor == null ) || ( project == null ) || ( file == null ) ) return;

        if( !has_selection( editor ) ) return;

        if( !CopyPasteManager.getInstance()
                             .areDataFlavorsAvailable( DataFlavor.stringFlavor ) ) return;

        String text = CopyPasteManager.getInstance()
                                      .getContents( DataFlavor.stringFlavor );

        if( ( text == null ) || !( text.length() > 0 ) ) return;

        do_action( "$Cut", e );

        stop_all_marking_modes( editor );

        WriteCommandAction.runWriteCommandAction( project,
                                                  e.getPresentation()
                                                   .getText(),
                                                  null,
                                                  () -> editor.getDocument()
                                                              .insertString( editor.getCaretModel()
                                                                                   .getCurrentCaret()
                                                                                   .getOffset(),
                                                                             text ),
                                                  file );
    }
}
