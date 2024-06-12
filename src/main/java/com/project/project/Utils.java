package com.project.project;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.project.project.acommon.data.Users;
import com.project.project.salon.data.Items;
import com.project.project.voice.MyAlarmService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

public class Utils {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void displayDirection(Context context) {
    }

    public static int calculateTotalAmount(List<Items> cartList){
        int amount = 0;
        for(Items cart : cartList){
            amount = amount + cart.getCalculatedPrice();
        }
        return amount;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static void launchApp(Context context, String packagename){

        Intent intent = new Intent();
        intent.setPackage(packagename);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        Log.i("Launchapp", "ResolveInfo: " + resolveInfos.size());
        if (resolveInfos.size() > 0) {
            ResolveInfo launchable = resolveInfos.get(0);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);
            context.startActivity(i);
        }
    }

    public static String  fetchDeviceDetails() {

        String mString = "";
        mString = mString + "VERSION.RELEASE {" + Build.VERSION.RELEASE + "}" +
                "\nVERSION.INCREMENTAL {" + Build.VERSION.INCREMENTAL + "}" +
                "\nVERSION.SDK {" + Build.VERSION.SDK + "}" +
                "\nBOARD {" + Build.BOARD + "}" +
                "\nBRAND {" + Build.BRAND + "}" +
                "\nDEVICE {" + Build.DEVICE + "}" +
                "\nFINGERPRINT {" + Build.FINGERPRINT + "}" +
                "\nHOST {" + Build.HOST + "}" +
                "\nID {" + Build.ID + "}";
        return mString;
    }

    public static void playVoice(Activity activity, String message) {
        Intent intentService = new Intent(activity, MyAlarmService.class);
        intentService.putExtra("text", message);
        activity.startService(intentService);
        Log.i("Utils", "message called");
    }

    public static void sendSMS(String phone, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null, message, null, null);
    }

    public static void launchExternalApp(String packagename, Context context){
        String TAG = "Utils";

        Intent intent = new Intent();
        intent.setPackage(packagename);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
        Log.i(TAG, "ResolveInfo: " + resolveInfos.size());
        if (resolveInfos.size() > 0) {
            ResolveInfo launchable = resolveInfos.get(0);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);
            context.startActivity(i);
        }
    }

    public static void openURL(String url , Activity activity){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    public static void sendSMSToUsers(String title){
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Users.class.getSimpleName());
        query.setLimit(500);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() > 0){
                    for(ParseObject po : objects){
                        sendSMS(po.getString(Users.phone), "Dear "+ po.getString(Users.name) + ", new book "+
                                title + " added.");
                    }
                }
            }
        });
    }

    private static void copyInputStreamToFile(InputStream in, File file) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
                in.close();
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public static void openMaps(String loc, Context context){
        String uri = "http://maps.google.com/maps?q=" + loc + " (" + "place" + ")";
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
