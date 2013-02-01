package sk.nanatoni.wine.evidence;

import java.io.File;

import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import sk.nanatoni.wine.DoubleVerifyListener;
import sk.nanatoni.wine.EditorNavigationStrategy;
import sk.nanatoni.wine.IntegerVerifyListener;
import sk.nanatoni.wine.MessageBox;
import sk.nanatoni.wine.WineMonitoring;
import sk.nanatoni.wine.entity.Regal;
import sk.nanatoni.wine.entity.Wine;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class WineTableModel {
  private static final String CODE_NODE = "wineCode";
  private static final String FULL_NAME_NODE = "fullName";
  private static final String PRODUCER_NODE = "producer";
  private static final String YEAR_NODE = "year";
  private static final String BUYING_PRICE_NODE = "buyingPrice";
  private static final String SELLING_PRICE_NODE = "sellingPrice";
  private static final String SYS_DATE_NODE = "sysDate";
  private static final String MIN_AMMOUNT_NODE = "minimalAmount";
  private static final String COMMENT_NODE = "comment";
  private static final String REGAL_NODE = "regal";
  private static final String REGALS_NODE = "regals";
  private static final String REGAL_POS_X_NODE = "posX";
  private static final String REGAL_POS_Y_NODE = "posY";
  private static final String REGAL_SYS_DATE_NODE = "sysDate";
  private static final String REGAL_AMOUNT_NODE = "amount";
  private static final String WINE_NODE = "wine";
  
  private TableViewer tableViewer = null;
  private LinkedList<Wine> data = null;
  private final int[] sorting = new int[]{1,1,1,1,1,1,1,1,1};
  private Table table = null;
    
  public List<Wine> getData() {
    return data;
  }

  public TableViewer getTableViewer() {
    return tableViewer;
  }

  private WineTable orderTable = null;
  
  public WineTableModel(WineTable monitoringTable){
    this.orderTable = monitoringTable;
    initializeTableViewer();
    initializeButtons();
    tableViewer.setInput(data);
    for (int index = 0 ; index < table.getColumnCount() ; index++){
      table.getColumn(index).pack();
    }
    table.pack();
  }
  
  public LinkedList<Wine> loadData(){
    LinkedList<Wine> result = new LinkedList<Wine>();
    File dataDir = new File(WineMonitoring.DATA_LOCATION);
    FilenameFilter filenameFilter = new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.endsWith(".xml");
      }
    };
    for (File file : dataDir.listFiles(filenameFilter)){
      Wine wine = loadWine(file);
      if (wine != null){
        result.add(wine);
      }
    }
    Collections.sort(result);
    return result;
  }
  
  private void initializeTableViewer() {
    table = this.orderTable.getTable();
    tableViewer = new TableViewer(table);
    EditorNavigationStrategy.setUpTableViewerNavigationStrategy(tableViewer);     
    tableViewer.setColumnProperties(WineTableLabelProvider.COLUMN_NAMES);
    tableViewer.setContentProvider(new WineTableContentProvider(this));
    tableViewer.setLabelProvider(new WineTableLabelProvider(this));
    data = loadData();
    for (int index = 0; index < table.getColumnCount(); index++) {
      final int fIndex = index;
      final WineTableModel monitoringTableModel = this;
      table.getColumn(index).addSelectionListener(new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e) {
          tableViewer.setSorter(new WineTableSorter(monitoringTableModel,
              fIndex, sorting[fIndex]));
          sorting[fIndex] = sorting[fIndex] * (-1);
        }
      });
    }
    CellEditor[] editors = new CellEditor[WineTableLabelProvider.COLUMN_NAMES.length];
    // Order
    final TextCellEditor codeCellEditor = new TextCellEditor(table);
    codeCellEditor.addListener(new ICellEditorListener() {
      @Override
      public void editorValueChanged(boolean arg0, boolean arg1) {
        // do nothing        
      }
      @Override
      public void cancelEditor() {
        // do nothing
      }
      @Override
      public void applyEditorValue() {
        checkCode((Text)codeCellEditor.getControl());
      }
    });
    editors[WineTableLabelProvider.CODE_COLUMN_ID] = codeCellEditor;
    editors[WineTableLabelProvider.FULL_NAME_COLUMN_ID] = new TextCellEditor(table);
    editors[WineTableLabelProvider.PRODUCER_COLUMN_ID] = new TextCellEditor(table);
    TextCellEditor textEditor = new TextCellEditor(table);
    ((Text) textEditor.getControl())
        .addVerifyListener(new IntegerVerifyListener());
    editors[WineTableLabelProvider.YEAR_COLUMN_ID] = textEditor;
    textEditor = new TextCellEditor(table);
    ((Text) textEditor.getControl())
        .addVerifyListener(new DoubleVerifyListener());
    editors[WineTableLabelProvider.BUYING_PRICE_COLUMN_ID] = textEditor;
    textEditor = new TextCellEditor(table);
    ((Text) textEditor.getControl())
        .addVerifyListener(new DoubleVerifyListener());
    editors[WineTableLabelProvider.SELLING_PRICE_COLUMN_ID] = textEditor;
    textEditor = new TextCellEditor(table);
    ((Text) textEditor.getControl())
        .addVerifyListener(new DoubleVerifyListener());
    editors[WineTableLabelProvider.MIN_AMOUNT_COLUMN_ID] = textEditor;
    editors[WineTableLabelProvider.COMMENT_COLUMN_ID] = new TextCellEditor(table);
    tableViewer.setCellEditors(editors);
    tableViewer.setCellModifier(new WineTableCellModifier(this));
  }
  
  private void initializeButtons(){
    orderTable.getClose().addSelectionListener(new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ){
        e.display.dispose();
      }
    });
    orderTable.getAdd().addSelectionListener(new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ){
        addWine();
      }
    });
    orderTable.getDelete().addSelectionListener(new SelectionAdapter() {
      public void widgetSelected( SelectionEvent e ){
        deleteWine();
      }
    });
  }
  public void wineChanged(Wine wine) {
    wine.setSysDate(new Date());
    wine.setCurrentAmount(WineTableModel.getCurrentAmount(wine));
    tableViewer.update(wine, null);
    if (wine.getCode() != null && !wine.getCode().equals("?")){
      saveWine(wine);  
    }
  }
  private void addWine(){
    if (isAddPossible()){
      Wine wine = new Wine("?");
      tableViewer.add(wine);
      data.addLast(wine);
      tableViewer.setSelection(new StructuredSelection(wine));
      tableViewer.editElement(wine, WineTableLabelProvider.CODE_COLUMN_ID);
    }
    else{
      MessageBox.displayMessageBox(table.getShell(), SWT.ERROR, "Chyba !",
        "Nemozno pridat vino." +
        "\nExistuje vino ktora nema vyplnene kod" +
        "\nOpravte najskor chybne vino.");
    }
  }
  private void deleteWine(){
    Wine wine = (Wine) ((IStructuredSelection) 
        tableViewer.getSelection()).getFirstElement();
    if (wine != null) {
      if (wine.getRegals() == null || wine.getRegals().size() == 0){
        tableViewer.remove(wine);
        int index = data.indexOf(wine);
        data.remove(wine);
        new File(WineMonitoring.DATA_LOCATION + 
              File.separator + 
              (wine.getCode() == null || wine.getCode().equals("?") ? "unknown" : wine.getCode()) +
              ".xml").delete();
        if (data.size() == index){
          index--;
        }
        if (data.size() > index && index >= 0){
          tableViewer.setSelection(new StructuredSelection(data.get(index)));
          table.setFocus();
        }        
      }
      else{
        MessageBox.displayMessageBox(table.getShell(), SWT.ERROR, "Chyba !",
          "Vino kod: " + wine.getCode() + " nemoze byt zmazane." +
          "\nK tomuto vinu existuju zaznamy v regaloch." +
          "\nZmazte najskor zaznamy v regaloch patriace k tomuto vinu.");
      }
    }  
  }
  
  private void checkCode (Text codeText){
    String errorMessage = null;
    String code = codeText.getText();
    if (code == null || !code.equals("?")){
      if (code == null || code.length() == 0){
        errorMessage = "Kod vina nemoze byt prazdny!";
      } else if (data.contains(new Wine(code))){
        errorMessage = "Vino s kodom " + code + " uz existuje!";
      }
      
      if (errorMessage != null){
        codeText.setText("?");
        MessageBox.displayMessageBox(table.getShell(), SWT.ERROR, "Chyba !", 
          errorMessage);
      }
    }
  }

  private boolean isAddPossible(){
    boolean addPossible = true;
    if (data != null){
      Iterator<Wine> iterator = data.iterator();
      while (iterator.hasNext() && addPossible){
        Wine wine = iterator.next();
        if (wine.getCode().equals("?")){
          addPossible = false;
        }
      }
    }
    return addPossible;
  }
  
  private Wine loadWine(File wineFile){

    Wine result = null;
    
    String errorDescription = null;
    SAXReader reader = new SAXReader();
    Document doc;
    try {
      doc = reader.read(wineFile);
      doc.normalize();
      Element rootNode = doc.getRootElement();
      if (rootNode.getName().equals(WineTableModel.WINE_NODE)){
        String code = rootNode.element(WineTableModel.CODE_NODE).getText();
        String fullName = rootNode.element(WineTableModel.FULL_NAME_NODE).getText();
        String producer = rootNode.element(WineTableModel.PRODUCER_NODE).getText();
        int year = Integer.parseInt(rootNode.element(WineTableModel.YEAR_NODE).getText());
        double buyingPrice = Double.parseDouble(rootNode.element(WineTableModel.BUYING_PRICE_NODE).getText());
        double sellingPrice = Double.parseDouble(rootNode.element(WineTableModel.SELLING_PRICE_NODE).getText());
        double minAmount = Double.parseDouble(rootNode.element(WineTableModel.MIN_AMMOUNT_NODE).getText());
        String comment = rootNode.element(WineTableModel.COMMENT_NODE).getText();
        Date sysDate = WineMonitoring.STORAGE_DATE_FORMAT.parse(rootNode.element(WineTableModel.SYS_DATE_NODE).getText());
        result = new Wine(code);
        result.setFullName(fullName);
        result.setProducer(producer);
        result.setYear(year);
        result.setBuyingPrice(buyingPrice);
        result.setSysDate(sysDate);
        result.setSellingPrice(sellingPrice);
        result.setMinAmount(minAmount);
        result.setComment(comment);
        Element regalsNode =  rootNode.element(WineTableModel.REGALS_NODE);
        if (regalsNode != null){
          List regalElements = regalsNode.elements(WineTableModel.REGAL_NODE);
          LinkedList<Regal> regals = new LinkedList<Regal>();
          for (Object object : regalElements){
            Element regal = (Element)object;
            int xPos = Integer.parseInt(regal.element(WineTableModel.REGAL_POS_X_NODE).getText());
            int yPos = Integer.parseInt(regal.element(WineTableModel.REGAL_POS_Y_NODE).getText());
            double amount = Double.parseDouble(regal.element(WineTableModel.REGAL_AMOUNT_NODE).getText());
            Date regalSysDate = WineMonitoring.STORAGE_DATE_FORMAT.parse(regal.element(WineTableModel.REGAL_SYS_DATE_NODE).getText());
            Regal regalEntity = new Regal(result);
            regalEntity.setxPos(xPos);
            regalEntity.setyPos(yPos);
            regalEntity.setAmount(amount);
            regalEntity.setSysDate(regalSysDate);
            regals.addLast(regalEntity);
          }
          result.setRegals(regals);
        }
        result.setCurrentAmount(WineTableModel.getCurrentAmount(result));
      }
      else{
        errorDescription = "Hlavny element suboru musi byt " + WineTableModel.WINE_NODE;
      }
    } catch (DocumentException e) {
      errorDescription = e.toString();
    } catch (NullPointerException jlnpe){
      errorDescription = jlnpe.toString();
    } catch (ParseException pe){
      errorDescription = pe.toString();
    }

    if (errorDescription != null){
      errorDescription = "Subor " + wineFile.getAbsoluteFile() + " nebol uspesne nacitany.\n" +
        "Detaily chyby:\n" +
        errorDescription;
      MessageBox.displayMessageBox(table.getShell(), SWT.ERROR, "Chyba!",
         errorDescription);
      result = null;
    }
    return result;
  }
  
  public void saveWine(Wine wine){
    Document document = DocumentHelper.createDocument();
    Element wineElement = document.addElement(WineTableModel.WINE_NODE);
    wineElement.addElement(WineTableModel.CODE_NODE)
      .addText(wine.getCode());
    wineElement.addElement(WineTableModel.FULL_NAME_NODE)
      .addText(wine.getFullName());
    wineElement.addElement(WineTableModel.PRODUCER_NODE)
      .addText(wine.getProducer());
    wineElement.addElement(WineTableModel.SYS_DATE_NODE)
      .addText(WineMonitoring.STORAGE_DATE_FORMAT.format(wine.getSysDate()));
    wineElement.addElement(WineTableModel.BUYING_PRICE_NODE)
      .addText(String.valueOf(wine.getBuyingPrice()));
    wineElement.addElement(WineTableModel.SELLING_PRICE_NODE)
      .addText(String.valueOf(wine.getSellingPrice()));
    wineElement.addElement(WineTableModel.MIN_AMMOUNT_NODE)
      .addText(String.valueOf(wine.getMinAmount()));
    wineElement.addElement(WineTableModel.YEAR_NODE)
      .addText(String.valueOf(wine.getYear()));
    wineElement.addElement(WineTableModel.COMMENT_NODE)
    .addText(String.valueOf(wine.getComment()));
    List<Regal> regals = wine.getRegals();
    if (regals != null && regals.size() > 0){
      Element regalsElement = wineElement.addElement(WineTableModel.REGALS_NODE);
      for (Regal regal : regals){
        Element regalElement = regalsElement.addElement(WineTableModel.REGAL_NODE); 
        regalElement.addElement(WineTableModel.REGAL_SYS_DATE_NODE)
          .addText(WineMonitoring.STORAGE_DATE_FORMAT.format(regal.getSysDate()));
        regalElement.addElement(WineTableModel.REGAL_POS_X_NODE)
            .addText(String.valueOf(regal.getxPos()));
        regalElement.addElement(WineTableModel.REGAL_POS_Y_NODE)
          .addText(String.valueOf(regal.getyPos()));
        regalElement.addElement(WineTableModel.REGAL_AMOUNT_NODE)
            .addText(String.valueOf(regal.getAmount()));
      }
    }
    // Pretty print the document to System.out
    OutputFormat format = OutputFormat.createPrettyPrint();
    XMLWriter writer;
    try {
      writer = new XMLWriter( new FileWriter(WineMonitoring.DATA_LOCATION + 
          File.separator + 
          (wine.getCode() == null || wine.getCode().equals("?") ? "unknown" : wine.getCode()) +
          ".xml")
       , format );
      writer.write( document );
      writer.close();
    } catch (IOException e) {
      MessageBox.displayMessageBox(table.getShell(), SWT.ERROR, "Chyba !",
        "Chyba pri zapise dat!\n" +
        "Data neboli spravne ulozene!\n"+ e);
    }
  }
  
  public static double getCurrentAmount (Wine wine){
    double result = 0.0;
    if (wine.getRegals() != null){
      for (Regal regal : wine.getRegals()){
        result += regal.getAmount();
      }  
    }
    
    return result;
  }
  
  public static String getWineDesc (Wine wine){
    StringBuffer sb = new StringBuffer("");
    sb.append(wine.getCode());
    sb.append(" - ");
    sb.append(wine.getFullName() != null ? wine.getFullName() : "<null>");
    sb.append(" - ");
    sb.append(wine.getYear() != 0 ? wine.getYear() : "<null>");
    sb.append(" - ");
    sb.append(wine.getProducer() != null ? wine.getProducer() : "<null>");
    sb.append(" - ");
    sb.append(wine.getComment() != null ? wine.getComment() : "<null>");
    
    return sb.toString();
  }
}
