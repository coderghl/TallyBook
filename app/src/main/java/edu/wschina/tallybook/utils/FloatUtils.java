package edu.wschina.tallybook.utils;

import java.math.BigDecimal;

public class FloatUtils {
  // 计算小数，返回百分比
  public static float backPercentage(float v1, float v2) {
    BigDecimal bigDecimal = new BigDecimal(v1 / v2);
    float value = bigDecimal.setScale(4, 4).floatValue() * 100;
    BigDecimal bigDecimal1 = new BigDecimal(value);
    float result = bigDecimal1.setScale(2, 2).floatValue();
    return result;
  }
}
