package sk.nanatoni.wine;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class DoubleVerifyListener implements VerifyListener{
  private static final String ALLOWED_CHARS = "0123456789" + WineMonitoring.DECIMAL_SEPARATOR;
  public void verifyText(VerifyEvent e) {
    if (e.text.length() > 1){
      try{
        Double.parseDouble(e.text);
        e.doit = true;
      } catch (NumberFormatException nfe){
        e.doit = false;
      }
    }
    else{
      e.doit = DoubleVerifyListener.ALLOWED_CHARS.indexOf(e.text) >= 0 ;  
    }
  }
}
