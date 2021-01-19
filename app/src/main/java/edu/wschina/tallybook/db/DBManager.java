package edu.wschina.tallybook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.wschina.tallybook.bean.AccountBean;
import edu.wschina.tallybook.bean.BarChartItemBean;
import edu.wschina.tallybook.bean.TypeBean;
import edu.wschina.tallybook.utils.FloatUtils;

/**
 * 负责管理数据库的类
 * 主要对于表当中的内容进行操作
 */

public class DBManager {
  public static SQLiteDatabase sDb;

  // 初始化数据库对象
  public static void initDB(Context context) {
    DBOpenHelper helper = new DBOpenHelper(context);
    sDb = helper.getWritableDatabase();
  }

  public static List<TypeBean> getTypeList(int kind) {
    List<TypeBean> list = new ArrayList<>();

    // 读取type_tb当中的数据
    String sql = "select * from " + DBConstants.TYPE_TB + " where " + DBConstants.KIND + "=" + kind;
    Cursor cursor = sDb.rawQuery(sql, null);

    // 循环读取游标内容存储到对象中
    if (cursor.moveToFirst()) {
      do {
        String type_name = cursor.getString(cursor.getColumnIndex(DBConstants.TYPE_NAME));
        int image_id = cursor.getInt(cursor.getColumnIndex(DBConstants.IMAGE_ID));
        int select_image_id = cursor.getInt(cursor.getColumnIndex(DBConstants.SELECT_IMAGE_ID));
        int kind1 = cursor.getInt(cursor.getColumnIndex(DBConstants.KIND));
        int id = cursor.getInt(cursor.getColumnIndex("id"));

        TypeBean typeBean = new TypeBean(id, type_name, image_id, select_image_id, kind1);
        list.add(typeBean);
      } while (cursor.moveToNext());
    }

    return list;
  }

  // 向记账表当中插入一行元素
  public static void insertItemToAccountTb(AccountBean bean) {
    ContentValues values = new ContentValues();

    values.put(DBConstants.TYPE_NAME, bean.getTypeName());
    values.put(DBConstants.SELECT_IMAGE_ID, bean.getSelectImageId());
    values.put(DBConstants.KIND, bean.getKind());
    values.put(DBConstants.MONEY, bean.getMoney());
    values.put(DBConstants.TIME, bean.getTime());
    values.put(DBConstants.YEAR, bean.getYear());
    values.put(DBConstants.MONTH, bean.getMonth());
    values.put(DBConstants.DAY, bean.getDay());
    values.put(DBConstants.REMARK, bean.getRemark());

    sDb.insert(DBConstants.ACCOUNT_TB, null, values);
    values.clear();
  }

  // 获取记账表中某一天当中所有支出和收入
  public static List<AccountBean> getAccountListOneDayData(int year, int month, int day) {
    List<AccountBean> mData = new ArrayList<>();
    String sql = "select * from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + "=? and " + DBConstants.MONTH + "=? and " + DBConstants.DAY + "=? order by id desc";

    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", day + ""});
    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex(DBConstants.TYPE_NAME));
        String remark = cursor.getString(cursor.getColumnIndex(DBConstants.REMARK));
        String time = cursor.getString(cursor.getColumnIndex(DBConstants.TIME));
        int select_id = cursor.getInt(cursor.getColumnIndex(DBConstants.SELECT_IMAGE_ID));
        int kind = cursor.getInt(cursor.getColumnIndex(DBConstants.KIND));
        float mMoney = cursor.getFloat(cursor.getColumnIndex(DBConstants.MONEY));

