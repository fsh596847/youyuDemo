package consumer.fin.rskj.com.library.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * mToast
 */
public class ToastUtils {
  private static Toast mToast = null;

  public static void showCenterToast(String text, Context context) {
    if (mToast == null) {
      mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
      mToast.setGravity(Gravity.CENTER, 0, 0);
    } else {
      mToast.setText(text);
      mToast.setDuration(Toast.LENGTH_SHORT);
      mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    mToast.show();
  }
}
