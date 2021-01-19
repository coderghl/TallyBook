package edu.wschina.tallybook.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import edu.wschina.tallybook.R;

public class KeyBoardUtils {
  private KeyboardView mKeyboardView;
  private EditText mEditText;
  private final Keyboard mKeyboard; // 自定义键盘
  private OnEnsureListener mOnEnsureListener;

  // 当点击完成后，通过接口回调，把数据传递到外部
  public interface OnEnsureListener {
    void onEnsure();
  }

  // 让外部实现
  public void setOnEnsureListener(OnEnsureListener listener) {
    this.mOnEnsureListener = listener;
  }

  public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
    mKeyboardView = keyboardView;
    mEditText = editText;
    this.mEditText.setInputType(InputType.TYPE_NULL); // 取消系统弹出键盘

    mKeyboard = new Keyboard(this.mEditText.getContext(), R.xml.key);

    this.mKeyboardView.setKeyboard(mKeyboard); // 设置键盘View显示我们自己写的键盘
    this.mKeyboardView.setEnabled(true); // 设置可以被点击
    this.mKeyboardView.setPreviewEnabled(false); // 设置键盘可以优先使用
    this.mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener); // 设置键盘按钮被点击时候的监听
  }

  KeyboardView.OnKeyboardActionListener mOnKeyboardActionListener = new KeyboardView.OnKeyboardActionListener() {
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    // 这个回调可以获取键盘点击的目标
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
      Editable text = mEditText.getText(); // 获取输入框操作内容类
      int start = mEditText.getSelectionStart(); // 获取光标首位下标

      switch (primaryCode) {
        case Keyboard.KEYCODE_DELETE: // 点击了删除
          // 只有当输入框中才能进行删除，每次删除一位
          if (text != null && text.length() > 0) {
            if (start > 0) {
              text.delete(start - 1, start);
            }
          }
          break;
        case Keyboard.KEYCODE_CANCEL: // 点击了清零
          text.clear(); // 直接清零
          break;
        case Keyboard.KEYCODE_DONE: // 点击了确定
          if (mOnEnsureListener != null) {
            mOnEnsureListener.onEnsure();
          }
          break;
        default:
          // 点击数字时插入到Edittext文本框中
          text.insert(start, Character.toString((char) primaryCode));
          break;
      }
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
  };

  // 显示键盘的方法
  public void showKeyBoard() {
    int visibility = mKeyboardView.getVisibility();

    if (visibility == View.INVISIBLE || visibility == View.GONE) {
      mKeyboardView.setVisibility(View.VISIBLE);
    }

  }

  // 显示键盘
  public void hindKeyBoard(){
    int visibility = mKeyboardView.getVisibility();

    if(visibility == View.INVISIBLE || visibility == View.VISIBLE){
      mKeyboardView.setVisibility(View.GONE);
    }
  }
}
