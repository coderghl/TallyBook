package edu.wschina.tallybook.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.wschina.tallybook.db.DBManager;

public class BaseActivity extends AppCompatActivity {
  private Context mContext;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = this;

    // 初始化数据库
    DBManager.initDB(mContext);
  }

  public void jumpActivity(Class<?> target) {
    Intent intent = new Intent(mContext,target);
    startActivity(intent);
  }
}
