package sk.nanatoni.wine;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import sk.nanatoni.properties.WineProperties;
import sk.nanatoni.wine.evidence.WineTable;
import sk.nanatoni.wine.evidence.WineTableModel;
import sk.nanatoni.wine.regal.RegalComposite;

public class WineMonitoring {
  public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
  public static final SimpleDateFormat STORAGE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy'T'HH:mm:ss.S");
  public static final GregorianCalendar GC = new GregorianCalendar();
  public static final int BUTTONS_MIN_WIDTH = 80;
  public static final char DECIMAL_SEPARATOR = new DecimalFormatSymbols().getDecimalSeparator();
  public static int NUM_ROWS_OF_REGAL = 10;
  public static int NUM_COLUMNS_OF_REGAL = 10;
  public static String DATA_LOCATION = "/home/vpakan/tmp";
  public static String PROPERTIES_LOCATION = null;
  public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
  
  private RegalComposite regalComposite = null;
  private WineTable wineTable;
  private WineTableModel wineTableModel;
  private WineProperties wineProperties;
  
  public WineProperties getMonitoringProductionProperties() {
    return wineProperties;
  }

  static {
    WineMonitoring.DECIMAL_FORMAT.setMinimumFractionDigits(2);
    WineMonitoring.DECIMAL_FORMAT.setMaximumFractionDigits(2);
    WineMonitoring.DECIMAL_FORMAT.setGroupingUsed(false);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    initializeDataLocation();
    new WineMonitoring().run();
  }

