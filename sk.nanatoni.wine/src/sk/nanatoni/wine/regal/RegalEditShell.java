package sk.nanatoni.wine.regal;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import sk.nanatoni.wine.WineMonitoring;
import sk.nanatoni.wine.entity.Regal;
import sk.nanatoni.wine.entity.Wine;
import sk.nanatoni.wine.evidence.WineTableModel;

public class RegalEditShell {
  private Text txWine;
  private Text txAmount;
  private Spinner spInsert;
  private Spinner spRemove;
  private int posX;
  private int posY;
  private Wine wine;
  private Regal regal;
  private WineTableModel wineTableModel;
  private LinkedList<Wine> lWinesData = new LinkedList<Wine>();
  private Shell shell;
  private List lWines;
  private double defaultAmount = 0.0;
    
  public RegalEditShell (WineTableModel wineTableModel , Text txWine){
    super();
    this.txWine = txWine;
    this.posX = ((Integer)txWine.getData(RegalComposite.POSX_DATA));
    this.posY = ((Integer)txWine.getData(RegalComposite.POSY_DATA));
    this.wineTableModel = wineTableModel;
    Object object = txWine.getData(RegalComposite.WINE_DATA);
    if (object != null){
      this.wine = (Wine)object;
      this.regal = null;
      if (wine.getRegals() != null) {
        Iterator<Regal> itRegal = wine.getRegals().iterator();
        while (itRegal.hasNext() && (this.regal == null)){
          Regal regal = itRegal.next();
          if (regal.getxPos() == this.posX && regal.getyPos() == this.posY){
            this.regal = regal;
            this.defaultAmount = regal.getAmount();
          }
        }
      }
    }
    else{
      this.wine = null;
      this.regal = null;
    }
  }
  
