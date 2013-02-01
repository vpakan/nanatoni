package sk.nanatoni.wine;

import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;

public class EditorNavigationStrategy {
  private static final int navigation = ColumnViewerEditor.TABBING_HORIZONTAL
    | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
    | ColumnViewerEditor.TABBING_VERTICAL
    | ColumnViewerEditor.KEYBOARD_ACTIVATION;
  
  public static void setUpTableViewerNavigationStrategy (TableViewer tableViewer){
    final ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
      tableViewer) {
      protected boolean isEditorActivationEvent(
              ColumnViewerEditorActivationEvent event) {
          return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
                   || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
                   || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION
                   || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR)
                   || event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
        }
    };
    TableViewerEditor.create(tableViewer, actSupport,navigation); 
  }
}