        AccountBean accountBean = new AccountBean(id, name, select_id, remark, mMoney, time, year, day, month, kind);
        mData.add(accountBean);
      } while (cursor.moveToNext());
    }

    cursor.close();
    return mData;
  }

  // 获取记账表中某一月当中所有支出和收入
  public static List<AccountBean> getAccountListOneMonthData(int year, int month) {
    List<AccountBean> mData = new ArrayList<>();
    String sql = "select * from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + "=? and " + DBConstants.MONTH + "=?  order by id desc";

    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + ""});
    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex(DBConstants.TYPE_NAME));
        String remark = cursor.getString(cursor.getColumnIndex(DBConstants.REMARK));
        String time = cursor.getString(cursor.getColumnIndex(DBConstants.TIME));
        int select_id = cursor.getInt(cursor.getColumnIndex(DBConstants.SELECT_IMAGE_ID));
        int kind = cursor.getInt(cursor.getColumnIndex(DBConstants.KIND));
        int day = cursor.getInt(cursor.getColumnIndex(DBConstants.DAY));
        float mMoney = cursor.getFloat(cursor.getColumnIndex(DBConstants.MONEY));

        AccountBean accountBean = new AccountBean(id, name, select_id, remark, mMoney, time, year, day, month, kind);
        mData.add(accountBean);
      } while (cursor.moveToNext());
    }

    cursor.close();
    return mData;
  }

  // 获取某一天的支出或收入总金额 kind: 支出0 收入1
  public static float getSumMoneyOneDay(int year, int month, int day, int kind) {
    float total = 0.0f;

    String sql = "select sum(" + DBConstants.MONEY + ") from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + "=? and " + DBConstants.MONTH + "=? and " + DBConstants.DAY + "=? and " + DBConstants.KIND + "=?";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});

    // 遍历
    if (cursor.moveToFirst()) {
      float money = cursor.getFloat(cursor.getColumnIndex("sum(" + DBConstants.MONEY + ")"));
      total = money;
    }
    return total;
  }

  // 获取某一月的支出或收入总金额 kind: 支出0 收入1
  public static float getSumMoneyOneMonth(int year, int month, int kind) {
    float total = 0.0f;

    String sql = "select sum(" + DBConstants.MONEY + ") from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + "=? and " + DBConstants.MONTH + "=? and " + DBConstants.KIND + "=?";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", kind + ""});

    // 遍历
    if (cursor.moveToFirst()) {
      float money = cursor.getFloat(cursor.getColumnIndex("sum(" + DBConstants.MONEY + ")"));
      total = money;
    }
    return total;
  }

  // 获取某一年的支出或收入总金额 kind: 支出0 收入1
  public static float getSumMoneyOneYear(int year, int kind) {
    float total = 0.0f;

    String sql = "select sum(" + DBConstants.MONEY + ") from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + "=? and " + DBConstants.KIND + "=?";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", kind + ""});

    // 遍历
    if (cursor.moveToFirst()) {
      float money = cursor.getFloat(cursor.getColumnIndex("sum(" + DBConstants.MONEY + ")"));
      total = money;
    }
    return total;
  }

  // 根据传入的Id，删除DBConstants.ACCOUNT_TB中一条记录
  public static int deleteFromAccount(int id) {
    int deletePosition = sDb.delete(DBConstants.ACCOUNT_TB, "id=?", new String[]{id + ""});
    return deletePosition;
  }


  // 通过备注信息进行模糊查询，收入or支出
  public static List<AccountBean> searchInfoRemark(String remark) {
    List<AccountBean> mData = new ArrayList<>();
    String sql = "select * from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.REMARK + " like '%" + remark + "%'";
    Cursor cursor = sDb.rawQuery(sql, null);

    if (cursor.moveToFirst()) {
      do {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex(DBConstants.TYPE_NAME));
        String remark_target = cursor.getString(cursor.getColumnIndex(DBConstants.REMARK));
        String time = cursor.getString(cursor.getColumnIndex(DBConstants.TIME));
        int select_id = cursor.getInt(cursor.getColumnIndex(DBConstants.SELECT_IMAGE_ID));
        int kind = cursor.getInt(cursor.getColumnIndex(DBConstants.KIND));
        int year = cursor.getInt(cursor.getColumnIndex(DBConstants.YEAR));
        int month = cursor.getInt(cursor.getColumnIndex(DBConstants.MONTH));
        int day = cursor.getInt(cursor.getColumnIndex(DBConstants.DAY));
        float mMoney = cursor.getFloat(cursor.getColumnIndex(DBConstants.MONEY));

        AccountBean accountBean = new AccountBean(id, name, select_id, remark_target, mMoney, time, year, day, month, kind);
        mData.add(accountBean);
      } while (cursor.moveToNext());
    }

    cursor.close();
    return mData;
  }

  // 查询记账表当中有几个年份信息
  public static List<Integer> getYearListFromAccount() {
    List<Integer> data = new ArrayList<>();
    String sql = "select distinct(year) from " + DBConstants.ACCOUNT_TB + " order by year asc";

    Cursor cursor = sDb.rawQuery(sql, null);
    if (cursor.moveToFirst()) {
      do {
        int year = cursor.getInt(cursor.getColumnIndex(DBConstants.YEAR));
        data.add(year);
      } while (cursor.moveToNext());
    }

    return data;
  }

  // 删除记账表中所有记录
  public static void deleteAllAccount() {
    String sql = "delete from " + DBConstants.ACCOUNT_TB + "";
    sDb.execSQL(sql);
  }

  // 统计某月份收入支出有多少条 收入-1 支出0
  public static int getCountItemOneMonth(int kind, int year, int month) {
    int total = 0;
    String sql = "select count(" + DBConstants.MONEY + ")  from " + DBConstants.ACCOUNT_TB + " where " + DBConstants.YEAR + " =? and " + DBConstants.MONTH + "=? and " + DBConstants.KIND + " =?";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
    if (cursor.moveToFirst()) {
      total = cursor.getInt(cursor.getColumnIndex("count(" + DBConstants.MONEY + ")"));
    }
    return total;
  }

  // 查询指定年份和月份的收入或者支出某一种类型的总钱数
  public static List<ChartItemBean> getChartList(int year, int month, int kind) {
    List<ChartItemBean> data = new ArrayList<>();

    // 先求出支出or收入总钱数
    float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);

    String sql = "select " + DBConstants.TYPE_NAME + ", " + DBConstants.SELECT_IMAGE_ID + ", sum(" + DBConstants.MONEY + ") as total from " +
      "" + DBConstants.ACCOUNT_TB + " where year=? and month=? and kind=? group by typename order by total desc";

    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
    if (cursor.moveToFirst()) {
      do {
        int selectImageId = cursor.getInt(cursor.getColumnIndex(DBConstants.SELECT_IMAGE_ID));
        String typename = cursor.getString(cursor.getColumnIndex(DBConstants.TYPE_NAME));
        float total = cursor.getFloat(cursor.getColumnIndex("total"));
        float result = FloatUtils.backPercentage(total, sumMoneyOneMonth);
        ChartItemBean chartItemBean = new ChartItemBean(selectImageId, typename, result, total);
        data.add(chartItemBean);
      } while (cursor.moveToNext());
    }

    return data;
  }

  // 获取某月当中收入支出最高的一天
  public static float getMaxMoneyOnDayInMonth(int year, int month, int kind) {
    String sql = "select sum(money) from account where year=? and month=? and kind=? group by day order by sum(money) desc";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
    if (cursor.moveToFirst()) {
      float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
      return money;
    }
    return 0;
  }

  // 根据指定月份，获取每日收入或者支出总钱数集合
  public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind) {
    String sql = "select day,sum(money) from account where year=? and month=? and kind=? group by day";
    Cursor cursor = sDb.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
    List<BarChartItemBean>list = new ArrayList<>();
    while (cursor.moveToNext()) {
      int day = cursor.getInt(cursor.getColumnIndex("day"));
      float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
      BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
      list.add(itemBean);
    }
    return list;
  }
}
