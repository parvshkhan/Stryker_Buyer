package com.app.utill;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.app.stryker.InviteandEarn;
import com.app.stryker.R;
import com.google.gson.Gson;
import com.quickblox.chat.model.QBPresence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {

    static Gson gson;

    public static Gson getGsonInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static void setFlagUpdate(Context context,boolean update)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("updateflag",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("updatef",update);
        editor.apply();

    }



    public static boolean getFlagUpdate(Context  context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("updateflag",Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean("updatef",true);
        return b;
    }






    public static void setVersionCode(String code,Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("version",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("code",code);
        editor.commit();

    }


    public static String getVersionCode(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("version",context.MODE_PRIVATE);
        String code =sharedPreferences.getString("code","1");
        return code;

    }

    public static void showCustomToast3(Context con, String message) {

        Context context = con;
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = ((Activity) con).getLayoutInflater();
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast_layout, null);
        TextView text = (TextView) toastRoot.findViewById(R.id.toast_txt);
        text.setText(message);

        Toast toast = new Toast(context);

        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();


    }

    public static String getMinOrderValue(Context c) {

        SharedPreferences sp = c.getSharedPreferences("minorder",
                Context.MODE_PRIVATE);
        return sp.getString("minorder", "");

    }

    public static void setMinOrderValue(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("minorder",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("minorder", data);
        e.commit();

    }

    public static void setOrderServiceData(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("OrderServiceData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("OrderServiceData", data);
        e.commit();

    }

    public static String getOrderServiceData(Context c) {

        SharedPreferences sp = c.getSharedPreferences("OrderServiceData",
                Context.MODE_PRIVATE);
        return sp.getString("OrderServiceData", "");

    }

    public static String getUserEmail(Context c) {

        SharedPreferences sp = c.getSharedPreferences("emailid",
                Context.MODE_PRIVATE);
        return sp.getString("emailid", "");

    }

    public static void setUserEmail(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("emailid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("emailid", data);
        e.commit();

    }

    public static void setUserChatID(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("chatid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("chatid", data);
        e.commit();

    }

    public static void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please Login First")
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void setUserAddress(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("address",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("address", data);
        e.commit();
    }

    public static String getUserAddress(Context c) {

        SharedPreferences sp = c.getSharedPreferences("address",
                Context.MODE_PRIVATE);
        return sp.getString("address", "");
    }

    public static void setpreStoreId(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("storeid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("storeid", data);
        e.commit();
    }

    public static String getpreStoreId(Context c) {

        SharedPreferences sp = c.getSharedPreferences("storeid",
                Context.MODE_PRIVATE);
        return sp.getString("storeid", "");
    }

    public static void setLogin(Context c, boolean data) {
        SharedPreferences sp = c.getSharedPreferences("islogin",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("islogin", data);
        e.commit();
    }

    public static boolean getLogin(Context c) {

        SharedPreferences sp = c.getSharedPreferences("islogin",
                Context.MODE_PRIVATE);
        return sp.getBoolean("islogin", false);

    }

    public static void setFirstLogin(Context c, boolean data) {
        SharedPreferences sp = c.getSharedPreferences("isfirstlogin",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("isfirstlogin", data);
        e.commit();
    }

    public static boolean getFirstLogin(Context c) {

        SharedPreferences sp = c.getSharedPreferences("isfirstlogin",
                Context.MODE_PRIVATE);
        return sp.getBoolean("isfirstlogin", true);

    }

    public static void setDeviceId(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("deviceid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("deviceid", data);
        e.commit();

    }

    public static String getDeviceId(Context c) {

        SharedPreferences sp = c.getSharedPreferences("deviceid",
                Context.MODE_PRIVATE);
        return sp.getString("deviceid", "");

    }

    public static void setUserId(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("userid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userid", data);
        e.commit();

    }

    public static void setLoggedInUser(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("loggedinuser",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("loggedinuser", data);
        e.commit();

    }

    public static String getULoggedInUser(Context c) {

        SharedPreferences sp = c.getSharedPreferences("loggedinuser",
                Context.MODE_PRIVATE);
        return sp.getString("loggedinuser", "");

    }

    public static String getUserId(Context c) {

        SharedPreferences sp = c.getSharedPreferences("userid",
                Context.MODE_PRIVATE);
        return sp.getString("userid", "");

    }

    public static void setChatUserLoginId(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("chatLoginid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("chatLoginid", data);
        e.commit();
    }

    public static String getChatUserLoginId(Context c) {

        SharedPreferences sp = c.getSharedPreferences("chatLoginid",
                Context.MODE_PRIVATE);
        return sp.getString("chatLoginid", "");

    }

    public static void setChatUserId(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("chatuserid",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("chatuserid", data);
        e.commit();
    }

    public static String getChatUserId(Context c) {

        SharedPreferences sp = c.getSharedPreferences("chatuserid",
                Context.MODE_PRIVATE);
        return sp.getString("chatuserid", "");

    }

    public static void setUserName(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("username",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("username", data);
        e.commit();

    }

    public static String getUserName(Context c) {

        SharedPreferences sp = c.getSharedPreferences("username",
                Context.MODE_PRIVATE);
        return sp.getString("username", "");

    }

    public static void setUserLocation(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("userlocation",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userlocation", data);
        e.commit();

    }

    public static String getUserLocation(Context c) {

        SharedPreferences sp = c.getSharedPreferences("userlocation",
                Context.MODE_PRIVATE);
        return sp.getString("userlocation", "");

    }

    public static void setUserPic(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("userPic",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userPic", data);
        e.commit();

    }

    public static String getUserPic(Context c) {

        SharedPreferences sp = c.getSharedPreferences("userPic",
                Context.MODE_PRIVATE);
        return sp.getString("userPic", "");

    }

    public static void setIsGPSOn(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("gpson",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("gpson", data);
        e.commit();

    }

    public static String getIsGPSOn(Context c) {

        SharedPreferences sp = c.getSharedPreferences("gpson",
                Context.MODE_PRIVATE);
        return sp.getString("gpson", "");

    }

    public static void setIsChatDelete(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("chat",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("chat", data);
        e.commit();

    }

    public static String getIsChatDelete(Context c) {

        SharedPreferences sp = c.getSharedPreferences("chat",
                Context.MODE_PRIVATE);
        return sp.getString("chat", "");

    }

    public static Typeface getRegularTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "abeezeeregular.otf");
    }

    public static Typeface getLightTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "opensanshebrewlight.ttf");
    }

    public static String getDeviceGCMId(Context context) {
        String ud_id = "99999";
        try {
            SharedPreferences sub_share = context.getSharedPreferences("regid",
                    0);

            if (!sub_share.equals(null)) {
                ud_id = sub_share.getString("rid", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ud_id;

        }

        return ud_id;

    }

    public static void showCustomToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showCustomToast2(Context context, String message) {
        Toast toast = Toast.makeText(context,
                message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
        toast.show();

    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass,
                                             Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void onKeyBoardDown(Context con) {
        try {
            InputMethodManager inputManager = (InputMethodManager) con
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) con)
                            .getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static String getrefreshValue(Context c) {

        SharedPreferences sp = c.getSharedPreferences("referesh",
                Context.MODE_PRIVATE);
        return sp.getString("referesh", "");

    }

    public static void setrefreshValue(Context c, String data) {
        SharedPreferences sp = c.getSharedPreferences("referesh",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("referesh", data);
        e.commit();

    }


    public static void SetAddStoreFlag(Context c ,boolean b)
    {
        SharedPreferences sp = c.getSharedPreferences("referesh",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("str", b);
        e.commit();



    }
    public static boolean GetAddStoreFlag(Context c)
    {
        SharedPreferences sp = c.getSharedPreferences("referesh",
                Context.MODE_PRIVATE);

        return sp.getBoolean("str",false);




    }


    public static void setAssociateName(String user, InviteandEarn context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ass",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",user);
        editor.apply();

    }

    public static void setAssociatePass(String pass, InviteandEarn context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ass2",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pass",pass);
        editor.apply();
    }

    public static String getAssociateName(InviteandEarn context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ass",Context.MODE_PRIVATE);
        return (sharedPreferences.getString("name",null));
    }
    public static String getAssociatepass(InviteandEarn context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ass2",Context.MODE_PRIVATE);
        return (sharedPreferences.getString("pass",null));
    }
}
