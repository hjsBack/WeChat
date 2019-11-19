package cn.csu.software.wechat.socket;

import cn.csu.software.wechat.constant.Configure;
import cn.csu.software.wechat.entity.SocketData;
import cn.csu.software.wechat.util.LogUtil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端线程
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-12
 */
public class SocketClient implements Runnable, ReceiveMessageThread.MessageListener {
    private static final String TAG = SocketClient.class.getSimpleName();

    private static final String SERVER_HOST = "34.92.16.72";

    private static final int SERVER_PORT = 8888;

    private static SocketClient socketClient;

    private Socket socket;

    private ObjectOutputStream objectOutputStream;

    private SocketClientListener socketClientListener;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 静态工厂方法
     *
     * @return SocketClient
     */
    public static SocketClient getInstance() {
        if (socketClient == null) {
            socketClient = new SocketClient();
        }
        return socketClient;
    }

    /**
     * 断开连接
     */
    public void close() {
        try {
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            LogUtil.i(TAG, "close socket error");
        }
    }

    /**
     * 发送消息
     *
     * @param socketData SocketData
     * @throws IOException IO异常
     */
    public void sendMessage(SocketData socketData) throws IOException {
        objectOutputStream.writeObject(socketData);
    }

    private void sendFirstMessage() {
        SocketData socketData = new SocketData();
        socketData.setSenderAccount(Configure.getMyAccount());
        socketData.setMessageType(-1);
        try {
            objectOutputStream.writeObject(socketData);
        } catch (IOException e) {
            LogUtil.e(TAG, "send first message error");
        }
    }

    @Override
    public void onMessageListener(SocketData socketData) {
        socketClientListener.onSocketClientListener(socketData);
    }

    @Override
    public void run() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LogUtil.e(TAG, "start socket error:  %s", e);
        }
        LogUtil.i(TAG, "successful connected to server: %s", socket.getInetAddress());
        ReceiveMessageThread receiveMessageThread = new ReceiveMessageThread(this.socket);
        receiveMessageThread.setMessageListener(this);
        sendFirstMessage();
        executorService.execute(receiveMessageThread);
    }

    public void setSocketClientListener(SocketClientListener socketClientListener) {
        this.socketClientListener = socketClientListener;
    }

    /**
     * 接收消息监听接口
     *
     * @author huangjishun 874904407@qq.com
     * @since 2019-10-12
     */
    public interface SocketClientListener {
        /**
         * 接收消息回调方法
         *
         * @param socketData SocketData
         */
        void onSocketClientListener(SocketData socketData);
    }
}