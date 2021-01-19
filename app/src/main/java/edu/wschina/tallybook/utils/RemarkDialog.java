package edu.wschina.tallybook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.annotation.NonNull;

import edu.wschina.tallybook.R;

public class RemarkDialog extends Dialog implements View.OnClickListener {

  private OnEnsureListener mListener;

  public RemarkDialog(@NonNull Context context) {
    super(context);
  }

  private Button dialog_remark_btn_no, dialog_remark_btn_yes;
  private EditText dialog_remark_et;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_remark);
    initView();
    setDialogSize();
  }

  private void initView() {
    dialog_remark_btn_no = findViewById(R.id.dialog_remark_btn_no);
    dialog_remark_btn_yes = findViewById(R.id.dialog_remark_btn_yes);
    dialog_remark_et = findViewById(R.id.dialog_remark_et);

    dialog_remark_btn_no.setOnClickListener(this);
    dialog_remark_btn_yes.setOnClickListener(this);
  }

  // 使用接口让外部处理数据
  public interface OnEnsureListener {
    void onEnsure(String data);

    void no();
  }

  public void setOnEnsureListener(OnEnsureListener listener) {
    this.mListener = listener;
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

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.dialog_remark_btn_yes:
        if (mListener != null) {
          String data = dialog_remark_et.getText().toString().trim();
          if (!data.isEmpty()) {
            mListener.onEnsure(data);
          }
        }
        break;
      case R.id.dialog_remark_btn_no:
        if (mListener != null) {
          mListener.no();
        }
        cancel();
        break;
    }
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
