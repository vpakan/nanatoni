package sk.nanatoni.wine.entity;

import java.util.Date;

public class Regal {
  private Wine wineEntity;
  private Date sysDate;
  private int xPos;
  private int yPos;
  private double amount;
  
  public Regal (Wine wineEntity){
    super();
    this.wineEntity = wineEntity;
  }
  
  public Wine getWineEntity() {
    return wineEntity;
  }
  public void setWineEntity(Wine wineEntity) {
    this.wineEntity = wineEntity;
  }
  public Date getSysDate() {
    return sysDate;
  }
  public void setSysDate(Date sysDate) {
    this.sysDate = sysDate;
  }
  public int getxPos() {
    return xPos;
  }
  public void setxPos(int xPos) {
    this.xPos = xPos;
  }
  public int getyPos() {
    return yPos;
  }
  public void setyPos(int yPos) {
    this.yPos = yPos;
  }
  public double getAmount() {
    return amount;
  }
  public void setAmount(double amount) {
    this.amount = amount;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(amount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((sysDate == null) ? 0 : sysDate.hashCode());
    result = prime * result
        + ((wineEntity == null) ? 0 : wineEntity.hashCode());
    result = prime * result + xPos;
    result = prime * result + yPos;
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Regal other = (Regal) obj;
    if (xPos != other.xPos)
      return false;
    if (yPos != other.yPos)
      return false;
    return true;
  }
  @Override
  public String toString() {
    return "Regal [wineEntity=" + wineEntity + ", sysDate=" + sysDate
        + ", xPos=" + xPos + ", yPos=" + yPos + ", amount=" + amount + "]";
  }
  
    
}
