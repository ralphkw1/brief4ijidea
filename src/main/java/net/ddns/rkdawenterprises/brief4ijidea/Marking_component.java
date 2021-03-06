package net.ddns.rkdawenterprises.brief4ijidea;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import static net.ddns.rkdawenterprises.brief4ijidea.MiscellaneousKt.has_selection;

public class Marking_component
{
    private static boolean s_is_marking_mode = false;
    private static LogicalPosition s_selection_origin = null;

    public static boolean is_marking_mode() { return s_is_marking_mode; }

    public static boolean toggle_marking_mode( @NotNull Editor editor )
    {
        s_is_marking_mode = !s_is_marking_mode;
        if( s_is_marking_mode )
        {
            enable_marking_mode( editor );
        }
        else
        {
            stop_marking_mode( editor,
                               true );
        }

        return s_is_marking_mode;
    }

    private static Key_adapter s_key_adapter = null;
    private static Mouse_adapter s_mouse_adapter = null;

    public static void enable_marking_mode( @NotNull Editor editor )
    {
        add_key_handlers( editor );

        s_is_marking_mode = true;

        State_component.status_bar_message( "<MARKING-MODE>" );

        s_selection_origin = editor.getCaretModel()
                                   .getLogicalPosition();

        editor.getCaretModel()
              .moveCaretRelatively( 1,
                                    0,
                                    true,
                                    false,
                                    true );
    }

    public static void stop_marking_mode( @NotNull Editor editor,
                                          boolean remove_selection )
    {
        s_is_marking_mode = false;
        s_selection_origin = null;

        State_component.status_bar_message( null );

        remove_key_handlers( editor );

        if( remove_selection )
        {
            if( has_selection( editor ) )
            {
                editor.getCaretModel()
                      .removeSecondaryCarets();
                editor.getSelectionModel()
                      .removeSelection();
            }
        }
    }

