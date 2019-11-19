package cn.csu.software.wechat.constant;

/**
 * 配置文件
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-19
 */
public class Configure {
    private static boolean isLogin = false;

    private Configure() {
    }

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static void setIsLogin(boolean isLogin) {
        Configure.isLogin = isLogin;
    }
}
