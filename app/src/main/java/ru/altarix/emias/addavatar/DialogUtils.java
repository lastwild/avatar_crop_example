package ru.altarix.emias.addavatar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by fife on 04.02.2016.
 */
public class DialogUtils {
    private Context context;

    public DialogUtils(Context context) {
        this.context = context;
    }

    public void createAlertDialog(CharSequence[] items, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context).setCancelable(true)
                .setItems(items, listener).create().show();
    }
}
