package sk.nanatoni.wine.evidence;

import java.text.ParseException;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import sk.nanatoni.wine.entity.Wine;
import sk.nanatoni.wine.WineMonitoring;

/**
 * This class implements an ICellModifier
 * An ICellModifier is called when the user modifies a cell in the 
 * tableViewer
 */

public class WineTableCellModifier implements ICellModifier {
	private WineTableModel orderTableModel;
	/**
	 * Constructor 
	 * @param ProductionMonitoringShell an instance of a TableViewerExample 
	 */
	public WineTableCellModifier(WineTableModel orderTableModel) {
		super();
		this.orderTableModel = orderTableModel;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
	  int columnIndex = WineTableLabelProvider.getColumnNames().indexOf(property);
	  Wine wine = (Wine) element;
    if (columnIndex == WineTableLabelProvider.CODE_COLUMN_ID){
      return wine.getCode().trim().equals("?");
    }
    else if (columnIndex == WineTableLabelProvider.CURRENT_AMOUNT_COLUMN_ID){
      return false;
    }
    else{
      return true;
    }  
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {

		int columnIndex = WineTableLabelProvider.getColumnNames().indexOf(property);

		Object result = null;
		Wine wine = (Wine) element;
		switch (columnIndex) {
			case WineTableLabelProvider.CODE_COLUMN_ID :
				result = wine.getCode();
				break;
			case WineTableLabelProvider.FULL_NAME_COLUMN_ID:
			  result = wine.getFullName();
				break;
			case WineTableLabelProvider.PRODUCER_COLUMN_ID:
        result = wine.getProducer();
        break;
			case WineTableLabelProvider.YEAR_COLUMN_ID: 
				result = wine.getYear() + "";
				break;
      case WineTableLabelProvider.BUYING_PRICE_COLUMN_ID: 
        result = wine.getBuyingPrice() + "";
        break;
      case WineTableLabelProvider.SELLING_PRICE_COLUMN_ID: 
        result = wine.getSellingPrice() + "";
        break;
      case WineTableLabelProvider.MIN_AMOUNT_COLUMN_ID: 
        result = wine.getMinAmount() + "";
        break;
      case WineTableLabelProvider.COMMENT_COLUMN_ID:
        result = wine.getComment() + "";
        break;
			default :
				result = "";
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	
		// Find the index of the column 
    int columnIndex = WineTableLabelProvider.getColumnNames().indexOf(property);
		TableItem item = (TableItem) element;
		Wine order = (Wine)item.getData();
		String valueString;
    switch (columnIndex) {
    case WineTableLabelProvider.CODE_COLUMN_ID:
      if (value == null){
        value = "?";
      }
      valueString = ((String)value).trim();
      order.setCode(valueString);
      break;
    case WineTableLabelProvider.FULL_NAME_COLUMN_ID:
      if (value == null){
        value = "?";
      }
      valueString = ((String)value).trim();
      order.setFullName(valueString);
      break;
    case WineTableLabelProvider.PRODUCER_COLUMN_ID:
      if (value == null){
        value = "?";
      }
      valueString = ((String)value).trim();
      order.setProducer(valueString);
      break;
    case WineTableLabelProvider.YEAR_COLUMN_ID:
      valueString = ((String)value).trim();
      try {
        order.setYear(Integer.parseInt(valueString));
      } catch (NumberFormatException e) {
        order.setYear(-1);
      }
      break;
    case WineTableLabelProvider.BUYING_PRICE_COLUMN_ID:
      valueString = ((String) value).trim();
      if (valueString.length() == 0){
        valueString = "-1";
      }  
      order.setBuyingPrice(WineTableCellModifier.parseFormattedDouble(valueString));
      break;
    case WineTableLabelProvider.SELLING_PRICE_COLUMN_ID:
      valueString = ((String) value).trim();
      if (valueString.length() == 0){
        valueString = "0";
      }  
      order.setSellingPrice(WineTableCellModifier.parseFormattedDouble(valueString));
      break;
    case WineTableLabelProvider.MIN_AMOUNT_COLUMN_ID:
      valueString = ((String) value).trim();
      if (valueString.length() == 0){
        valueString = "0";
      }  
      order.setMinAmount(WineTableCellModifier.parseFormattedDouble(valueString));
      break;
    case WineTableLabelProvider.COMMENT_COLUMN_ID:
      valueString = ((String) value).trim();
      order.setComment(valueString);
      break;
    default :
    }
    orderTableModel.wineChanged (order);
	}
	
	private static double parseFormattedDouble (String doubleString){
	  try {
      return WineMonitoring.DECIMAL_FORMAT.parse(doubleString).doubleValue();
    } catch (ParseException pe) {
      throw new RuntimeException(pe);
    }
	}
}
