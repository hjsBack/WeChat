package cn.csu.software.wechat.entity;

/**
 * http 返回结果类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-18
 */
public class HttpResponse {
    private int code;
    private String content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
