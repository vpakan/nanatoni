package sk.nanatoni.wine;

import org.eclipse.swt.widgets.Shell;

public class MessageBox {
  public static void displayMessageBox (Shell parent, int style,String text, String message){
    org.eclipse.swt.widgets.MessageBox messageBox = new org.eclipse.swt.widgets.MessageBox(parent,style);
    messageBox.setText(text);
    messageBox.setMessage(message);
    messageBox.open();
  }
}
