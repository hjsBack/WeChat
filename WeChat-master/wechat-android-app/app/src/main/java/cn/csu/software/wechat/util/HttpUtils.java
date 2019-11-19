package cn.csu.software.wechat.util;

import cn.csu.software.wechat.entity.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpURLConnection 工具类
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-11-18
 */
public class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName();

    private static final int TIME_OUT = 10000;

    private static final int RESPONSE_CODE_SUCCESS = 200;

    private static final String REQUEST_METHOD_GET = "GET";

    private static final String REQUEST_METHOD_POST = "POST";

    private static final String PROPERTY_CONTENT_TYPE_KEY = "Content-Type";

    private static final String VALUE_JSON_UTF_8 = "application/json; utf-8";

    private HttpUtils() {
    }

    /**
     * GET方法，使用一个回调接口，成功则回调onSuccess方法，失败则回调onError方法
     * @param url 网址
     * @param callback 回调接口
     */
    public static void httpGet(String url, HttpCallback callback) {
        HttpResponse response = baseGet(url);
        if (response.getCode() == RESPONSE_CODE_SUCCESS) {
            callback.onSuccess(response.getContent());
        } else {
            callback.onError(response.getCode(), new Exception());
        }
    }

    /**
     * 基础的GET实现，不对外公布此方法，仅仅是被上面两个方法调用
     * 返回的HttpResponse类，包含状态码和响应内容。
     *
     * @param url url
     * @return HttpResponse
     */
    private static HttpResponse baseGet(String url) {
        HttpURLConnection conn = getHttpUrlConnection(url);
        HttpResponse response = new HttpResponse();
        BufferedReader reader;
        try {
            // 设置请求方式
            conn.setRequestMethod(REQUEST_METHOD_GET);
            conn.connect();
            response.setCode(conn.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            // 获取响应内容
            response.setContent(stringBuilder.toString());
            reader.close();
            conn.disconnect();
        } catch (IOException e) {
            LogUtil.e(TAG, "connect error");
        }
        return response;
    }

    /**
     * POST方法，使用一个回调接口，成功则回调onSuccess方法，失败则回调onError方法
     * @param url 网址
     * @param data post提交的数据
     * @param callback 回调接口
     */
    public static void httpPost(String url, String data, HttpCallback callback) {
        HttpResponse response = basePost(url, data);
        if (response.getCode() == RESPONSE_CODE_SUCCESS) {
            callback.onSuccess(response.getContent());
        } else {
            callback.onError(response.getCode(), new Exception());
        }
    }

    /**
     * 基础的POST实现，不对外公布此方法，只被上面两个POST方法调用
     * @param url 网址
     * @param data 提交的数据
     * @return HttpResponse
     */
    private static HttpResponse basePost(String url, String data) {
        HttpURLConnection conn = getHttpUrlConnection(url);
        HttpResponse response = new HttpResponse();
        BufferedReader reader;
        try {
            // 设置请求方式
            conn.setRequestMethod(REQUEST_METHOD_POST);
            // 获取输出流并转化为处理流
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            // 写入参数
            dos.writeUTF(data);
            conn.connect();
            response.setCode(conn.getResponseCode());
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            // 获取响应内容
            response.setContent(stringBuilder.toString());
            reader.close();
            conn.disconnect();
        } catch (IOException e) {
            LogUtil.e(TAG, "connect error");
        }
        return response;
    }

    /**
     * 获取HttpUrlConnection对象，并进行基础网络设置
     *
     * @param url url
     * @return HttpURLConnection
     */
    private static HttpURLConnection getHttpUrlConnection(String url) {
        HttpURLConnection conn = null;
        try {
            // 获取HttpURLConnection对象
            URL mUrl = new URL(url);
            conn = (HttpURLConnection) mUrl.openConnection();
            // 进行一些通用设置
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            conn.setRequestProperty(PROPERTY_CONTENT_TYPE_KEY, VALUE_JSON_UTF_8);
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException e) {
            LogUtil.e(TAG, "create connection error");
        }
        return conn;
    }

    /**
     * 回调接口
     *
     * @author  huangjishun 874904407@qq.com
     * @since 2019-11-18
     */
    public interface HttpCallback {
        /**
         * 成功
         *
         * @param response 返回内容
         */
        void onSuccess(String response);

        /**
         * 失败
         *
         * @param responseCode 状态码
         * @param exception Exception
         */
        void onError(int responseCode, Exception exception);
    }
}
