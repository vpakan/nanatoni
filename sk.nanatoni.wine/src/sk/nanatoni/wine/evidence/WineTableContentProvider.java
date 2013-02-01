package sk.nanatoni.wine.evidence;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class WineTableContentProvider implements IStructuredContentProvider{
  private WineTableModel orderTableModel = null;
  
  public WineTableContentProvider (WineTableModel orderTableModel){
    this.orderTableModel = orderTableModel;
  }
  
  public void inputChanged(Viewer v, Object oldInput, Object newInput) {
  }
  
  public Object[] getElements(Object parent) {
    return orderTableModel.getData().toArray();
  }
  
  public void dispose() {
    // not implemented yet
  }
}
