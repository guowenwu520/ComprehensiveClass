package com.example.vaporwaveVideo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppUtils {

    public static void shareVideo(Context context, String path, String packageName) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.setPackage(packageName);
        File shareFile = new File(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", shareFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        }
        try {
            context.startActivity(shareIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ShareItem> getAllSupportShareImageApp(Context context) {
        Intent resolveIntent = new Intent(Intent.ACTION_SEND);
        resolveIntent.setType("image/*");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveinfoList = pm
                .queryIntentActivities(resolveIntent, 0);
        List<ShareItem> mShares = new ArrayList<>();
        for (int i = 0; i < resolveinfoList.size(); i++) {
            ShareItem item = resolve2ShareItem(pm, resolveinfoList.get(i));
            mShares.add(item);
        }
        return mShares;
    }

    public static ShareItem resolve2ShareItem(PackageManager pm, ResolveInfo resolve) {
        try {
            return new ShareItem(resolve.loadIcon(pm), resolve.loadLabel(pm).toString(), resolve.activityInfo.packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ShareItem {
        public Drawable icon;
        public String name;
        public String packageName;

        public ShareItem(Drawable icon, String name, String packageName) {
            this.icon = icon;
            this.name = name;
            this.packageName = packageName;
        }
    }


}
