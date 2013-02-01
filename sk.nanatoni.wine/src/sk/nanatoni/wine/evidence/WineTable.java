package sk.nanatoni.wine.evidence;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import sk.nanatoni.wine.WineMonitoring;

public class WineTable extends Composite{
  private Table table = null;
  private Button add = null;
  private Button delete = null;
  private Button close = null;
  
  public Button getAdd() {
    return add;
  }
  public Button getDelete() {
    return delete;
  }
  public Button getClose() {
    return close;
  }
  
  public Table getTable() {
    return table;
  }
  public WineTable(Composite parent, int style){
    super(parent,style);
    this.setLayout(new GridLayout(1,false));
    createTable(this);
    createButtonsComposite(this);
  }
  private void createTable (Composite parent){
    table = new Table(parent, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
        SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
      GridData gridData = new GridData(GridData.FILL_BOTH);
      gridData.grabExcessVerticalSpace = true;
      gridData.horizontalSpan = 4;
      table.setLayoutData(gridData);   
      table.setLinesVisible(true);
      table.setHeaderVisible(true);
      for (int index = 0 ; index < WineTableLabelProvider.COLUMN_NAMES.length ; index ++){
        TableColumn column = new TableColumn(table, 
          (index < 4 || index > 7 ? SWT.LEFT : SWT.RIGHT),
          index);   
        column.setText(WineTableLabelProvider.COLUMN_NAMES[index]);
      }    
  }
  private void createButtonsComposite (Composite parent){
    Composite buttons = new Composite (parent,SWT.NONE);
    buttons.setLayout(new GridLayout(4,false));
    buttons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    add = new Button(buttons,SWT.PUSH | SWT.CENTER);
    add.setText(" Pridaj ");
    GridData gdAdd = new GridData ();
    gdAdd.widthHint = WineMonitoring.BUTTONS_MIN_WIDTH;
    add.setLayoutData(gdAdd);
    delete = new Button(buttons,SWT.PUSH | SWT.CENTER);
    delete.setText(" Zmaz ");
    GridData gdDelete = new GridData ();
    gdDelete.widthHint = WineMonitoring.BUTTONS_MIN_WIDTH;
    delete.setLayoutData(gdDelete);
    Label dummyLabel = new Label(buttons,SWT.NONE);
    GridData gridDataDummy = new GridData(GridData.FILL_HORIZONTAL);
    dummyLabel.setLayoutData(gridDataDummy);
    close = new Button(buttons,SWT.PUSH | SWT.CENTER);
    GridData gdClose = new GridData ();
    gdClose.widthHint = WineMonitoring.BUTTONS_MIN_WIDTH;
    close.setLayoutData(gdClose);
    close.setText(" Zavri ");
  }
}
