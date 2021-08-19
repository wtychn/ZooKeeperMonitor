package com.wtychn.zookeeper.watcher;

import com.wtychn.zookeeper.pojo.ServerInfo;
import com.wtychn.zookeeper.utils.WebSocketServer;
import com.wtychn.zookeeper.utils.ZooKeeperUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerWatcherThread extends Thread {

    ServerInfo serverInfo;

    public ServerWatcherThread(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            ServerInfo newInfo = ZooKeeperUtil.getServerInfo(serverInfo);
            if (!newInfo.equals(serverInfo)) {
                serverInfo = newInfo;
                WebSocketServer.sendInfo("ServerStatChanged");
                log.info("[服务器状态更改]");
            }
            Thread.sleep(2000);
        }
    }

}
