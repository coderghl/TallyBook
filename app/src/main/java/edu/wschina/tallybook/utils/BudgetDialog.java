package edu.wschina.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import edu.wschina.tallybook.R;

public class BudgetDialog extends Dialog {
  private OnEnSureListener mListener;

  public BudgetDialog(@NonNull Context context) {
    super(context);
  }

  private Button dialog_budget_btn_yes, dialog_budget_btn_no;
  private EditText dialog_budget_et;

  public interface OnEnSureListener {
    void enSure(float money);
  }

  public void setOnEnSureListener(OnEnSureListener listener) {
    this.mListener = listener;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_budget);
    initView();
    initEvent();
    setDialogSize();
  }

  private void initEvent() {
    dialog_budget_btn_yes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String data = dialog_budget_et.getText().toString();
        if (data != null && data.length() > 0) {
          float money = Float.parseFloat(data);
          if (money > 0) {
            if (mListener != null) {
              mListener.enSure(money);
            }
          } else {
            Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
        }
        cancel();
      }
    });

    dialog_budget_btn_no.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        cancel();
      }
    });
  }

  private void initView() {
    dialog_budget_btn_yes = findViewById(R.id.dialog_budget_btn_yes);
    dialog_budget_btn_no = findViewById(R.id.dialog_budget_btn_no);
    dialog_budget_et = findViewById(R.id.dialog_budget_et);
  }


  // 设置dialog的尺寸和屏幕尺寸一致
  public void setDialogSize() {
    Window window = getWindow();

    // 获取窗口参数对象
    WindowManager.LayoutParams attributes = window.getAttributes();

    // 获取屏幕宽度
    Display display = window.getWindowManager().getDefaultDisplay();
    attributes.width = display.getWidth(); //对话框窗口为屏幕窗口
    attributes.gravity = Gravity.BOTTOM;
    window.setBackgroundDrawableResource(android.R.color.transparent);
    window.setAttributes(attributes);
    // 延迟一秒打开系统软键盘
    mHandler.sendEmptyMessageDelayed(1,500);
  }

  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(@NonNull Message msg) {
      // 自动弹出软键盘
      InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }
  };
}
