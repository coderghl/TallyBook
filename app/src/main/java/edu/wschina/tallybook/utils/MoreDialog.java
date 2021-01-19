package edu.wschina.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import edu.wschina.tallybook.R;
import edu.wschina.tallybook.activity.AboutActivity;
import edu.wschina.tallybook.activity.ChartActivity;
import edu.wschina.tallybook.activity.HistoryActivity;
import edu.wschina.tallybook.activity.SettingActivity;

public class MoreDialog extends Dialog {
  private final Context mContext;

  public MoreDialog(@NonNull Context context) {
    super(context);
    this.mContext = context;
  }

  private Button dialog_more_btn_about, dialog_more_btn_setting, dialog_more_btn_history, dialog_more_btn_info;
  private ImageView dialog_more_iv_close;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_more);
    initView();
    initEvent();
    setDialogSize();
  }

  private void setDialogSize() {
    Window window = getWindow();
    WindowManager.LayoutParams attributes = window.getAttributes();

    Display defaultDisplay = window.getWindowManager().getDefaultDisplay();
    attributes.width = defaultDisplay.getWidth();
    attributes.gravity = Gravity.BOTTOM;

    window.setBackgroundDrawableResource(android.R.color.transparent);
    window.setAttributes(attributes);
  }

  private void initView() {
    dialog_more_btn_about = findViewById(R.id.dialog_more_btn_about);
    dialog_more_btn_setting = findViewById(R.id.dialog_more_btn_setting);
    dialog_more_btn_history = findViewById(R.id.dialog_more_btn_history);
    dialog_more_btn_info = findViewById(R.id.dialog_more_btn_info);
    dialog_more_iv_close = findViewById(R.id.dialog_more_iv_close);
  }

  public void jumpActivity(Class<?> target) {
    Intent intent = new Intent(mContext,target);
    mContext.startActivity(intent);
  }
  private void initEvent() {
    dialog_more_btn_about.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(AboutActivity.class);
        cancel();
      }
    });

    dialog_more_btn_setting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(SettingActivity.class);
        cancel();
      }
    });

    dialog_more_btn_history.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(HistoryActivity.class);
        cancel();
      }
    });

    dialog_more_btn_info.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jumpActivity(ChartActivity.class);
        cancel();
      }
    });

    dialog_more_iv_close.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancel();
      }
    });
  }

}
