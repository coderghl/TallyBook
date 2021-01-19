package edu.wschina.tallybook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.adapter.AccountListAdapter;
import edu.wschina.tallybook.bean.AccountBean;
import edu.wschina.tallybook.db.DBManager;
import edu.wschina.tallybook.utils.CalendarDialog;

public class HistoryActivity extends BaseActivity {
  private TextView history_tv_time;
  private ListView history_lv;

  private List<AccountBean> mData = new ArrayList<>();
  private AccountListAdapter mAdapter;

  private int year, month;
  private int dialogYearSelectPosition = -1;
  private int dialogMonthSelectPosition = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_history);
    initView();
    initTime();
    initEvent();
  }

  private void initEvent() {
    // 设置listView长按事件
    history_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AccountBean targetBean = mData.get(position);
        deleteItem(targetBean);
        return false;
      }
    });
  }

  private void deleteItem(AccountBean targetBean) {
    new AlertDialog.Builder(this)
      .setTitle("温馨提示")
      .setMessage("确定删除这条记录吗？")
      .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
      })
      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          DBManager.deleteFromAccount(targetBean.getId());
          mData.remove(targetBean); // 删除后刷新
          mAdapter.notifyDataSetChanged();
        }
      })
      .create()
      .show();
  }

  private void initTime() {
    Calendar calendar = Calendar.getInstance();
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH) + 1;
    history_tv_time.setText(year + "年" + month + "月");

    // 加载数据
    loadData(year, month);
  }

  private void loadData(int year, int month) {
    // 获取指定年份月份收支列表
    List<AccountBean> monthData = DBManager.getAccountListOneMonthData(year, month);
    mData.clear();
    mData.addAll(monthData);
    mAdapter.notifyDataSetChanged();
  }

  private void initView() {
    history_tv_time = findViewById(R.id.history_tv_time);
    history_lv = findViewById(R.id.history_lv);

    // 设置适配器
    mAdapter = new AccountListAdapter(this, mData);
    history_lv.setAdapter(mAdapter);
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.history_iv_back:
        finish();
        break;
      case R.id.history_iv_calendar:
        CalendarDialog dialog = new CalendarDialog(this,dialogYearSelectPosition,dialogMonthSelectPosition);
        dialog.show();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
          @Override
          public void onRefresh(int position, int year, int month) {

            history_tv_time.setText(year + "年" + month + "月");
            loadData(year, month);

            dialogYearSelectPosition = position;
            dialogMonthSelectPosition = month;
          }
        });
        break;
    }
  }
}