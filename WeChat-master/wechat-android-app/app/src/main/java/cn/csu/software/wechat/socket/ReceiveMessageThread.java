package cn.csu.software.wechat.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import cn.csu.software.wechat.entity.SocketData;

/**
 * 接收消息线程
 *
 * @author huangjishun 874904407@qq.com
 * @since 2019-10-12
 */
public class ReceiveMessageThread implements Runnable {
    private Socket socket;

    private ObjectInputStream objectInputStream;

    private MessageListener messageListener;

    public ReceiveMessageThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object object = objectInputStream.readObject();
                if (object instanceof SocketData) {
                    SocketData socketData = (SocketData) object;
                    messageListener.onMessageListener(socketData);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("close");
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public interface MessageListener {
        void onMessageListener(SocketData socketData);
    }
}
