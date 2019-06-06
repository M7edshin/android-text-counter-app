package shahin.textcounter;


import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

public class Constant {
    public static final String MARKET_APP_ID = "market://details?id=shahin.textcounter";
    public static final String PRIVACY_POLICY = "https://github.com/M7edshin/Privacy-Policy";

    public static void setDialog(Context context, String title, String str) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(str)
                .setNegativeButton(android.R.string.ok, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
