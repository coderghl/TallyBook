package edu.wschina.tallybook.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.db.DBManager;

public class SettingActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.setting_delete:
        showDeleteDialog();
        break;
      case R.id.setting_back:
        finish();
        break;
    }
  }

  private void showDeleteDialog() {
    new AlertDialog.Builder(this)
      .setTitle("温馨提示")
      .setMessage("确定删除所有记录吗？删除后无法恢复")
      .setNegativeButton("取消", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
      })
      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          DBManager.deleteAllAccount();
          Toast.makeText(SettingActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
        }
      })
      .create()
      .show();
  }
}