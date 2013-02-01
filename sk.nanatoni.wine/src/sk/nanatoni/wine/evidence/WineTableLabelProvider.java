package sk.nanatoni.wine.evidence;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import sk.nanatoni.wine.WineMonitoring;
import sk.nanatoni.wine.entity.Wine;
/**
 * Label provider for the TableViewerExample
 * 
 * @see org.eclipse.jface.viewers.LabelProvider
 */
public class WineTableLabelProvider extends LabelProvider implements ITableLabelProvider{
  // Set the table column property names
  private static final String CODE_COLUMN     = "Kod vina";
  private static final String FULL_NAME_COLUMN   = "Nazov";
  private static final String PRODUCER_COLUMN   = "Vyrobca";
  private static final String YEAR_COLUMN = "Rocnik";
  private static final String BUYING_PRICE_COLUMN = "Nakupna cena";
  private static final String SELLING_PRICE_COLUMN = "Predajna cena";
  private static final String MIN_AMOUNT_COLUMN = "Min. mnozstvo";
  private static final String CURRENT_AMOUNT_COLUMN = "Akt. mnozstvo";
  private static final String COMMENT_COLUMN = "Poznamka";
  protected static final int CODE_COLUMN_ID = 0;
  protected static final int FULL_NAME_COLUMN_ID = 1;
  protected static final int PRODUCER_COLUMN_ID = 2;
  protected static final int YEAR_COLUMN_ID = 3;
  protected static final int BUYING_PRICE_COLUMN_ID = 4;
  protected static final int SELLING_PRICE_COLUMN_ID = 5;
  protected static final int MIN_AMOUNT_COLUMN_ID = 6;
  protected static final int CURRENT_AMOUNT_COLUMN_ID = 7;
  protected static final int COMMENT_COLUMN_ID = 8;
  private static Image warningImage = null;
  // Set column names
  public static final String[] COLUMN_NAMES = new String[] { 
    CODE_COLUMN,
    FULL_NAME_COLUMN,
    PRODUCER_COLUMN,
    YEAR_COLUMN,
    BUYING_PRICE_COLUMN,
    SELLING_PRICE_COLUMN,
    MIN_AMOUNT_COLUMN,
    CURRENT_AMOUNT_COLUMN,
    COMMENT_COLUMN};
  private static final List<String> COLUMN_NAMES_LIST = Arrays.asList(
    WineTableLabelProvider.COLUMN_NAMES);
  @SuppressWarnings("unused")
  private WineTableModel orderTableModel = null;
  
  public WineTableLabelProvider(WineTableModel orderTableModel){
    super();
    this.orderTableModel = orderTableModel;
  }
  /**
   * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
   *      int)
   */
  public String getColumnText(Object element, int columnIndex) {
    String result = "";
    Wine wine = (Wine) element;
    switch (columnIndex) {
    case CODE_COLUMN_ID:
      result = wine.getCode() == null ? "?" : wine.getCode();
      break;
    case FULL_NAME_COLUMN_ID:
      result = wine.getFullName() == null ? "?" : wine.getFullName();
      break;
    case PRODUCER_COLUMN_ID:
      result = wine.getProducer() == null ? "?" : wine.getProducer();
      break;
    case YEAR_COLUMN_ID:
      result = String.valueOf(wine.getYear());
      break;
    case BUYING_PRICE_COLUMN_ID:
      result = WineMonitoring.DECIMAL_FORMAT.format(wine.getBuyingPrice());
      break;
    case SELLING_PRICE_COLUMN_ID:
      result = WineMonitoring.DECIMAL_FORMAT.format(wine.getSellingPrice());
      break;
    case MIN_AMOUNT_COLUMN_ID:
      result = WineMonitoring.DECIMAL_FORMAT.format(wine.getMinAmount());
      break;
    case CURRENT_AMOUNT_COLUMN_ID:
      result = WineMonitoring.DECIMAL_FORMAT.format(wine.getCurrentAmount());
      break;
    case COMMENT_COLUMN_ID:
      result = wine.getComment() == null ? "?" : wine.getComment();
      break;      
    default:
      break;
    }
    return result;
  }

  /**
   * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
   *      int)
   */
  public Image getColumnImage(Object element, int columnIndex) {
    if (columnIndex == WineTableLabelProvider.CURRENT_AMOUNT_COLUMN_ID){
      Wine wine = (Wine)element;
      if (wine.getCurrentAmount() < wine.getMinAmount()){
        return getWarningImage();
      }
      else {
        return null;
      }
    }
    else {
      return null;
    }      
  }
  /**
   * Return the column names in a collection
   * 
   * @return List  containing column names
   */
  public static List<String> getColumnNames() {
    return WineTableLabelProvider.COLUMN_NAMES_LIST;
  }
  
  private static Image getWarningImage(){
    
    if (warningImage == null || warningImage.isDisposed()){
      warningImage = new Image(Display.getCurrent(), 
          WineTableLabelProvider.class.getResourceAsStream("/img/warning.png"));      
    }
    
    return warningImage;
    
  }
}
