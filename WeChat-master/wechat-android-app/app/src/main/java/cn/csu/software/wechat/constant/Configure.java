package cn.csu.software.wechat.constant;

/**
 * 配置文件
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class Configure {
    private static boolean isLogin = false;

    private static int myAccount;

    private Configure() {
    }

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        Configure.isLogin = isLogin;
    }

    public static int getMyAccount() {
        return myAccount;
    }

    public static void setMyAccount(int myAccount) {
        Configure.myAccount = myAccount;
    }
}