  public void open(){
    shell = new Shell(SWT.APPLICATION_MODAL | SWT.SHELL_TRIM);
    shell.setText("Editovanie regalu");
    shell.setLayout(new GridLayout(2,false));
    shell.setLayoutData(new GridData (GridData.FILL_BOTH));
    Group grWine = new Group(shell, SWT.NONE);
    GridData gdGrWine = new GridData (GridData.FILL_BOTH);
    gdGrWine.verticalSpan = 2;
    grWine.setLayout(new GridLayout(1,false));
    grWine.setLayoutData(gdGrWine);
    grWine.setText(" Vyber vin: ");
    lWines = new List (grWine , SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    lWines.setLayoutData(new GridData (GridData.FILL_BOTH));
    lWines.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        wineSelectionChanged();        
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    Group grWineEdit = new Group(shell, SWT.NONE);
    grWineEdit.setLayout(new GridLayout (2, false));
    grWineEdit.setText(" Editacia regala: ");
    Label lbRegal = new Label (grWineEdit , SWT.NONE);
    lbRegal.setText("Regal: ");
    Text txRegal = new Text (grWineEdit, SWT.BORDER | SWT.READ_ONLY);
    txRegal.setText((char)((int)'A' + this.posX) + String.valueOf(this.posY + 1));
    txRegal.setBackground(RegalComposite.BGCOLOR_READ_ONLY_TEXT);
    Label lbAmount = new Label (grWineEdit , SWT.NONE);
    lbAmount.setText("Mnozstvo: ");
    txAmount = new Text (grWineEdit, SWT.BORDER | SWT.READ_ONLY);
    txAmount.setText(this.regal != null ? 
        WineMonitoring.DECIMAL_FORMAT.format(this.regal.getAmount()) : 
        WineMonitoring.DECIMAL_FORMAT.format(0));
    txAmount.setBackground(RegalComposite.BGCOLOR_READ_ONLY_TEXT);
    Label lbInsert = new Label (grWineEdit , SWT.NONE);
    lbInsert.setText("Vklad: ");
    spInsert = new Spinner(grWineEdit, SWT.BORDER);
    spInsert.setDigits(2);
    spInsert.setMinimum(0);
    spInsert.setMaximum(4000);
    spInsert.setIncrement(100);
    spInsert.setSelection(0);
    spInsert.addListener(SWT.CHANGED, new Listener() {
      @Override
      public void handleEvent(Event arg0) {
      spinnerInsertChanged();  
      }
    });
    spInsert.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        spinnerInsertChanged();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    Label lbRemove = new Label (grWineEdit , SWT.NONE);
    lbRemove.setText("Vyber: ");
    spRemove = new Spinner(grWineEdit, SWT.BORDER);
    spRemove.setDigits(2);
    spRemove.setMinimum(0);
    spRemove.setMaximum(4000);
    spRemove.setIncrement(100);
    spRemove.setSelection(0);
    spRemove.addListener(SWT.CHANGED, new Listener() {
      @Override
      public void handleEvent(Event arg0) {
      spinnerRemoveChanged();  
      }
    });
    spRemove.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        spinnerRemoveChanged();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    int width = spInsert.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
    GridData glTxRegal = new GridData();
    glTxRegal.widthHint = width;
    txRegal.setLayoutData(glTxRegal);
    GridData glTxAmount = new GridData();
    glTxAmount.widthHint = width;
    txAmount.setLayoutData(glTxAmount);
    Composite cmpTemp = new Composite(shell, SWT.NONE);
    cmpTemp.setLayoutData(new GridData (GridData.FILL_VERTICAL));
    Button btnSave = new Button(shell, SWT.FLAT);
    btnSave.setText("  Ulozit  ");
    btnSave.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        saveRegal();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    Button btnCancel = new Button(shell, SWT.FLAT);
    btnCancel.setText("  Zavriet  ");
    btnCancel.setLayoutData(new GridData (GridData.HORIZONTAL_ALIGN_END));
    btnCancel.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        closeRegalEditing();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    // populate wine list
    if (wineTableModel.getData() != null && wineTableModel.getData().size() > 0){
      lWinesData = new LinkedList<Wine>(wineTableModel.getData());
      lWines.add("Prazdny regal",0);
      Collections.sort(lWinesData);
      for (Wine wine : lWinesData){
        lWines.add(WineTableModel.getWineDesc(wine));
      }
    }
    if (wine != null){
      lWines.setSelection(lWinesData.indexOf(wine) + 1);
    }
    // Ask the shell to display its content
    shell.open();
    Display display = shell.getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }
  
  private void closeRegalEditing(){
    shell.close();
  }
  
  private void saveRegal() {
    double amount;
    try {
      amount = WineMonitoring.DECIMAL_FORMAT.parse(txAmount.getText()).doubleValue();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    Text txAmount = (Text) txWine.getData(RegalComposite.SIBLING_DATA);
    if (lWines.getSelectionIndex() != 0) {
      Wine selectedWine = lWinesData.get(lWines.getSelectionIndex() - 1);
      Wine dataWine = wineTableModel.getData().get(
          wineTableModel.getData().indexOf(selectedWine));
      txAmount.setText(WineMonitoring.DECIMAL_FORMAT.format(amount));
      txWine.setText(selectedWine.getCode());
      txWine.setData(RegalComposite.WINE_DATA, dataWine);
      txAmount.setData(RegalComposite.WINE_DATA, dataWine);
      if (!dataWine.equals(wine)) {
        if (dataWine.getRegals() != null) {
          Iterator<Regal> itRegal = dataWine.getRegals().iterator();
          Regal editedRegal = null;
          while (itRegal.hasNext() && editedRegal == null) {
            Regal currRegal = itRegal.next();
            if (currRegal.getxPos() == posX && currRegal.getyPos() == posY) {
              editedRegal = currRegal;
            }
          }
          if (editedRegal != null) {
            editedRegal.setAmount(amount);
            editedRegal.setSysDate(new Date());
          } else {
            editedRegal = new Regal(dataWine);
            editedRegal.setAmount(amount);
            editedRegal.setxPos(posX);
            editedRegal.setyPos(posY);
            editedRegal.setSysDate(new Date());
            dataWine.getRegals().add(editedRegal);
          }
        } else {
          Regal editedRegal = new Regal(dataWine);
          editedRegal.setAmount(amount);
          editedRegal.setxPos(posX);
          editedRegal.setyPos(posY);
          editedRegal.setSysDate(new Date());
          LinkedList<Regal> regals = new LinkedList<Regal>();
          regals.add(editedRegal);
          dataWine.setRegals(regals);
        }
        if (regal != null){
          wine.getRegals().remove(regal);
        }
        wineTableModel.wineChanged(dataWine);
        if (wine != null){
          wineTableModel.wineChanged(wine);
        }
      } else {
        if (dataWine.getRegals() != null) {
          Iterator<Regal> itRegal = dataWine.getRegals().iterator();
          Regal editedRegal = null;
          while (itRegal.hasNext() && editedRegal == null) {
            Regal currRegal = itRegal.next();
            if (currRegal.getxPos() == posX && currRegal.getyPos() == posY) {
              editedRegal = currRegal;
            }
          }
          if (editedRegal != null) {
            editedRegal.setAmount(amount);
            editedRegal.setSysDate(new Date());
          } else {
            editedRegal = new Regal(dataWine);
            editedRegal.setAmount(amount);
            editedRegal.setxPos(posX);
            editedRegal.setyPos(posY);
            editedRegal.setSysDate(new Date());
            dataWine.getRegals().add(editedRegal);
          }
        } else {
          Regal editedRegal = new Regal(dataWine);
          editedRegal.setAmount(amount);
          editedRegal.setxPos(posX);
          editedRegal.setyPos(posY);
          editedRegal.setSysDate(new Date());
          LinkedList<Regal> regals = new LinkedList<Regal>();
          regals.add(editedRegal);
          dataWine.setRegals(regals);
        }
        wineTableModel.wineChanged(dataWine);
      }
    } else {
      if (this.regal != null) {
        this.wine.getRegals().remove(regal);
        txWine.setText("");
        txWine.setData(RegalComposite.WINE_DATA, null);
        txAmount.setText("");
        txAmount.setData(RegalComposite.WINE_DATA, null);
        wineTableModel.wineChanged(wine);
      }
    }
    RegalComposite.setTooltip(txAmount);
    RegalComposite.setTooltip(txWine);
    closeRegalEditing();
  }
  
  private void spinnerRemoveChanged(){
    spInsert.setSelection(0);
    double remove = spRemove.getSelection() / 100.0;
    double amount = defaultAmount - remove;
    if (amount < 0){
      amount = 0;
    }
    txAmount.setText(WineMonitoring.DECIMAL_FORMAT.format(amount));
  }
  
  private void spinnerInsertChanged(){
    spRemove.setSelection(0);
    double insert = spInsert.getSelection() / 100.0;
    txAmount.setText(WineMonitoring.DECIMAL_FORMAT.format(defaultAmount + insert));
  }
  
  private void wineSelectionChanged(){
    spInsert.setSelection(0);
    spRemove.setSelection(0);
    defaultAmount = 0.0;
    if (lWines.getSelectionIndex() > 0){
      spInsert.setEnabled(true);
      spRemove.setEnabled(true);
      Wine selectedWine = lWinesData.get(lWines.getSelectionIndex() - 1);
      if (selectedWine.equals(this.wine) &&
        this.regal != null){
        defaultAmount = regal.getAmount();
      }
    }
    else{
      spInsert.setEnabled(false);
      spRemove.setEnabled(false);
    }
    txAmount.setText(WineMonitoring.DECIMAL_FORMAT.format(defaultAmount));
  }
}
