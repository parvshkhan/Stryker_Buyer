package com.app.Dialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.app.stryker.R;
import com.app.utill.AppUtil;

public class UpdateDialog extends Dialog {

    private Button btUpdate,btCancel;
    private Context context;

    public UpdateDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update);

        btUpdate = (Button)findViewById(R.id.update);
        btCancel = (Button)findViewById(R.id.cancel);

        setCancelable(false);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApp();

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.setFlagUpdate(context,false);
                dismiss();
            }
        });

    }
    public void updateApp() {
        try {
            Intent updateIntent = updateIntentForUrl("market://details");
            context.startActivity(updateIntent);
        } catch (ActivityNotFoundException e) {
            Intent updateIntent = updateIntentForUrl("https://play.google.com/store/apps/details");
            context.startActivity(updateIntent);
        }
    }

    private Intent updateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }
}
