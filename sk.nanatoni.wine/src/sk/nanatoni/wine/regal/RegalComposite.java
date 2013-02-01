package sk.nanatoni.wine.regal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import sk.nanatoni.wine.WineMonitoring;
import sk.nanatoni.wine.entity.Regal;
import sk.nanatoni.wine.entity.Wine;
import sk.nanatoni.wine.evidence.WineTableModel;

public class RegalComposite extends Composite{
  private List<Text> wineTexts;
  private List<Text> amountTexts;
  private WineTableModel wineTableModel;
  private Text txDetail;
  private Text txSearch;
  private int searchForIndex = -1;
  public static final String POSX_DATA = "posXData";
  public static final String POSY_DATA = "posYData";
  public static final String SIBLING_DATA = "siblingData";
  public static final String WINE_DATA = "wineData";
  public static final String TYPE_DATA = "typeData";
  public static final Color BGCOLOR_READ_ONLY_TEXT = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
  public static final Color BGCOLOR_REGAL_LABEL = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
  public static final Color FGCOLOR_REGAL_LABEL = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
  public static final Color BGCOLOR_AMOUNT_TEXT = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
  public static final Color BGCOLOR_CODE_TEXT = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
  
  public RegalComposite(Composite parent, int style){
    super(parent,style);
    this.setLayout(new GridLayout(1,false));
    // Create search component
    Composite cmpSearch = new Composite(this,SWT.None);
    cmpSearch.setLayout(new GridLayout(4,false));
    Label lbSearch = new Label(cmpSearch, SWT.None);
    lbSearch.setText("Hladaj:");
    txSearch = new Text(cmpSearch, SWT.BORDER);
    txSearch.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
        if (searchRegal() && searchForIndex == -1){
          searchForIndex = 0;
        }
      }
    });
    txSearch.addListener(SWT.CHANGED, new Listener() {
      @Override
      public void handleEvent(Event arg0) {
        searchForIndex = -1;
      }
    });
    GridData gdTxSearch = new GridData();
    gdTxSearch.widthHint = 200;
    txSearch.setLayoutData(gdTxSearch);
    Button btnBack = new Button(cmpSearch, SWT.FLAT);
    btnBack.setText("<");
    btnBack.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        searchForIndex--;
        if (searchForIndex < 0){
          searchForIndex = 0;
        }
        searchRegal();
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    Button btnForward = new Button(cmpSearch, SWT.FLAT);
    btnForward.setText(">");
    btnForward.addSelectionListener(new SelectionListener() {
      @Override
      public void widgetSelected(SelectionEvent arg0) {
        searchForIndex++;
        if (!searchRegal()){
          searchForIndex--;
          searchRegal();
        }
      }
      @Override
      public void widgetDefaultSelected(SelectionEvent arg0) {
      }
    });
    // Create regal component
    Composite cmpRegal = new Composite(this,SWT.BORDER);
    cmpRegal.setLayout(new GridLayout(WineMonitoring.NUM_COLUMNS_OF_REGAL * 2,false));
    wineTexts = new LinkedList<Text>();
    amountTexts = new LinkedList<Text>();
    WineMouseListener wineMouseListener = new WineMouseListener();
    AmountMouseListener amountMousetListener = new AmountMouseListener();
    WineFocusListener wineFocusListener = new WineFocusListener();
    AmountFocusListener amountFocusListener = new AmountFocusListener();
    for (int row = 0; row < WineMonitoring.NUM_ROWS_OF_REGAL ; row++){
      for (int column = 0; column < WineMonitoring.NUM_COLUMNS_OF_REGAL ; column++){
        Label lbRegal = new Label (cmpRegal, SWT.CENTER);
        GridData gdLbRegal = new GridData(GridData.FILL_HORIZONTAL);
        gdLbRegal.horizontalSpan = 2;
        lbRegal.setLayoutData(gdLbRegal);
        lbRegal.setText((char)((int)'A' + column) + String.valueOf(row + 1));
        lbRegal.setBackground(RegalComposite.BGCOLOR_REGAL_LABEL);
        lbRegal.setForeground(RegalComposite.FGCOLOR_REGAL_LABEL);
      }
      for (int column = 0; column < WineMonitoring.NUM_COLUMNS_OF_REGAL ; column++){
        Text txWine = new Text (cmpRegal, SWT.READ_ONLY | SWT.BORDER);
        wineTexts.add(txWine);
        GridData gdtxWine = new GridData();
        gdtxWine.widthHint = 60;
        txWine.setLayoutData(gdtxWine);
        txWine.setData(RegalComposite.POSX_DATA,column);
        txWine.setData(RegalComposite.POSY_DATA,row);
        txWine.setData(RegalComposite.TYPE_DATA,RegalTextType.WINE);
        txWine.addMouseListener(wineMouseListener);
        txWine.addFocusListener(wineFocusListener);
        txWine.setBackground(RegalComposite.BGCOLOR_CODE_TEXT);
        Text txAmount = new Text (cmpRegal, SWT.READ_ONLY | SWT.BORDER | SWT.RIGHT);
        amountTexts.add(txAmount);
        GridData gdtxAmount = new GridData();
        gdtxAmount.widthHint = 40;
        txAmount.setLayoutData(gdtxAmount);
        txWine.setData(RegalComposite.SIBLING_DATA,txAmount);
        txAmount.setData(RegalComposite.SIBLING_DATA,txWine);
        txAmount.setData(RegalComposite.POSX_DATA,column);
        txAmount.setData(RegalComposite.POSY_DATA,row);
        txAmount.setData(RegalComposite.TYPE_DATA,RegalTextType.AMOUNT);
        txAmount.addMouseListener(amountMousetListener);
        txAmount.addFocusListener(amountFocusListener);
        txAmount.setBackground(RegalComposite.BGCOLOR_AMOUNT_TEXT);
      }  
    }
    // Create detail component
    Composite cmpDetail = new Composite(this,SWT.None);
    cmpDetail.setLayout(new GridLayout(1,false));
    cmpDetail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    txDetail = new Text(cmpDetail, SWT.READ_ONLY | SWT.BORDER);
    txDetail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    txDetail.setBackground(RegalComposite.BGCOLOR_READ_ONLY_TEXT);
  }
  
  public void initialize (WineTableModel wineTableModel){
    this.wineTableModel = wineTableModel;
    List<Wine> wines = wineTableModel.getData();
    for (Wine wine : wines){
      if (wine.getRegals() != null && wine.getRegals().size() > 0){
        for (Regal regal : wine.getRegals()){
          Iterator<Text> itText = wineTexts.iterator();
          Text txWineCode = null;
          while (itText.hasNext() && txWineCode == null){
            Text text = itText.next();
            int posX = (Integer)text.getData(RegalComposite.POSX_DATA);
            int posY = (Integer)text.getData(RegalComposite.POSY_DATA);
            if (regal.getxPos() == posX && regal.getyPos() == posY){
              txWineCode = text;
            }
          }
          Iterator<Text> itAmount = amountTexts.iterator();
          Text txAmount = null;
          while (itAmount.hasNext() && txAmount == null){
            Text text = itAmount.next();
            int posX = (Integer)text.getData(RegalComposite.POSX_DATA);
            int posY = (Integer)text.getData(RegalComposite.POSY_DATA);
            if (regal.getxPos() == posX && regal.getyPos() == posY){
              txAmount = text;
            }
          }
          txAmount.setData(RegalComposite.WINE_DATA,wine);
          txWineCode.setData(RegalComposite.WINE_DATA,wine);
          txAmount.setText(WineMonitoring.DECIMAL_FORMAT.format(regal.getAmount()));
          txWineCode.setText(wine.getCode());
          RegalComposite.setTooltip(txAmount);
          RegalComposite.setTooltip(txWineCode);
        }
      }
    }
  }
  
  private void wineTextFocusGained (Text text){
    if (text.getData(RegalComposite.WINE_DATA) != null){
      txDetail.setText(WineTableModel.getWineDesc((Wine)text.getData(RegalComposite.WINE_DATA)));
    }
    else{
      txDetail.setText("");
    }
  }
  
  private void amountTextFocusGained (Text text){
    if (text.getData(RegalComposite.WINE_DATA) != null){
      txDetail.setText(WineTableModel.getWineDesc((Wine)text.getData(RegalComposite.WINE_DATA)));
    }
    else{
      txDetail.setText("");
    }
  }
  
  private class WineMouseListener implements MouseListener {
    @Override
    public void mouseUp(MouseEvent arg0) {
    }
    @Override
    public void mouseDown(MouseEvent arg0) {
    }
    @Override
    public void mouseDoubleClick(MouseEvent arg0) {
      new RegalEditShell(wineTableModel, (Text)arg0.widget)
        .open();            
    }
  }
  private class WineFocusListener implements FocusListener {
    @Override
    public void focusLost(FocusEvent arg0) {
      txDetail.setText("");
    }
    @Override
    public void focusGained(FocusEvent arg0) {
      wineTextFocusGained ((Text)arg0.getSource());
    }
  }
  private class AmountMouseListener implements MouseListener {
    @Override
    public void mouseUp(MouseEvent arg0) {
    }
    @Override
    public void mouseDown(MouseEvent arg0) {
    }
    @Override
    public void mouseDoubleClick(MouseEvent arg0) {
      Text txAmount = (Text)arg0.widget;
      new RegalEditShell(wineTableModel, (Text)txAmount.getData(RegalComposite.SIBLING_DATA))
        .open();            
    }
  }
  private class AmountFocusListener implements FocusListener {
    @Override
    public void focusLost(FocusEvent arg0) {
      txDetail.setText("");
    }
    @Override
    public void focusGained(FocusEvent arg0) {
      amountTextFocusGained ((Text)arg0.getSource());
    }
  }
  
  public static void setTooltip (Text text){
    Wine wine = null;
    Object object = text.getData(RegalComposite.WINE_DATA);
    String tooltip = "";
    if (object != null){
      wine = (Wine)object;
      tooltip = WineTableModel.getWineDesc(wine);
    }
    text.setToolTipText(tooltip);
  }
  
  private boolean searchRegal () {
    int ocurrenceIndex = 0;
    Iterator<Text> itTextWine = wineTexts.iterator();
    Text txWine = null;
    final String searchForText = txSearch.getText().toLowerCase();
    if (searchForText != null && searchForText.length() > 0){
      while (itTextWine.hasNext() && txWine == null){
        Text currWine = itTextWine.next();
        Object object = currWine.getData(RegalComposite.WINE_DATA);
        if (object != null){
          Wine wine = (Wine)object;
          if ((wine.getCode() != null && wine.getCode().toLowerCase().indexOf(searchForText) > -1) ||
              (wine.getFullName() != null && wine.getFullName().toLowerCase().indexOf(searchForText) > -1) ||
              (wine.getProducer() != null && wine.getProducer().toLowerCase().indexOf(searchForText) > -1)){
            if (ocurrenceIndex >= searchForIndex){
              txWine = currWine;
              txWine.setFocus();
              txWine.selectAll();
            }
            else{
              ocurrenceIndex++;
            }
          }
        }
      }
    }
    
    return txWine != null;
  }
  
}
