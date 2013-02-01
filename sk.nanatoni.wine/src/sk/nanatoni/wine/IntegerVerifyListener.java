package sk.nanatoni.wine;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

public class IntegerVerifyListener implements VerifyListener{
  public void verifyText(VerifyEvent e) {
    if (e.text.length() > 1){
      try{
        Integer.parseInt(e.text);
        e.doit = true;
      } catch (NumberFormatException nfe){
        e.doit = false;
      }
    }
    else{
      e.doit = "0123456789".indexOf(e.text) >= 0 ;  
    }
  }
}
