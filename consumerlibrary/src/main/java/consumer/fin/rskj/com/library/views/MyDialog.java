package consumer.fin.rskj.com.library.views;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by HP on 2017/9/12.
 */

public class MyDialog extends ProgressDialog {
    Context context;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }


    public void onDestory() {
        context = null;
    }
}
