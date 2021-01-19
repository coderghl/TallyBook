package edu.wschina.tallybook.bean;

import androidx.annotation.NonNull;

/**
 * 用户一条数据的相关内容
 */
public class AccountBean {
  private int id;
  private String typeName; // 名字
  private int selectImageId; // 图片
  private String remark; // 备注
  private float money; // 钱
  private String time; // 事件字符串
  private int year, day, month; // 年月日
  private int kind; // 收入是1 支出是0

  public AccountBean() {
  }

  public AccountBean(int id, String typeName, int selectImageId, String remark, float money, String time, int year, int day, int month, int kind) {
    this.id = id;
    this.typeName = typeName;
    this.selectImageId = selectImageId;
    this.remark = remark;
    this.money = money;
    this.time = time;
    this.year = year;
    this.day = day;
    this.month = month;
    this.kind = kind;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public int getSelectImageId() {
    return selectImageId;
  }

  public void setSelectImageId(int selectImageId) {
    this.selectImageId = selectImageId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public float getMoney() {
    return money;
  }

  public void setMoney(float money) {
    this.money = money;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getKind() {
    return kind;
  }

  public void setKind(int kind) {
    this.kind = kind;
  }


  @Override
  public String toString() {
    return "AccountBean{" +
      "id=" + id +
      ", typeName='" + typeName + '\'' +
      ", selectImageId=" + selectImageId +
      ", remark='" + remark + '\'' +
      ", money=" + money +
      ", time='" + time + '\'' +
      ", year=" + year +
      ", day=" + day +
      ", month=" + month +
      ", kind=" + kind +
      '}';
  }
}
