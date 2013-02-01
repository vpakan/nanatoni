package sk.nanatoni.wine.evidence;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

import sk.nanatoni.wine.WineMonitoring;
import sk.nanatoni.wine.entity.Wine;


/**
 * Sorter for the TableViewerExample that displays items of type 
 * <code>ExampleTask</code>.
 * The sorter supports three sort criteria:
 * <p>
 * <code>DESCRIPTION</code>: Task description (String)
 * </p>
 * <p>
 * <code>OWNER</code>: Task Owner (String)
 * </p>
 * <p>
 * <code>PERCENT_COMPLETE</code>: Task percent completed (int).
 * </p>
 */
public class WineTableSorter extends ViewerSorter {
	// Criteria that the instance uses 
	private int criteria = 0;
	private int order = 1;
	@SuppressWarnings("unused")
	private WineTableModel wineTableModel = null;
	public WineTableSorter (WineTableModel wineTableModel,int criteria, int order){
	  this.order = order;
	  this.criteria = criteria;
	  this.wineTableModel = wineTableModel;
	}

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or 
	 *   <code>TYPE</code>
	 */

  public int compare(Viewer viewer, Object o1, Object o2) {

    Wine wine1 = (Wine) o1;
    Wine wine2 = (Wine) o2;
  
    int result = 0;
    switch (criteria) {
    case WineTableLabelProvider.CODE_COLUMN_ID:
      result = WineMonitoring.sortString(wine1.getCode(),
          wine2.getCode());
      break;
    case WineTableLabelProvider.FULL_NAME_COLUMN_ID:
      result = WineMonitoring.sortString(wine1.getFullName(),
          wine2.getFullName());
      break;
    case WineTableLabelProvider.PRODUCER_COLUMN_ID:
      result = WineMonitoring.sortString(wine1.getProducer(),
          wine2.getProducer());
      break;
    case WineTableLabelProvider.YEAR_COLUMN_ID:
      result = WineMonitoring.sortInt(wine1.getYear(), wine2
          .getYear());
      break;
    case WineTableLabelProvider.BUYING_PRICE_COLUMN_ID:
      result = WineMonitoring.sortDouble(wine1.getBuyingPrice(),
          wine2.getBuyingPrice());
      break;
    case WineTableLabelProvider.SELLING_PRICE_COLUMN_ID:
      result = WineMonitoring.sortDouble(wine1.getSellingPrice(),
          wine2.getSellingPrice());
      break;
    case WineTableLabelProvider.MIN_AMOUNT_COLUMN_ID:
      result = WineMonitoring.sortDouble(wine1.getMinAmount(),
          wine2.getMinAmount());
      break;
    case WineTableLabelProvider.CURRENT_AMOUNT_COLUMN_ID:
      result = WineMonitoring.sortDouble(wine1.getCurrentAmount(),
          wine2.getCurrentAmount());
      break;
    case WineTableLabelProvider.COMMENT_COLUMN_ID:
      result = WineMonitoring.sortString(wine1.getComment(),
          wine2.getComment());
      break;  
    default:
      break;
    }

    result = this.order * result;
    
    return result;
  }

	/**
	 * Returns the sort criteria of this this sorter.
	 *
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}