  private void run(){
    initializeProperty();
    Shell shell = new Shell();
    shell.setText("Evidencia vina");

    // Set layout for shell
    FillLayout layout = new FillLayout();
    shell.setLayout(layout);
    final ScrolledComposite sc = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.H_SCROLL);
    final Composite composite = new Composite(sc, SWT.BORDER);
    sc.setContent(composite);
    composite.setLayout(new FillLayout());
    createContent(composite);
    initialize();
    sc.setExpandVertical(true);
    sc.setExpandHorizontal(true);
    sc.addControlListener(new ControlAdapter() {
      public void controlResized(ControlEvent e) {
        sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        WineMonitoring.setScrollbarIncrement(sc); 
      }
    });
    shell.addDisposeListener(new DisposeListener() {

      public void widgetDisposed(DisposeEvent e) {
        dispose();     
      }
      
    });
    // Ask the shell to display its content
    shell.open();
    Display display = shell.getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }
  
  private void dispose(){
  }
  
  private void createContent(Composite parent){
    parent.setLayout(new FillLayout());
    final TabFolder tabFolder = new TabFolder (parent, SWT.BORDER);
    TabItem regals = new TabItem (tabFolder, SWT.NONE);
    regals.setText ("Rozlozenie v regaloch");
    regalComposite = new RegalComposite(tabFolder, SWT.NONE);
    regals.setControl(regalComposite);
    TabItem wines = new TabItem (tabFolder, SWT.NONE);
    wines.setText ("Zoznam vin");
    wineTable = new WineTable(tabFolder, SWT.NONE);
    wines.setControl(wineTable);
  }
  
  private void initialize(){
    // Initialize Wines & Regals Data
    wineTableModel = new WineTableModel(wineTable);
    regalComposite.initialize(wineTableModel);
  }
  
  private void initializeProperty(){
    wineProperties = new WineProperties();
    wineProperties.loadProperties();
    int numProperty = 0;
    try{
      numProperty = Integer.parseInt(wineProperties.getProperty(WineProperties.NUM_COLUMNS_PROPERTY));
    } catch (NumberFormatException nfe){
      numProperty = 0;
    }
    if (numProperty != 0){
      WineMonitoring.NUM_COLUMNS_OF_REGAL = numProperty; 
    }
    numProperty = 0;
    try{
      numProperty = Integer.parseInt(wineProperties.getProperty(WineProperties.NUM_ROWS_PROPERTY));  
    } catch (NumberFormatException nfe){
      numProperty = 0;
    }
    if (numProperty != 0){
      WineMonitoring.NUM_ROWS_OF_REGAL = numProperty; 
    }    
  }
  
  public static int sortString(String input1 , String input2){
    if (input1 == null && input2 == null){
      return 0;
    } else if (input1 == null){
      return 1;
    } else if (input2 == null){
      return -1;
    } else{
      return input1.compareTo(input2);      
    }

  }
  
  public static int sortDate(Date input1 , Date input2){
    int result = 0;
    if (input1 == null && input2 == null){
      result =  0;
    } else if (input1 == null){
      result =  1;
    } else if (input2 == null){
      result =  -1;
    } else{
      if (input1.before(input2)) {
        result = -1;
      } else if (input1.after(input2)) {
        result = 1;
      }
    }
    return result;
  }
  
  public static int sortInt(int input1 , int input2){
    int result = input1 - input2;
    result = result < 0 ? -1 : (result > 0) ? 1 : 0;  
    return result;
  }
  
  public static int sortDouble(double input1 , double input2){
    double result = input1 - input2;
    return result < 0 ? -1 : (result > 0) ? 1 : 0;
  }
  public static Date getFirstDayOfMonth(Date date){
    GC.setTime(date);
    GC.set(Calendar.MILLISECOND, 0);
    GC.set(Calendar.SECOND, 0);
    GC.set(Calendar.MINUTE, 0);
    GC.set(Calendar.HOUR_OF_DAY, 0);
    GC.set(Calendar.DAY_OF_MONTH, 1);
    
    return GC.getTime();
    
  }
  
  public static Date getLastDayOfMonth(Date date){
    GC.setTime(date);
    GC.set(Calendar.MILLISECOND, 0);
    GC.set(Calendar.SECOND, 0);
    GC.set(Calendar.MINUTE, 0);
    GC.set(Calendar.HOUR_OF_DAY, 0);
    GC.set(Calendar.DAY_OF_MONTH, 1);
    GC.add(Calendar.MONTH, 1);
    GC.add(Calendar.MILLISECOND, -1);
    
    return GC.getTime();
    
  }
  
  public static Date resetTimeOfDate(Date date){
    
    Date result = null;
    
    if (date != null){
      GC.setTime(date);
      GC.set(Calendar.MILLISECOND, 0);
      GC.set(Calendar.SECOND, 0);
      GC.set(Calendar.MINUTE, 0);
      GC.set(Calendar.HOUR_OF_DAY, 0);
      result = GC.getTime();
    }
        
    return result;
    
  }
  
  private static void initializeDataLocation(){
    DATA_LOCATION = new File(WineMonitoring.class
        .getProtectionDomain()
        .getCodeSource()
        .getLocation()
        .getPath())
      .getParent();
    // Add data subdirectory
    PROPERTIES_LOCATION = DATA_LOCATION + File.separator + "properties";
    DATA_LOCATION = DATA_LOCATION + File.separator + "data";
    File dataDir = new File(DATA_LOCATION);
    if (!dataDir.exists()){
      dataDir.mkdir();
    }
    File propsDir = new File(PROPERTIES_LOCATION);
    if (!propsDir.exists()){
      propsDir.mkdir();
    }
  }
  
  public static boolean isDateWithinPeriod (Date date, Date fromDate, Date toDate){
    return date == null
      ||((fromDate == null
          || fromDate.equals(date)
          || fromDate.before(date))
        && (toDate == null
            || toDate.equals(date)
            || toDate.after(date)));

  }
  
  public static void setScrollbarIncrement(final ScrolledComposite scrolledComposite) {
    ScrollBar sbX = scrolledComposite.getHorizontalBar();
    if ( sbX != null ) {
        sbX.setPageIncrement( Math.round(scrolledComposite.getSize().x) );
        sbX.setIncrement( Math.max( 1, scrolledComposite.getSize().x / 5 ) );
    }

    ScrollBar sbY = scrolledComposite.getVerticalBar();
    if ( sbY != null ) {
        sbY.setPageIncrement( Math.round(scrolledComposite.getSize().y) );
        sbY.setIncrement( Math.max( 1, scrolledComposite.getSize().y / 5 ) );
    }
} 
  
}
