package com.example.mymusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.mymusic.R;
import com.example.mymusic.activities.LoginActivity;
import com.example.mymusic.constants.SPConstants;
import com.example.mymusic.helps.RealmHelper;
import com.example.mymusic.helps.UserHelper;
import com.example.mymusic.models.UserModel;

import java.util.List;

import io.realm.Realm;

public class UserUtils {
    /*验证用户输入合法性*/
    public static boolean validateLogin(Context context,String phone,String password){
        /*RegexUtils.isMobileSimple(phone);*/
        RegexUtils.isMobileExact(phone);
        if (!RegexUtils.isMobileExact(phone)) {
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        /**
         * 1.当前用户手机号是否已经被注册了
         * 2.用户输入的手机号和密码是否匹配
         */
        if (!UserUtils.userExitFromPhone(phone)) {
            Toast.makeText(context,"当前手机号未注册",Toast.LENGTH_SHORT).show();
            return false;
        }
        RealmHelper realmHelper = new RealmHelper();
        boolean result= realmHelper.validateUser(phone,EncryptUtils.encryptMD5ToString(password));

        if (!result) {
            Toast.makeText(context,"手机号或密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        /**
         * 保存用户登录标记
         */
        boolean isSave = SPUtils.saveUser(context, phone);
        if (!isSave) {
            Toast.makeText(context,"系统错误，请稍后重试",Toast.LENGTH_SHORT).show();
            return false;
        }
        /**
         * 保存用户标记，在全局单例类之中
         */
        UserHelper.getInstance().setPhone(phone);

        /**
         * 保存音乐源数据
         * */
        realmHelper.setMusicSource(context);

        realmHelper.close();
        return true;
    }
    /*退出登录*/
    public static void logout(Context context){
        /**
         * 删除sp保存的用户标记
         */
        boolean isRemove = UserUtils.removeUser(context);
        if (!isRemove) {
            Toast.makeText(context,"系统错误，请稍后重试",Toast.LENGTH_SHORT).show();
            return;
        }

        /**
         * 删除数据源
         * */
        RealmHelper realmHelper = new RealmHelper();
        /*realmHelper.removeMusicSource();*/
        realmHelper.close();

        Intent intent = new Intent(context, LoginActivity.class);
        /*添加intent标志符，清理task栈，并且新生成一个task栈*/
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        /*定义Activity跳转动画*/
        ((Activity)context).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }

    /**
     * 注册用户
     * @param context
     * @param phone
     * @param password
     * @param passwordConfirm
     */
    public static boolean registerUser(Context context,String phone,String password,String passwordConfirm){
        if (!RegexUtils.isMobileExact(phone)) {
            Toast.makeText(context,"无效手机号",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (StringUtils.isEmpty(password) || !password.equals(passwordConfirm)) {
            Toast.makeText(context,"请确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        /*用户当前输入手机号是否已经被注册*/
        /**
        * 1.通过Realm获取到当前已经注册的所有用户
        * 2.根据用户输入的手机号匹配查询的所有用户，如果匹配则证明该手机号已经被注册了，否则表示手机号还未注册
        * */
        if (UserUtils.userExitFromPhone(phone)) {
            Toast.makeText(context,"该手机号已经存在",Toast.LENGTH_SHORT).show();
            return false;
        }

        UserModel userModel = new UserModel();
        userModel.setPhone(phone);
        /*userModel.setPassword(password);*/
        userModel.setPassword(EncryptUtils.encryptMD5ToString(password));

        saveUser(userModel);
        return true;
    }

    /**
     * 保存用户到数据库
     * @param userModel
     */
    public static void saveUser(UserModel userModel){
        RealmHelper realmHelper = new RealmHelper();
        realmHelper.saveUser(userModel);
        realmHelper.close();
    }
    /**
     * 根据手机号判断用户是否存在
     */
    public static boolean userExitFromPhone(String phone){
        boolean result=false;

        RealmHelper realmHelper = new RealmHelper();
        List<UserModel> allUser= realmHelper.getAllUser();

        for (UserModel userModel : allUser) {
            if (userModel.getPhone().equals(phone)) {
                /*当前手机号已经存在数据库中了*/
                result=true;
                break;
            }
        }
        realmHelper.close();
        return result;
    }
    /**
     * 验证是否存在已登录用户
     */
    public static boolean validateUserLogin(Context context){
        return SPUtils.isLoginUser(context);
    }
    /**
     * 删除用户标记
     */
    public static boolean removeUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(SPConstants.SP_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(SPConstants.SP_KEY_PHONE);
        boolean result = editor.commit();
        return result;
    }
    /**
     * 修改密码
     * 1.数据验证
     *    1.1原密码是否输入
     *    1.2新密码是否输入并且确定是否相同
     *    1.3原密码是否正确
     *       Realm获取到当前的用户模型
     *       根据用户模型中保存的密码匹配用户原密码
     * 2.利用Realm模型自动更新的特性完成密码的修改
     */
    public static boolean changePassword(Context context,String oldPassword,String password,String passwordConfirm){
        if (TextUtils.isEmpty(oldPassword)) {
            Toast.makeText(context,"请输入原密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password) ||!password.equals(passwordConfirm)) {
            Toast.makeText(context,"请确认密码",Toast.LENGTH_SHORT).show();
            return false;
        }

        /**
         * 验证原密码是否正确
          */
        RealmHelper realmHelper = new RealmHelper();
        UserModel userModel = realmHelper.getUser();
        if (!EncryptUtils.encryptMD5ToString(oldPassword).equals(userModel.getPassword())) {
            Toast.makeText(context,"原密码不正确",Toast.LENGTH_SHORT).show();
            return false;
        }
        realmHelper.changePassword(EncryptUtils.encryptMD5ToString(password));
        realmHelper.close();
        return true;
    }
}
