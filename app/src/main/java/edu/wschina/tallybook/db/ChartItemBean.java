package edu.wschina.tallybook.db;

public class ChartItemBean {
  private int selectImageId;
  private String type;
  private float ratio; // 所占比例
  private  float totalMoney; // 总钱数


  public ChartItemBean(int selectImageId, String type, float ratio, float totalMoney) {
    this.selectImageId = selectImageId;
    this.type = type;
    this.ratio = ratio;
    this.totalMoney = totalMoney;
  }

  public ChartItemBean() {
  }

  public int getSelectImageId() {
    return selectImageId;
  }

  public void setSelectImageId(int selectImageId) {
    this.selectImageId = selectImageId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public float getRatio() {
    return ratio;
  }

  public void setRatio(float ratio) {
    this.ratio = ratio;
  }

  public float getTotalMoney() {
    return totalMoney;
  }

  public void setTotalMoney(float totalMoney) {
    this.totalMoney = totalMoney;
  }

  @Override
  public String toString() {
    return "ChartItemBean{" +
      "selectImageId=" + selectImageId +
      ", type='" + type + '\'' +
      ", ratio=" + ratio +
      ", totalMoney=" + totalMoney +
      '}';
  }
}
