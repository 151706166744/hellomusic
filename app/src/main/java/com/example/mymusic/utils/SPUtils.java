package com.example.mymusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.mymusic.constants.SPConstants;
import com.example.mymusic.helps.UserHelper;

public class SPUtils {
    /**
     * 1.1当用户进行登录时，利用SharedPreferences保存用户登录的用户标记（手机号码）
     */
    public static boolean saveUser(Context context,String phone){
        SharedPreferences sp = context.getSharedPreferences(SPConstants.SP_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SPConstants.SP_KEY_PHONE,phone);
        boolean result = editor.commit();
        return result;
    }
    /**
     * 验证是否存在已经登录用户
     */
    public static boolean isLoginUser(Context context){
        boolean result=false;
        SharedPreferences sp = context.getSharedPreferences(SPConstants.SP_NAME_USER, Context.MODE_PRIVATE);
        String phone = sp.getString(SPConstants.SP_KEY_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            result=true;
            UserHelper.getInstance().setPhone(phone);
        }
        return result;
    }
}
