package edu.wschina.tallybook.bean;

public class TypeBean {
  int id;
  private String typeName; // 类型名称
  private int imageId; // 未选中图片id
  private int selectImageId; // 选中图片id
  private int kind; // 收入 1 支出 0

  public TypeBean() {
  }

  public TypeBean(int id, String typeName, int imageId, int selectImageId, int kind) {
    this.id = id;
    this.typeName = typeName;
    this.imageId = imageId;
    this.selectImageId = selectImageId;
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

  public int getImageId() {
    return imageId;
  }

  public void setImageId(int imageId) {
    this.imageId = imageId;
  }

  public int getSelectImageId() {
    return selectImageId;
  }

  public void setSelectImageId(int selectImageId) {
    this.selectImageId = selectImageId;
  }

  public int getKind() {
    return kind;
  }

  public void setKind(int kind) {
    this.kind = kind;
  }
}
