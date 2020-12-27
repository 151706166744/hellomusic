package com.example.mymusic.helps;
/**
 *1.用户登录
 *    1.1当用户进行登录时，利用SharedPreferences保存用户登录的用户标记（手机号码）
 *    1.2利用全局单例类UserHelper保存用登录用户信息
 *       1.2.1用户登录之后，
 *       1.2.2用户重新打开应用程序之后，检测SharedPreferences中是否存在登录用户标记，如果存在则为UserHelper进行赋值，并且进入主页。
 *       如果不存在则进入登录页面
 * 2.用户退出
 *     1.1删除掉SharedPreferences保存的用户标记，退出到登录界面
 */
public class UserHelper {
    private static UserHelper instance;
    private UserHelper(){};
    public static UserHelper getInstance(){
        if (instance==null) {
            synchronized (UserHelper.class){
                if (instance==null) {
                    instance=new UserHelper();
                }
            }
        }
        return instance;
    }
    private String phone;
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
