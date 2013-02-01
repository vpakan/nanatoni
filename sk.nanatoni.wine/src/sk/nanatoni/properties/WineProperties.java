package sk.nanatoni.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import sk.nanatoni.wine.WineMonitoring;

public class WineProperties {
  
  private Properties properties = new Properties();
  private static final String PROPERTIES_FILE_NAME = "wine.properties";
  public static final String NUM_ROWS_PROPERTY = "number.of.rows";
  public static final String NUM_COLUMNS_PROPERTY = "number.of.columns";
  
  public void loadProperties (){
    try {
      properties.load(new FileInputStream(WineMonitoring.PROPERTIES_LOCATION + File.separator + PROPERTIES_FILE_NAME));
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
  }
  
  public void saveProperties(){
    try {
      properties.store(new FileOutputStream(WineMonitoring.PROPERTIES_LOCATION + File.separator + PROPERTIES_FILE_NAME),
          "Properties file for Monitoring Production");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public String getProperty(String propertyName){
    return properties.getProperty (propertyName,"<null>");
  }
  
  public void setProperty(String key , String value){
    properties.setProperty (key,value);
  }

}
