package com.example.vaporwaveVideo.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.vaporwaveVideo.R;
import com.example.vaporwaveVideo.ui.WebViewActivity;
import com.example.vaporwaveVideo.util.DensityUtil;

public class ShowDialogSet {
   private Activity activity;
    public ShowDialogSet(Activity activity) {
        this.activity = activity;
    }
    public  void  initDialog(int layid){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        View view=activity.getLayoutInflater().inflate(layid,null,false);
        TextView dialog_Privacy_Policy=view.findViewById(R.id.dialog_Privacy_Policy);
        TextView dialog_Terms=view.findViewById(R.id.dialog_Terms);
        setDrawableSize(dialog_Privacy_Policy,R.drawable.icon_arrow_right);
        setDrawableSize(dialog_Terms,R.drawable.icon_arrow_right);
        builder.setView(view);
        final AlertDialog alertDialog=builder.show();
        WindowManager.LayoutParams params =
                alertDialog.getWindow().getAttributes();
        params.width = DensityUtil.dip2px(activity,280);
        params.height = DensityUtil.dip2px(activity,220);
        alertDialog.getWindow().setAttributes(params);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog_Privacy_Policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.loadUrl(activity,activity.getResources().getText(R.string.privacy).toString(),"https://sites.google.com/view/neonpu/home");
                alertDialog.dismiss();
            }
        });
        dialog_Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebViewActivity.loadUrl(activity,activity.getResources().getText(R.string.terms).toString(),"https://sites.google.com/view/neonpu/terms");
                alertDialog.dismiss();
            }
        });
    }

    //设置图片尺寸
    private  void setDrawableSize(TextView radioButton, int id){
        Drawable drawable=activity.getResources().getDrawable(id);
        drawable.setBounds(0,0, DensityUtil.dip2px(activity,30),DensityUtil.dip2px(activity,30));
        radioButton.setCompoundDrawables(null,null,drawable,null);
    }
}