    private static void add_key_handlers( @NotNull Editor editor )
    {
        EditorActionManager editor_action_manager = EditorActionManager.getInstance();

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_UP,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_UP ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_UP_WITH_SELECTION ),
                                                                           KeyEvent.VK_UP ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN_WITH_SELECTION ),
                                                                           KeyEvent.VK_DOWN ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT_WITH_SELECTION ),
                                                                           KeyEvent.VK_RIGHT ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT_WITH_SELECTION ),
                                                                           KeyEvent.VK_LEFT ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP_WITH_SELECTION ),
                                                                           KeyEvent.VK_PAGE_UP ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN_WITH_SELECTION ),
                                                                           KeyEvent.VK_PAGE_DOWN ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_DELETE,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_DELETE ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_DELETE ),
                                                                           KeyEvent.VK_DELETE ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_BACKSPACE,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_BACKSPACE ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_BACKSPACE ),
                                                                           KeyEvent.VK_BACK_SPACE ) );

        editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_ENTER,
                                                new Editor_action_handler( editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_ENTER ),
                                                                           editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_ENTER ),
                                                                           KeyEvent.VK_ENTER ) );

        // TODO: Handle cursor movement commands also (or add actions for those)...

        s_key_adapter = new Marking_component.Key_adapter( editor );
        editor.getContentComponent()
              .addKeyListener( s_key_adapter );

        s_mouse_adapter = new Marking_component.Mouse_adapter( editor );
        editor.getContentComponent()
              .addMouseListener( s_mouse_adapter );
    }

    private static void remove_key_handlers( @NotNull Editor editor )
    {
        editor.getContentComponent()
              .removeKeyListener( s_key_adapter );
        editor.getContentComponent()
              .removeMouseListener( s_mouse_adapter );

        EditorActionManager editor_action_manager = EditorActionManager.getInstance();

        EditorActionHandler handler;

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_UP );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_UP,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_DOWN,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_LEFT,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_RIGHT,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_UP,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_MOVE_CARET_PAGE_DOWN,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_DELETE );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_DELETE,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_BACKSPACE );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_BACKSPACE,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }

        handler = editor_action_manager.getActionHandler( IdeActions.ACTION_EDITOR_ENTER );
        if( handler instanceof Editor_action_handler )
        {
            editor_action_manager.setActionHandler( IdeActions.ACTION_EDITOR_ENTER,
                                                    ( (Editor_action_handler)handler ).m_original_handler );
        }
    }

    public static void marking_post_handler( @NotNull Editor editor,
                                             int key_code )
    {
        if( ( key_code == KeyEvent.VK_DELETE ) ||
                ( key_code == KeyEvent.VK_BACK_SPACE ) ||
                ( key_code == KeyEvent.VK_ENTER ) )
        {
            stop_marking_mode( editor,
                               false );
            return;
        }

        LogicalPosition caret_logical_position = editor.getCaretModel()
                                                       .getCurrentCaret()
                                                       .getLogicalPosition();

        if( caret_logical_position.compareTo( s_selection_origin ) > 0 )
        {
            LogicalPosition start = s_selection_origin;
            LogicalPosition end = caret_logical_position;

            editor.getCaretModel()
                  .getCurrentCaret()
                  .setSelection( editor.logicalPositionToOffset( start ),
                                 editor.logicalPositionToOffset( end ) );
            return;
        }

        if( caret_logical_position.compareTo( s_selection_origin ) < 0 )
        {
            LogicalPosition start = caret_logical_position;
            LogicalPosition end = s_selection_origin;

            editor.getCaretModel()
                  .getCurrentCaret()
                  .setSelection( editor.logicalPositionToOffset( start ),
                                 editor.logicalPositionToOffset( end ) );
            return;
        }

        if( caret_logical_position.compareTo( s_selection_origin ) == 0 )
        {
            LogicalPosition start = s_selection_origin;
            LogicalPosition end = caret_logical_position;

            editor.getCaretModel()
                  .getCurrentCaret()
                  .setSelection( editor.logicalPositionToOffset( start ),
                                 editor.logicalPositionToOffset( end ) );
            return;
        }
    }

    public static final class Editor_action_handler
            extends EditorActionHandler
    {
        private final EditorActionHandler m_original_handler;
        private final EditorActionHandler m_substitute_handler;
        private final int m_key_code;

        public Editor_action_handler( @NotNull EditorActionHandler original_handler,
                                      @Nullable EditorActionHandler substitute_handler,
                                      int key_code )
        {
            m_original_handler = original_handler;
            m_substitute_handler = substitute_handler;
            m_key_code = key_code;
        }

        /**
         * Executes the action in the context of given caret. Subclasses should override this method.
         *
         * @param editor      the editor in which the action is invoked.
         * @param caret       the caret for which the action is performed at the moment, or {@code null} if it's a
         *                    'one-off' action executed without current context
         * @param dataContext the data context for the action.
         */
        @Override
        protected void doExecute( @NotNull Editor editor,
                                  @Nullable Caret caret,
                                  DataContext dataContext )
        {
            Objects.requireNonNullElse( m_substitute_handler,
                                        m_original_handler )
                   .execute( editor,
                             caret,
                             dataContext );

            marking_post_handler( editor,
                                  m_key_code );
        }
    }

    public static final class Key_adapter
            extends KeyAdapter
    {
        private final Editor m_editor;

        public Key_adapter( @NotNull Editor editor ) { m_editor = editor; }

        @Override
        public void keyTyped( KeyEvent e )
        {
            stop_marking_mode( m_editor,
                               true );
        }
    }

    public static final class Mouse_adapter
            extends MouseAdapter
    {
        private final Editor m_editor;

        public Mouse_adapter( @NotNull Editor editor ) { m_editor = editor; }

        @Override
        public void mouseClicked( MouseEvent e )
        {
            stop_marking_mode( m_editor,
                               true );
        }
    }
}
