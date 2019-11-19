package cn.csu.software.wechat.server;

import java.util.HashMap;
import java.util.Map;

public class SocketQueue {
    private static Map<Integer, SocketThread> socketMap = new HashMap<>();

    public static Map<Integer, SocketThread> getSocketMap() {
        return socketMap;
    }

    public static void setSocketMap(Map<Integer, SocketThread> socketMap) {
        SocketQueue.socketMap = socketMap;
    }
}
