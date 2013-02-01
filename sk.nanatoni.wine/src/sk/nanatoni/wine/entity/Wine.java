package sk.nanatoni.wine.entity;

import java.util.Date;
import java.util.List;
import java.lang.Comparable;

import sk.nanatoni.wine.WineMonitoring;

public class Wine implements Comparable<Wine> {
  private String code;
  private String fullName = "";
  private String producer = "";
  private int year;
  private double buyingPrice;
  private double sellingPrice;
  private double minAmount;
  private double currentAmount;
  private String comment = "";
  private Date sysDate;
  private List<Regal> regals;
  
  public Wine(String code) {
    super();
    this.code = code;
  }

  public List<Regal> getRegals() {
    return regals;
  }

  public void setRegals(List<Regal> regals) {
    this.regals = regals;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getProducer() {
    return producer;
  }

  public void setProducer(String producer) {
    this.producer = producer;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public double getBuyingPrice() {
    return buyingPrice;
  }

  public void setBuyingPrice(double buyingPrice) {
    this.buyingPrice = buyingPrice;
  }

  public double getSellingPrice() {
    return sellingPrice;
  }

  public void setSellingPrice(double sellingPrice) {
    this.sellingPrice = sellingPrice;
  }

  public double getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(double minAmount) {
    this.minAmount = minAmount;
  }

  public double getCurrentAmount() {
    return currentAmount;
  }

  public void setCurrentAmount(double currentAmount) {
    this.currentAmount = currentAmount;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getSysDate() {
    return sysDate;
  }

  public void setSysDate(Date sysDate) {
    this.sysDate = sysDate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(buyingPrice);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((code == null) ? 0 : code.hashCode());
    result = prime * result + ((comment == null) ? 0 : comment.hashCode());
    temp = Double.doubleToLongBits(currentAmount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
    temp = Double.doubleToLongBits(minAmount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((producer == null) ? 0 : producer.hashCode());
    temp = Double.doubleToLongBits(sellingPrice);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + year;
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
    Wine other = (Wine) obj;
    if (code == null) {
      if (other.code != null)
        return false;
    } else if (!code.equals(other.code))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuffer sbRegals = new StringBuffer("");
    for (Regal regal : regals){
      sbRegals.append("\n");
      sbRegals.append(regal);
    }
    return "Wine [code=" + code + ", fullName=" + fullName + ", producer="
        + producer + ", year=" + year + ", buyingPrice=" + buyingPrice
        + ", sellingPrice=" + sellingPrice + ", minAmount=" + minAmount
        + ", currentAmount=" + currentAmount + ", comment=" + comment + ", sysDate=" + sysDate + "]\nRegals:\n"
        + sbRegals.toString();
  }

  public void synchonizeFrom(Wine wine) {
    this.code = wine.getCode();
    this.fullName = wine.getFullName();
    this.producer = wine.getProducer();
    this.year = wine.getYear();
    this.buyingPrice = wine.getBuyingPrice();
    this.sellingPrice = wine.getSellingPrice();
    this.minAmount = wine.getMinAmount();
    this.currentAmount = wine.getCurrentAmount();
    this.comment = wine.getComment();
    this.sysDate = wine.getSysDate();
    this.regals = wine.getRegals();
  }

  public int compareTo(Wine order) {
    return WineMonitoring.sortString(this.getCode(),
        order.getCode());
  }
}
